<?php
header("Content-Type: application/json");
error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once("db_connection.php");

$user_id = isset($_POST['user_id']) ? intval($_POST['user_id']) : 0;
$is_correct = isset($_POST['is_correct']) ? $_POST['is_correct'] : '';

if ($user_id <= 0) {
    echo json_encode(['success' => false, 'message' => 'Brak lub zły ID użytkownika']);
    exit();
}

$conn->begin_transaction();

try {
    $stmt = $conn->prepare("UPDATE users SET answers = answers + 1 WHERE id = ?");
    if (!$stmt) {
        throw new Exception("Prepare failed: " . $conn->error);
    }
    $stmt->bind_param("i", $user_id);
    if (!$stmt->execute()) {
        throw new Exception("Execute failed: " . $stmt->error);
    }
    $stmt->close();

    if ($is_correct === "true") {
        $stmt2 = $conn->prepare("UPDATE users SET correct_answers = correct_answers + 1 WHERE id = ?");
        if (!$stmt2) {
            throw new Exception("Prepare failed (correct_answers): " . $conn->error);
        }
        $stmt2->bind_param("i", $user_id);
        if (!$stmt2->execute()) {
            throw new Exception("Execute failed (correct_answers): " . $stmt2->error);
        }
        $stmt2->close();
    }

    $conn->commit();

    echo json_encode(['success' => true, 'message' => 'Updated answers successfully']);
} catch (Exception $e) {
    $conn->rollback();
    echo json_encode(['success' => false, 'message' => $e->getMessage()]);
}

$conn->close();
?>
