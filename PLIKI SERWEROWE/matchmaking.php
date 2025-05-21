<?php
require_once 'db_connection.php';

header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);
$user_id = isset($data['user_id']) ? (int)$data['user_id'] : null;
$category_id = isset($data['category_id']) ? (int)$data['category_id'] : null;
$tryb_id = 1; // Tryb 1 vs 1

if (!$user_id || !$category_id) {
    error_log("Błąd matchmakingu: Brak user_id ($user_id) lub category_id ($category_id)");
    echo json_encode(['success' => false, 'message' => 'Brak user_id lub category_id']);
    exit;
}

// Sprawdzanie istniejącej gry w stanie 'waiting'
$query = "SELECT id FROM rozgrywka WHERE tryb_id = ? AND kategoria_id = ? AND status = 'waiting' AND max_players = 2 LIMIT 1";
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, 'ii', $tryb_id, $category_id);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$game = mysqli_fetch_assoc($result);
mysqli_stmt_close($stmt);

if ($game) {
    $rozgrywka_id = $game['id'];

    // Sprawdzanie liczby graczy w sesji
    $query = "SELECT COUNT(*) as player_count, GROUP_CONCAT(user_id) as users FROM sesje_graczy WHERE rozgrywka_id = ?";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, 'i', $rozgrywka_id);
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);
    $session_data = mysqli_fetch_assoc($result);
    $player_count = (int)$session_data['player_count'];
    $existing_users = explode(',', $session_data['users'] ?? '');
    mysqli_stmt_close($stmt);

    error_log("Rozgrywka ID: $rozgrywka_id, Liczba graczy: $player_count, Istniejący użytkownicy: " . implode(',', $existing_users));

    // Sprawdzanie, czy użytkownik już jest w tej grze
    if (in_array($user_id, $existing_users)) {
        error_log("Błąd: Użytkownik $user_id już jest w grze $rozgrywka_id");
        echo json_encode(['success' => false, 'message' => 'Użytkownik już jest w tej grze']);
        exit;
    }

    // Sprawdzanie, czy gra jest pełna
    if ($player_count >= 2) {
        error_log("Błąd: Gra $rozgrywka_id jest pełna (player_count: $player_count)");
        echo json_encode(['success' => false, 'message' => 'Gra jest pełna']);
        exit;
    }

    // Dodawanie użytkownika do sesji graczy
    $query = "INSERT INTO sesje_graczy (rozgrywka_id, user_id) VALUES (?, ?)";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, 'ii', $rozgrywka_id, $user_id);
    $success = mysqli_stmt_execute($stmt);
    mysqli_stmt_close($stmt);

    if ($success) {
        // Aktualizacja statusu gry na 'active', jeśli dołączył drugi gracz
        $query = "UPDATE rozgrywka SET status = 'active' WHERE id = ? AND (SELECT COUNT(*) FROM sesje_graczy WHERE rozgrywka_id = ?) = 2";
        $stmt = mysqli_prepare($conn, $query);
        mysqli_stmt_bind_param($stmt, 'ii', $rozgrywka_id, $rozgrywka_id);
        $success = mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        if ($success) {
            error_log("Użytkownik $user_id dołączył do gry $rozgrywka_id, status: active");
            echo json_encode(['success' => true, 'rozgrywka_id' => $rozgrywka_id, 'status' => 'active']);
        } else {
            error_log("Użytkownik $user_id dołączył do gry $rozgrywka_id, status: waiting");
            echo json_encode(['success' => true, 'rozgrywka_id' => $rozgrywka_id, 'status' => 'waiting']);
        }
    } else {
        error_log("Błąd: Nie udało się dodać użytkownika $user_id do sesji gry $rozgrywka_id");
        echo json_encode(['success' => false, 'message' => 'Błąd dodawania gracza do sesji']);
    }
} else {
    // Tworzenie nowej gry
    $query = "INSERT INTO rozgrywka (tryb_id, kategoria_id, status, max_players) VALUES (?, ?, 'waiting', 2)";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, 'ii', $tryb_id, $category_id);
    $success = mysqli_stmt_execute($stmt);
    $rozgrywka_id = mysqli_insert_id($conn);
    mysqli_stmt_close($stmt);

    if ($success) {
        // Dodawanie pierwszego gracza do sesji
        $query = "INSERT INTO sesje_graczy (rozgrywka_id, user_id) VALUES (?, ?)";
        $stmt = mysqli_prepare($conn, $query);
        mysqli_stmt_bind_param($stmt, 'ii', $rozgrywka_id, $user_id);
        $success = mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        if ($success) {
            error_log("Utworzono nową grę $rozgrywka_id dla użytkownika $user_id, status: waiting");
            echo json_encode(['success' => true, 'rozgrywka_id' => $rozgrywka_id, 'status' => 'waiting']);
        } else {
            error_log("Błąd: Nie udało się dodać użytkownika $user_id do nowej gry $rozgrywka_id");
            echo json_encode(['success' => false, 'message' => 'Błąd dodawania gracza do sesji']);
        }
    } else {
        error_log("Błąd: Nie udało się utworzyć nowej gry dla kategorii $category_id");
        echo json_encode(['success' => false, 'message' => 'Błąd tworzenia gry']);
    }
}

mysqli_close($conn);
?>
