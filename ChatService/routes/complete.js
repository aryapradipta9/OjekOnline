var express = require('express');
var router = express.Router();
var admin = require("./chatOjek").admin;
var token = require('./tokenList');

var serviceAccount = require("../new-tubes3-firebase-adminsdk-9uhub-e94215cd34.json");

router.get('/:user/:driver', function(req, res) {
    // suruh chat driver close
    var tokenList = token.tokenList;
    var user = req.params.target;
    var registrationToken = tokenList.find(o => o.username === user).token;
    var payload = {
        data: {
          "message": "chat-finished"
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
});