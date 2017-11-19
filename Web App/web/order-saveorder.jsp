<!-- <?php
require_once("include/SQLConnection.php");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $origin = $_POST["origin"];
    $destination = $_POST["destination"];
    $userId = $_POST["userId"];
    $driverId = $_POST["driverId"];
    $rating = $_POST["rating"];
    $comment = $_POST["comment"];
    $date = date("l, F jS Y", time());

    var_dump($date);
    $query = "insert into `order` (date, origin, destination, order_rating, comment, id_driver, id_user) values
              ('$date', '$origin', '$destination', $rating, '$comment', $driverId, $userId)";
    $conn = new SQLConnection();
    $conn->runQuery($query);
    header("location: order-selectdestination.php?id=".$userId);
}

?> -->