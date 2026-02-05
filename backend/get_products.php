<?php
// Suppress warnings from breaking JSON and set the correct header
error_reporting(0);
header('Content-Type: application/json');

require_once 'db_config.php';

// Check connection
if ($conn->connect_error) {
    // Instead of a PHP error, send a clean JSON error
    echo json_encode(['error' => 'Database connection failed: ' . $conn->connect_error]);
    exit();
}

$sql = "SELECT product_id, user_id, productName, category, Quantity, price FROM tblproduct";
$result = $conn->query($sql);
$products = array();

// Check if the query itself failed
if (!$result) {
    echo json_encode(['error' => 'SQL query failed: ' . $conn->error]);
    exit();
}

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $products[] = $row;
    }
}

// Always output a valid JSON array, even if it's empty
echo json_encode($products);

$conn->close();
?>