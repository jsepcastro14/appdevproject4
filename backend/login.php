        <?php
        require_once 'db_config.php';
        $email = $_POST['email'];
        $password = $_POST['password'];

        $sql = "SELECT * FROM users WHERE email='$email' AND password='$password'";
        $result = $conn->query($sql);
        if($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo json_encode(["status"=>"success", "user"=>$row]);
        } else echo json_encode(["status"=>"error"]);
        $conn->close();
        ?>
        