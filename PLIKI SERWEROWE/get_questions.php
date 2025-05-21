<?php
header("Content-Type: application/json");
require_once("db_connection.php");

// Get category ID from query parameter
$category_id = isset($_GET['category_id']) ? (int)$_GET['category_id'] : 0;

if ($category_id <= 0) {
    echo json_encode([
        'success' => false,
        'message' => 'Invalid category ID',
        'questions' => []
    ]);
    mysqli_close($conn);
    exit();
}

// Query to fetch 5 random questions with their answers
$query = "
    SELECT p.id, p.tresc, p.photo, p.kategoria_id,
           o1.id AS o1_id, o1.nazwa AS o1_nazwa, o1.poprawna AS o1_poprawna,
           o2.id AS o2_id, o2.nazwa AS o2_nazwa, o2.poprawna AS o2_poprawna,
           o3.id AS o3_id, o3.nazwa AS o3_nazwa, o3.poprawna AS o3_poprawna,
           o4.id AS o4_id, o4.nazwa AS o4_nazwa, o4.poprawna AS o4_poprawna
    FROM pytania p
    LEFT JOIN odpowiedzi o1 ON p.id = o1.pytanie_id AND o1.id = (
        SELECT id FROM odpowiedzi WHERE pytanie_id = p.id LIMIT 0,1
    )
    LEFT JOIN odpowiedzi o2 ON p.id = o2.pytanie_id AND o2.id = (
        SELECT id FROM odpowiedzi WHERE pytanie_id = p.id LIMIT 1,1
    )
    LEFT JOIN odpowiedzi o3 ON p.id = o3.pytanie_id AND o3.id = (
        SELECT id FROM odpowiedzi WHERE pytanie_id = p.id LIMIT 2,1
    )
    LEFT JOIN odpowiedzi o4 ON p.id = o4.pytanie_id AND o4.id = (
        SELECT id FROM odpowiedzi WHERE pytanie_id = p.id LIMIT 3,1
    )
    WHERE p.kategoria_id = ?
    ORDER BY RAND()
    LIMIT 5
";

$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, "i", $category_id);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

if (!$result) {
    echo json_encode([
        'success' => false,
        'message' => 'Query failed: ' . mysqli_error($conn),
        'questions' => []
    ]);
    mysqli_close($conn);
    exit();
}

$questions = [];
while ($row = mysqli_fetch_assoc($result)) {
    $question = [
        'id' => $row['id'],
        'tresc' => $row['tresc'],
        'photo' => $row['photo'],
        'kategoria_id' => $row['kategoria_id'],
        'odpowiedzi' => []
    ];

    // Add answers if they exist
    for ($i = 1; $i <= 4; $i++) {
        if ($row["o{$i}_id"]) {
            $question['odpowiedzi'][] = [
                'id' => $row["o{$i}_id"],
                'nazwa' => $row["o{$i}_nazwa"],
                'poprawna' => $row["o{$i}_poprawna"]
            ];
        }
    }

    $questions[] = $question;
}

echo json_encode([
    'success' => true,
    'message' => 'Questions fetched successfully',
    'questions' => $questions
]);

mysqli_stmt_close($stmt);
mysqli_close($conn);
?>
