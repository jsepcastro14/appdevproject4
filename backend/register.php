        <?php
        require_once 'db_config.php';
        $firstname = $_POST['firstname'];
        $lastname = $_POST['lastname'];
        $email = $_POST['email'];
        $password = $_POST['password'];
        $mobileNum = $_POST['mobileNum'];

        $sql = "INSERT INTO users (firstname, lastname, email, password, mobileNum) VALUES ('$firstname', '$lastname', '$email', '$password', '$mobileNum')";
        if($conn->query($sql) === TRUE) echo "success";
        else echo "error: " . $conn->error;
        $conn->close();
        ?>
        