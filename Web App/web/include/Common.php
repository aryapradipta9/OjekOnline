<?php
require_once("User.php");

function printHeader($id) {
    $userData = getUserbyId($id);
    $username = $userData["username"];

    echo '<div class="row header">
			<div class="header-logo">
				<div class="logo-text">NGEEENG!</div>
				<div class="logo-subtext">A Solution for Your Transportation</div>
			</div>
			<div class="header-user">
				<div class="header-username">Hello! <span class="username">' . $username . '</span></div>
				<div class="header-logout"><a href="logout.php">Logout!</a></div>
			</div>
		</div>';
}

function printNavbar($id, $page) {
    if ($page == 0) {
        echo '<div class="nav">
            <a href="order-selectdestination.php?id=' . $id . '"><div class="nav-item active">ORDER</div></a>
            <a href="history-orderhistory.php?id=' . $id . '"><div class="nav-item">HISTORY</div></a>
            <a href="profile.php?id=' . $id . '"><div class="nav-item">MY PROFILE</div></a></div>
            ';
    } else if ($page == 1) {
        echo '<div class="nav">
            <a href="order-selectdestination.php?id=' . $id . '"><div class="nav-item">ORDER</div></a>
            <a href="history-orderhistory.php?id=' . $id . '"><div class="nav-item active">HISTORY</div></a>
            <a href="profile.php?id=' . $id . '"><div class="nav-item">MY PROFILE</div></a></div>
            ';
    } else if ($page == 2) {
        echo '<div class="nav">
            <a href="order-selectdestination.php?id=' . $id . '"><div class="nav-item">ORDER</div></a>
            <a href="history-orderhistory.php?id=' . $id . '"><div class="nav-item">HISTORY</div></a>
            <a href="profile.php?id=' . $id . '"><div class="nav-item active">MY PROFILE</div></a></div>
            ';
    }


}

function printProfile($userdata, $type) {
    echo '<img ';
    if ($userdata["profilePic"] != "") {
        echo 'src="' . $userdata["profilePic"] . '"';
    }
    echo 'class = "'.$type.' ';
    echo ($userdata["isDriver"] == TRUE ? "driver" : "user");
    echo '" ></img > ';
}

?>
