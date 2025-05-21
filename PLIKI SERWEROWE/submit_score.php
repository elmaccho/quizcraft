<?php
require_once 'db_connection.php';

header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);
$user_id = isset($data['user_id']) ? (int)$data['user_id'] : null;
$rozgrywka_id = isset($data['rozgrywka_id']) ? (int)$data['rozgrywka_id'] : null;
$score = isset($data['score']) ? (int)$data['score'] : null;

if (!$user_id || !$rozgrywka_id || $score === null) {
    echo json_encode(['success' => false, 'message' => 'Brak user_id, rozgrywka_id lub score']);
    exit;
}

// Dodanie kolumny score, jeśli nie istnieje
$query = "ALTER TABLE sesje_graczy ADD COLUMN IF NOT EXISTS score INT DEFAULT 0";
mysqli_query($conn, $query);

// Zapis wyniku gracza
$query = "UPDATE sesje_graczy SET score = ? WHERE rozgrywka_id = ? AND user_id = ?";
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, 'iii', $score, $rozgrywka_id, $user_id);
$success = mysqli_stmt_execute($stmt);
mysqli_stmt_close($stmt);

if (!$success) {
    echo json_encode(['success' => false, 'message' => 'Błąd aktualizacji wyniku']);
    exit;
}

// Sprawdzanie, czy obaj gracze przesłali wyniki
$query = "SELECT user_id, score FROM sesje_graczy WHERE rozgrywka_id = ?";
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, 'i', $rozgrywka_id);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$players = [];
while ($row = mysqli_fetch_assoc($result)) {
    $players[] = $row;
}
mysqli_stmt_close($stmt);

if (count($players) == 2) {
    // Określanie zwycięzcy
    $winner_id = null;
    if ($players[0]['score'] > $players[1]['score']) {
        $winner_id = $players[0]['user_id'];
    } elseif ($players[1]['score'] > $players[0]['score']) {
        $winner_id = $players[1]['user_id'];
    }

    // Aktualizacja statusu gry i zwycięzcy
    $query = "UPDATE rozgrywka SET status = 'finished', winner_id = ? WHERE id = ?";
    $stmt = mysqli_prepare($conn, $query);
    mysqli_stmt_bind_param($stmt, 'ii', $winner_id, $rozgrywka_id);
    $success = mysqli_stmt_execute($stmt);
    mysqli_stmt_close($stmt);

    if ($success) {
        echo json_encode([
            'success' => true,
            'message' => 'Gra zakończona',
            'winner_id' => $winner_id,
            'scores' => [
                ['user_id' => $players[0]['user_id'], 'score' => $players[0]['score']],
                ['user_id' => $players[1]['user_id'], 'score' => $players[1]['score']]
            ]
        ]);
    } else {
        echo json_encode(['success' => false, 'message' => 'Błąd aktualizacji statusu gry']);
    }
} else {
    echo json_encode(['success' => true, 'message' => 'Wynik zapisany, oczekuj na przeciwnika']);
}

mysqli_close($conn);
?>
