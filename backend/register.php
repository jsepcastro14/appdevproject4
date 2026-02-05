<?php
require_once 'db_config.php';

// These must match the params.put() keys in RegisterActivity.java exactly
$firstName = $_POST['firstName'];
$lastName  = $_POST['lastName'];
$email     = $_POST['email'];
$password  = $_POST['password'];
$mobileNo  = $_POST['mobileNo'];

// Use the exact table name from your database (tbluser)
$sql = "INSERT INTO tbluser (firstName, lastName, email, password, mobileNo) 
        VALUES ('$firstName', '$lastName', '$email', '$password', '$mobileNo')";

if($conn->query($sql) === TRUE) {
    echo "success";
} else {
    // This helps you see the actual SQL error in the Android Toast
    echo "error: " . $conn->error;
}
$conn->close();
?>