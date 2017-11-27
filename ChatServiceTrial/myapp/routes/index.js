var admin = require("firebase-admin");

var serviceAccount = require("../tubes-3-wbd-ojek-online-firebase-adminsdk-34z7r-6295bb28f0.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://tubes-3-wbd-ojek-online.firebaseio.com/"
});

var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
 res.render('index', { title: 'La La Land' });
});

router.post('/', function(req, res) {
  console.log(req.body);
  res.send(req.body);
 });
module.exports = router;

// This registration token comes from the client FCM SDKs.
var registrationToken = "f0IlcpJhW9k:APA91bG0tFjvVzpQFqqA-SHjp2UO0Ste2MEVxZHgt_MXJ095_dVtGdgcMqCTuJjFQC9aTKthTcaMtXx1ys9odzfNf9CPyPy6DPnUIFwp9PQ3prqU2T7-WN5kf9dzg-4Rc4TYIfAcswLp";

// See the "Defining the message payload" section below for details
// on how to define a message payload.
var payload = {
  data: {
    message: "This is test message"
  }
};

// Send a message to the device corresponding to the provided
// registration token.
admin.messaging().sendToDevice(registrationToken, payload)
  .then(function(response) {
    // See the MessagingDevicesResponse reference documentation for
    // the contents of response.
    console.log("Successfully sent message:", response);
  })
  .catch(function(error) {
    console.log("Error sending message:", error);
  });


