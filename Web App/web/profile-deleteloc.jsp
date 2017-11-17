<!--<?php
require_once("include/SQLConnection.php");

if ($_SERVER['REQUEST_METHOD'] == "POST") {
    $newlocation = $_POST["newlocation"];
    $id = $_POST["id"];
    $location = $_POST["oldlocation"];

    $new = new SQLConnection();
    $new->runQuery("DELETE FROM preferred_loc where id=$id and place='$location'");

    header("location: profile-editlocations.php?id=" . $id);
}
?>-->