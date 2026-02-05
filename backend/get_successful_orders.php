<?php
require_once 'db_config.php';
error_reporting(0);
header('Content-Type: application/json');

// You can filter by user_id if you want the farmer to only see THEIR successful sales
$sql = "SELECT * FROM tblsuccessfulorders ORDER BY rate_id DESC";
$result = $conn->query($sql);
$sales = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $sales[] = $row;
    }
}
echo json_encode($sales);
$conn->close();
?>