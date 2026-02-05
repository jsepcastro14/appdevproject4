<?php
require_once 'db_config.php';

$user_id = $_GET['user_id'];

$sql = "SELECT * FROM tblAddtoCart WHERE user_id = '$user_id'";
$result = $conn->query($sql);
$cartItems = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $cartItems[] = $row;
    }
}

echo json_encode($cartItems);
$conn->close();
?>