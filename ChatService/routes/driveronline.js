var express = require('express');
var router = express.Router();
var async = require('async');

var online = [];
var _flagCheck;

/* GET add driver status and prefloc */
// kasi status onlen / finding order
/* MUST USE X-WWW-FORM-URLENCODED */
router.post('/:user', function(req, res) {
    console.log(req.body.location);
    var location = req.body.location;
    var mongoClient = require('mongodb').MongoClient;
    var url = "mongodb://localhost:27017/chat";
    res.setHeader('Content-Type', 'application/json');
    var user = req.params.user;
    // cek di online list, apakah udah ada di list
    var matchUsername = online.filter(function(value){ return value.user == req.params.user;});
    if (matchUsername.length > 0) {
        // delete
        matchUsername.splice(matchUsername.findIndex(e => e.user === req.params.user),1);
    }
    // push ke online
    var curOnline = {};
    curOnline.user = user;
    curOnline.location = location;
    online.push(curOnline);
    console.log(JSON.stringify(online));
    _flagCheck = setInterval(function() {
        if (online.filter(function(value){ return value.user == req.params.user;}).length == 0) {
            clearInterval(_flagCheck);
            console.log("empty");
            res.send(JSON.stringify({ "user" : req.params.user})); 
            //theCallback(); // the function to run once all flags are true
        }
    }, 100);

    // ambil request headernya

});

// ambil semua ojek yang online

router.get('/', function(req, res) {
    res.send(JSON.stringify(online));
});

router.delete('/:user', function(req, res) {
    clearInterval(_flagCheck);
    res.send("true");
})

module.exports.route = router;
module.exports.online = online;