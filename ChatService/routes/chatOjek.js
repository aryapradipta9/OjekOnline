var express = require('express');
var router = express.Router();
var mongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/ojek";
var admin = require("firebase-admin");
var token = require('./tokenList');
var serviceAccount = require("../new-tubes3-firebase-adminsdk-9uhub-e94215cd34.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://new-tubes3.firebaseio.com/"
});


router.post('/:user/:target', function(req, res){
    // baca request
    var message = req.body.message;
    // masukin db
    var inpData = {};
    inpData.sender = req.params.user;
    inpData.receiver = req.params.target;
    inpData.message = message;
    //var inpData = JSON.parse();
    mongoClient.connect(url, function(err,db) {
      if (err) throw err;

      if (inpData.message != 'chat-finished') { 
        db.collection("chat").insertOne(inpData,function(err,resu){
            if (err) throw err;
            console.log(inpData);
            //res.send(JSON.stringify({"status" : "ok"}));
        });
        db.close();
      } else {
      }
    });
    // FIREBASE SECTION
    var tokenList = token.tokenList;
    var user = req.params.target;
    var registrationToken = tokenList.find(o => o.username === user).token;
    var payload = {
        data: {
          "message": message
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
    
    // kirim data chat dalam json ke firebase

});

router.get('/:user/:target', function(req, res){
    var sender = req.params.user;
    var receiver = req.params.target;
    var query = {
        $or:[
          {"sender" : sender, "receiver" : receiver},
          {"sender" : receiver, "receiver" : sender}
        ]};
    mongoClient.connect(url, function(err,db) {
        if (err) throw err;
        db.collection("chat").find(query).sort({"_id" : 1}).toArray(function(err, result){
            // iterate result
            var ans = [];
            for (var i in result) {
                var inp = {};
                inp.message = result[i].message;
                
                if (result[i].sender !== sender) {
                    inp.type = "message-received";
                } else {
                    inp.type = "message-sent";
                }
                ans.push(inp);
            }
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(ans));
            db.close();
        });
    });
});
module.exports = router;