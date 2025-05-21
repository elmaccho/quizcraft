<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$host = 'localhost';
$dbname = 'quizcraft';
$username = 'quizuser';
$password = 'ZAQ!@WSX';

$conn = new mysqli($host, $username, $password, $dbname);

if($conn->connect_error){
	echo json_encode(['status' => 'error', 'message' => 'Blad polaczenia z baza danych: '.$conn->connect_error]);
	exit();
} else if (!$conn){
	die("Connection failed: ". mysqli_connect_error());
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);

}
?>
