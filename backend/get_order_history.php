<?php
require_once 'db_config.php';error_reporting(0);
header('Content-Type: application/json');

$user_id = $_GET['user_id'];

$sql = "SELECT * FROM tblorderhistory WHERE user_id = '$user_id' ORDER BY order_history_id DESC";
$result = $conn->query($sql);
$orders = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $orders[] = $row;
    }
}
echo json_encode($orders);
$conn->close();
?>