<?php
require_once("SQLConnection.php");

/**
 * Mengambil user dengan ID $id dari dalam database
 * @param $id int ID User
 * @return array|boolean associative array berisi data user, false jika tidak ditemukan
 */
function getUserbyId($id)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from user where id = '$id'");

    if ($result->num_rows == 1) {
        return $result->fetch_assoc();
    } else {
        return false;
    }
}

/**
 * Mengambil user dengan username dari dalam database
 * @param $id int ID User
 * @return int|boolean id user, false jika tidak ditemukan
 */
function getUserIdbyUsername($username)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from user where username = '$username'");

    if ($result->num_rows == 1) {
        return $result->fetch_assoc()["id"];
    } else {
        return false;
    }
}

/**
 * Mengambil user dengan username dari dalam database
 * @param $id int ID User
 * @return int|boolean id user, false jika tidak ditemukan
 */
function getUserIdbyEmail($email)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from user where email = '$email'");

    if ($result->num_rows == 1) {
        return $result->fetch_assoc()["id"];
    } else {
        return false;
    }
}


/**
 * Melakukan pengecekan terhadap username dan password, mengembalikan detail akun user jika berhasil
 * @param $username username pengguna
 * @param $password password pengguna
 * @return array|bool
 */
function logIn($username, $password)
{
    //cek apakah user ada
    $id = getUserIdbyUsername($username);
    if ($id) {
        //cek apakah password benar
        $user_details = getUserbyId($id);
        if ($password == $user_details["password"]) {
            return $user_details;
        } else {
            return false;
        }
    }
    return false;
}

function getDriverbyId($id)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from driver natural join user where id = $id");

    if ($result->num_rows == 1) {
        return $result->fetch_assoc();
    } else {
        return false;
    }
}

function searchDriverbyUsername($name_query, $self_id)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from user natural join driver where (username like '%$name_query%' or name like '%$name_query%') and id <> $self_id and isDriver=TRUE");

    if ($result->num_rows >= 1) {
        return $result;
    } else {
        return false;
    }
}

function searchDriverbyLocation($location, $self_id)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select distinct id, name, rating, profilePic, votes from user natural join driver natural join preferred_loc where place like '%$location%' and id <> $self_id and isDriver=TRUE");

    if ($result->num_rows >= 1) {
        return $result;
    } else {
        return false;
    }
}

function getLocationbyId($id)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from preferred_loc where id = $id");

    if ($result->num_rows >= 1) {
        return $result;
    } else {
        return false;
    }
}

function getOrderHistory($id)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from `order` join driver on `order`.id_driver = driver.id join user on `order`.id_driver = user.id where id_user = $id and hidden_user = FALSE order by order_id desc ");

    return $result;
}

function getDriverHistory($id)
{
    $conn = new SQLConnection();
    $result = $conn->runQuery("select * from `order` join user on `order`.id_user = user.id  where id_driver = $id and hidden_driver = FALSE order by order_id desc");

    return $result;
}

function updateDriverRating($id)
{
    $conn = new SQLConnection();
    $count = $conn->runQuery("select count(order_id) as 'count' from `order` where id_driver=$id group by id_driver ");
    $average = $conn->runQuery("select avg(order_rating) as 'average' from `order` where id_driver=$id  group by id_driver");
    if ($count && $average) {
        $count = $count->fetch_assoc()["count"];
        $average = $average->fetch_assoc()["average"];

        $conn->runQuery("update driver set rating=$average, votes=$count where id='$id'");
    }
}