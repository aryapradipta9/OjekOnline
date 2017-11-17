function getConfirmation() {
   var retVal = confirm("Apa anda yakin ingin menghapus lokasi?");
   if( retVal == true ){
	  
	  return true;
   }
   else{
	  return false;
   }
}