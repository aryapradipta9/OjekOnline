var express = require('express');
var router = express.Router();
var driveronline = require('./driveronline');

var mapping = [];

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

router.get('/:user/:driver', function(req,res){
    driveronline.online.splice(driveronline.online.findIndex(e => e === req.params.driver),1);
    mapping.push({"cust" : req.params.user, "driver" : req.params.driver});
    res.send(JSON.stringify({"driver" : req.params.driver}));
});

module.exports.route = router;
module.exports.map = mapping;