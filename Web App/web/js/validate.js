function checkAvailability(value, attribute){
    var ajaxreq = new XMLHttpRequest();
    ajaxreq.onreadystatechange = function(){
        if(ajaxreq.readyState == 4 && ajaxreq.status == 200){
            var element = document.getElementById(attribute+"check");
            var response = JSON.parse(ajaxreq.responseText);

            if(response["foundID"] == false){
                element.innerHTML = "&#10004;";
                element.value = "yes";
            }
            else{
                element.innerHTML = "&#10060;";
                element.value = "no";
            }
        }
    };
    ajaxreq.open("POST", "usercheck.php");

    ajaxreq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    var query = "value="+encodeURIComponent(value)+"&"+"attribute="+encodeURIComponent(attribute);
    ajaxreq.send(query);
}

function validateRegister(formname){
    var name = document.forms[formname]["name"].value;
    var username = document.forms[formname]["username"].value;
    var password = document.forms[formname]["password"].value;
    var confirm_password = document.forms[formname]["confirm_password"].value;
    var phone = document.forms[formname]["phone"].value;
    var email = document.forms[formname]["email"].value;

    var email_regex = /^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,64}$/i;
    var name_regex = /^[A-Za-z\s]{0,20}$/;
    var phone_regex = /^[0-9]{9,12}$/;

    var emailvalid = email !== "" && email.match(email_regex) !== null;
    var namevalid = name !== "" && name.match(name_regex) !== null;
    var phonevalid = phone !== "" && phone.match(phone_regex) !== null;
    var passwordvalid = confirm_password !== "" && password !== "" && password === confirm_password;
    var usernamevalid = username !== "";


    if(emailvalid && namevalid && phonevalid && passwordvalid && usernamevalid){
        return true;
    }
    else{
        alert("Cek input lagi ya~");
        return false;
    }
}

function validateUpdateprofile(formname){
    var name = document.forms[formname]["name"].value;
    var phone = document.forms[formname]["phone"].value;

    var email_regex = /^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,64}$/i;
    var name_regex = /^[A-Za-z\s]{0,20}$/;
    var phone_regex = /^[0-9]{9,12}$/;

    var namevalid = name.match(name_regex).length !== null;
    var phonevalid = phone.match(phone_regex).length !== null;

    if(namevalid && phonevalid){
        return true;
    }
    else{
        alert("Cek input lagi ya~");
        return false;
    }
}

function validateOrder(formname){
    var origin = document.forms[formname]["origin"].value;
    var destination = document.forms[formname]["destination"].value;

    if (origin!="" && destination!=""){
        return true;
    }
    else{
        alert("Origin/Destination tidak boleh kosong")
        return false;
    }
}