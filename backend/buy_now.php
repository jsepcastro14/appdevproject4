<?php
require_once 'db_config.php';
error_reporting(0);

$user_id     = $_POST['user_id'];
$product_id  = $_POST['product_id'];
$productName = $_POST['productName'];
$category    = $_POST['category'];
$Quantity    = $_POST['Quantity'];
$price       = $_POST['price'];
$address     = $_POST['address'];

// Start a transaction for safety
$conn->begin_transaction();

try {
    // 1. Log the purchase in order history
    $sql_history = "INSERT INTO tblorderhistory (user_id, product_id, productName, category, Quantity, price, address) 
                    VALUES ('$user_id', '$product_id', '$productName', '$category', '$Quantity', '$price', '$address')";
    $conn->query($sql_history);

    // 2. Add/Update the item in the user's personal inventory
    $sql_inventory = "INSERT INTO tblinventory (product_id, user_id, productName, category, address, Quantity, price)
                      VALUES ('$product_id', '$user_id', '$productName', '$category', '$address', '$Quantity', '$price')
                      ON DUPLICATE KEY UPDATE Quantity = tblinventory.Quantity + VALUES(Quantity)";
    $conn->query($sql_inventory);

    // If both steps succeeded, save the changes
    $conn->commit();
    echo "success";

} catch (mysqli_sql_exception $exception) {
    // If any step failed, undo all changes
    $conn->rollback();
    echo "error: " . $exception->getMessage();
}

$conn->close();
?>