function initFirebase() {
  var config = {
    apiKey: "AIzaSyBeW9Ip7oU9U640m7x-2XncWNtVwXBiV9s",
    authDomain: "tubes-3-wbd-ojek-online.firebaseapp.com",
    databaseURL: "https://tubes-3-wbd-ojek-online.firebaseio.com",
    projectId: "tubes-3-wbd-ojek-online",
    storageBucket: "tubes-3-wbd-ojek-online.appspot.com",
    messagingSenderId: "183053726880"
  };
  firebase.initializeApp(config);
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i < ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') {
          c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
          return c.substring(name.length, c.length);
      }
  }
  return "";
}

function generateToken() {
  
  token = '0';
  //const messaging = firebase.messaging();
  firebase.messaging().requestPermission()
  .then(function() {
    console.log('Notification permission granted.');
    return firebase.messaging().getToken();})
  .then(function(token) {
    console.log(token);
    usrnm = getCookie("username");
    console.log(usrnm);
    if (token) {
      $.post("http://localhost:3000/token/"+usrnm+"/"+token,  {
        
      }, function(data, status) {
        console.log('data: ' + data);
      });/*
       $.post({        
            type : 'POST',
            url : "http://localhost:3000/token/"+usrnm,
            contentType : 'application/x-www-form-urlencoded',
            dataType: 'json',
            data: JSON.stringify({"token": token}),
            success : function(data) {
                console.log('data: ' + data);
            },
            error : function(xhr, status, error) {
                console.log("error");
                console.log(xhr.error);                   
            }
        });
        */
    } else {
      console.log('No Instance ID token available. Request permission to generate one.');
    }
  })
  .catch(function(err) {
    console.log('An error occurred while retrieving token. ', err);
    //showToken('Error retrieving Instance ID token. ', err);
  });
   
  // })
  // .catch(function(err) {
  //   console.log('Unable to get permission to notify.', err);
  // });
  //return token;
  return token;
}