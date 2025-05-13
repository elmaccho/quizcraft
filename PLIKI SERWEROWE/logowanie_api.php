<?php
header("Content-Type: application/json");
require_once("db_connection.php");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Logowanie danych wejściowych
    file_put_contents('debug.log', "POST Data: " . print_r($_POST, true) . "\n", FILE_APPEND);

    // Pobieranie danych z FormUrlEncoded
    $email = filter_var(trim($_POST['email'] ?? ''), FILTER_VALIDATE_EMAIL);
    $password = $_POST['password'] ?? '';

    if (!$email) {
        file_put_contents('debug.log', "Invalid email: " . ($_POST['email'] ?? 'empty') . "\n", FILE_APPEND);
        echo json_encode(["success" => false, "message" => "Nieprawidłowy email."]);
        exit;
    }

    if (empty($password)) {
        echo json_encode(["success" => false, "message" => "Hasło nie może być puste."]);
        exit;
    }

    // Zapytanie do bazy danych
    $sql = "SELECT id, username, name, email, password, photo FROM users WHERE email = ?";
    $stmt = $conn->prepare($sql);

    if ($stmt === false) {
        echo json_encode(["success" => false, "message" => "Błąd zapytania: " . $conn->error]);
        exit;
    }

    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc();

        // Weryfikacja hasła
        if (password_verify($password, $user['password'])) {
            echo json_encode([
                "success" => true,
                "message" => "Logowanie udane.",
                "user" => [
                    "id" => $user['id'],
                    "username" => $user['username'],
                    "name" => $user['name'],
                    "email" => $user['email'],
                    "photo" => $user['photo']
                ]
            ]);
        } else {
            echo json_encode(["success" => false, "message" => "Nieprawidłowe hasło."]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Użytkownik o podanym emailu nie istnieje."]);
    }

    $stmt->close();
    $conn->close();
} else {
    echo json_encode(["success" => false, "message" => "Błąd serwera. Użyj metody POST."]);
}
?>
