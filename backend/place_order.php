<?php
require_once 'db_config.php';

$user_id = $_POST['user_id'];
$address = $_POST['address'];

// 1. Copy to history
$sql1 = "INSERT INTO tblorderhistory (product_id, user_id, productName, category, Quantity, price, address)
         SELECT product_id, user_id, productName, category, Quantity, price, '$address' 
         FROM tbladdtocart WHERE user_id = '$user_id'";

if ($conn->query($sql1) === TRUE) {
    // 2. Add to Inventory (Container of bought items)
    $sql2 = "INSERT INTO tblinventory (product_id, user_id, productName, category, address, Quantity, price)
             SELECT product_id, user_id, productName, category, '$address', Quantity, price 
             FROM tbladdtocart WHERE user_id = '$user_id'";
    $conn->query($sql2);

    // 3. Clear cart
    $sql3 = "DELETE FROM tbladdtocart WHERE user_id = '$user_id'";
    $conn->query($sql3);
    
    echo "success";
} else {
    echo "error: " . $conn->error;
}
$conn->close();
?>