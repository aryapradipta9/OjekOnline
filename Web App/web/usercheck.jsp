<!-- <?php
    require_once("include/User.php");

    if($_SERVER["REQUEST_METHOD"] == "POST"){
        if($_POST["attribute"] == "username"){
            $id["foundID"] = getUserIdbyUsername($_POST["value"]);
        }
        else if ($_POST["attribute"] == "email") {
            $id["foundID"] = getUserIdbyEmail($_POST["value"]);
        }

        echo(json_encode($id));
        json_encode($id);
    }

//curl -d "attribute=username&value=use123" -X POST http://localhost/VROOOM/usercheck.php
?> -->