var express = require('express');
var router = express.Router();
var driveronline = require('./driveronline');

/* GET add driver status and prefloc */
/* MUST USE X-WWW-FORM-URLENCODED */
router.post('/', function(req, res) {
    var jsonData = req.body.data;
    
    res.send(req.body);
    var mongoClient = require('mongodb').MongoClient;
    
    var url = "mongodb://localhost:27017/chat";
    var inpData = JSON.parse(jsonData);
    mongoClient.connect(url, function(err,db) {
      if (err) throw err;
        db.collection("onlineList").insertOne(inpData,function(err,res){
            if (err) throw err;
            console.log(inpData);
        })
        db.close();
    });
});

router.get('/', function(req,res){
    var temp = driveronline.online.pop();
    res.send(JSON.stringify(temp));
});

module.exports = router;