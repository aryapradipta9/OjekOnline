function deleteloc(seq) {
    var form = document.forms["location-form-"+seq];
    var retVal = confirm("Apa anda yakin ingin menghapus lokasi?");

    if (retVal === true) {
        form.action = "/deletelocation";
        form.submit();
    }
}

function editloc(seq) {
    var field = document.getElementById("location-newvalue-"+seq);
    var value = document.getElementById("location-value-"+seq);
    var editbutton = document.getElementById("location-edit-"+seq);
    var savebutton = document.getElementById("location-save-"+seq);


    field.type = "text";
    value.setAttribute("style","display:none");
    editbutton.setAttribute("style","display:none");
    savebutton.setAttribute("style","display:inline-block");
}

function saveloc(seq){
    var form = document.forms["location-form-"+seq];
    form.action = "/updatelocation";
    form.submit();
}

