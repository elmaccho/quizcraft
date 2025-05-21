<?php
header("Content-Type: application/json");
require_once("db_connection.php");

$mysqli = new mysqli($host, $username, $password, $dbname);

if ($mysqli->connect_error) {
    die(json_encode(['error' => 'Błąd połączenia z bazą danych: ' . $mysqli->connect_error]));
}

$user_id = isset($_POST['user_id']) ? intval($_POST['user_id']) : 0;

if ($user_id <= 0) {
    die(json_encode(['error' => 'Nieprawidłowe ID użytkownika']));
}

$query = "SELECT id, username, name, email, password, photo, games_won, game_lost, games_draw, day_streak, quizzes_played, answers, correct_answers, last_played, created_at FROM users WHERE id = ?";
$stmt = $mysqli->prepare($query);

if (!$stmt) {
    die(json_encode(['error' => 'Błąd przygotowania zapytania: ' . $mysqli->error]));
}

$stmt->bind_param('i', $user_id);

$stmt->execute();

$result = $stmt->get_result();
$user = $result->fetch_assoc();

header('Content-Type: application/json');
if ($user) {
    echo json_encode($user);
} else {
    echo json_encode(['error' => 'Użytkownik nie znaleziony']);
}

$stmt->close();
$mysqli->close();
?>
