<?php
header("Content-Type: application/json");
require_once("db_connection.php");

// Query to fetch categories
$query = "SELECT id, nazwa, photo FROM kategorie";
$result = mysqli_query($conn, $query);

if (!$result) {
    echo json_encode([
        'success' => false,
        'message' => 'Query failed: ' . mysqli_error($conn),
        'user' => null
    ]);
    mysqli_close($conn);
    exit();
}

$categories = [];
while ($row = mysqli_fetch_assoc($result)) {
    $categories[] = $row;
}

// Return JSON response
echo json_encode([
    'success' => true,
    'message' => 'Categories fetched successfully',
    'user' => null, // Adjust if user data is needed
    'categories' => $categories // Add categories as an extra field
]);

mysqli_close($conn);
?>
