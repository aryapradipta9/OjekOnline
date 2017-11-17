<?php
    function fileUpload($fileinfo, $elmtname, $username){
        $savedir = "profpic/";
        $file_name = md5($username);
        $file_ext = pathinfo($fileinfo[$elmtname]["name"],PATHINFO_EXTENSION);
        $target_file = $savedir . $file_name . "." . $file_ext;
        $uploadOk = 1;

        if(isset($_POST["submit"])){
            //Check image
            $check = getimagesize($fileinfo[$elmtname]["tmp_name"]);
            if($check === false){
                $uploadOk = 1;
            }

            if($uploadOk == 1){
                if(move_uploaded_file($_FILES[$elmtname]["tmp_name"],$target_file)){
                    return $target_file;
                }
                else{
                    return null;
                }
            }
        }
    }

?>