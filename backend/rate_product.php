<?php
require_once 'db_config.php';

$user_id     = $_POST['user_id'];
$product_id  = $_POST['product_id'];
$productName = $_POST['productName'];
$category    = $_POST['category'];
$address     = $_POST['address'];
$rate        = $_POST['rate'];

$sql = "INSERT INTO tblsuccessfulorders (product_id, user_id, productName, category, address, rate) 
        VALUES ('$product_id', '$user_id', '$productName', '$category', '$address', '$rate')";

if($conn->query($sql) === TRUE) {
    echo "success";
} else {
    echo "error: " . $conn->error;
}
$conn->close();
?>