function updateProfilePic(){
    var files = document.forms["profileedit"]["profpic"].files[0];
    var reader = new FileReader();

    reader.onload = function(e){
        document.getElementsByTagName("img")[0].src= "#";
        document.getElementsByTagName("img")[0].setAttribute("style","background-image:none")
        document.getElementsByTagName("img")[0].src= e.target.result;
    };
    reader.readAsDataURL(files);
    console.log(files);
}