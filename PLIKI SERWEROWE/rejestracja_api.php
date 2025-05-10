<?php
header("Content-Type: application/json");
require_once("db_connection.php");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = htmlspecialchars(trim($_POST['username']));
    $name = htmlspecialchars(trim($_POST['name']));
    $email = filter_var(trim($_POST['email']), FILTER_VALIDATE_EMAIL);
    $password = $_POST['password'];
    $photo = htmlspecialchars(trim($_POST['photo']));

    if (!$email) {
        echo json_encode(["success" => false, "message" => "Nieprawidłowy email."]);
        exit;
    }

    // Sprawdzenie, czy email już istnieje w bazie
    $sql_check_email = "SELECT * FROM users WHERE email = ?";
    $stmt_check_email = $conn->prepare($sql_check_email);
    
    if ($stmt_check_email === false) {
        echo json_encode(["success" => false, "message" => "Błąd zapytania: " . $conn->error]);
        exit;
    }

    $stmt_check_email->bind_param("s", $email);
    $stmt_check_email->execute();
    $result = $stmt_check_email->get_result();

    if ($result->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Ten email jest już zarejestrowany."]);
        $stmt_check_email->close();
        exit;
    }

    // Sprawdzenie, czy username już istnieje w bazie
    $sql_check_username = "SELECT * FROM users WHERE username = ?";
    $stmt_check_username = $conn->prepare($sql_check_username);
    
    if ($stmt_check_username === false) {
        echo json_encode(["success" => false, "message" => "Błąd zapytania: " . $conn->error]);
        exit;
    }

    $stmt_check_username->bind_param("s", $username);
    $stmt_check_username->execute();
    $result = $stmt_check_username->get_result();

    if ($result->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Ten username jest już zajęty."]);
        $stmt_check_username->close();
        exit;
    }

    // Jeśli email i username nie istnieją, kontynuuj rejestrację
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    $game_lost = 0;
    $games_draw = 0;
    $day_streak = 0;
    $answers = 0;
    $correct_answers = 0;
    $now = date('Y-m-d H:i:s');

    $sql = "INSERT INTO users (username, name, email, password, photo, game_lost, games_draw, day_streak, answers, correct_answers, last_played, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    $stmt = $conn->prepare($sql);

    if ($stmt === false) {
        echo json_encode(["success" => false, "message" => "Błąd zapytania: " . $conn->error]);
        exit;
    }

    $stmt->bind_param("sssssiiiiiss", 
        $username, 
        $name, 
        $email, 
        $hashed_password, 
        $photo, 
        $game_lost, 
        $games_draw, 
        $day_streak, 
        $answers, 
        $correct_answers, 
        $now, 
        $now
    );

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Zarejestrowano pomyślnie."]);
    } else {
        echo json_encode(["success" => false, "message" => "Nie udało się zarejestrować. " . $stmt->error]);
    }

    $stmt->close();
    $stmt_check_email->close();
    $stmt_check_username->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Błąd serwera."]);
}
?>
