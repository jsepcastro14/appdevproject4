<?php
require_once 'db_config.php';

$user_id     = $_POST['user_id'];
$productName = $_POST['productName'];
$category    = $_POST['category'];$Quantity    = $_POST['Quantity'];
$price       = $_POST['price'];

$sql = "INSERT INTO tblproduct (user_id, productName, category, Quantity, price) 
        VALUES ('$user_id', '$productName', '$category', '$Quantity', '$price')";

if($conn->query($sql) === TRUE) {
    echo "success";
} else {
    echo "error: " . $conn->error;
}

$conn->close();
?>