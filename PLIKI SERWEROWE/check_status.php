<?php

require_once("db_connection.php")

$user_id = $_POST['user_id'] ?? null;
$rozgrywka_id = $_POST['rozgrywka_id'] ?? null;

if (!$user_id || !$rozgrywka_id) {
    echo json_encode(['success' => false, 'message' => 'Brak wymaganych parametrów']);
    exit;
}

// Zapytanie, które sprawdza status rozgrywki
$query = $pdo->prepare("SELECT status FROM rozgrywki WHERE id = ?");
$query->execute([$rozgrywka_id]);
$status = $query->fetchColumn();

if ($status) {
    echo json_encode(['success' => true, 'status' => $status, 'rozgrywka_id' => $rozgrywka_id]);
} else {
    echo json_encode(['success' => false, 'message' => 'Nie znaleziono rozgrywki']);
}
?>
