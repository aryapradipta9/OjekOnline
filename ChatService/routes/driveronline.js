var express = require('express');
var router = express.Router();
var async = require('async');
var driveronline = require('./addonline')

var online = [];
var _flagCheck;

/* GET add driver status and prefloc */
// kasi status onlen / finding order
/* MUST USE X-WWW-FORM-URLENCODED */
router.post('/:user', function(req, res) {
    console.log(req.body.location);
    
    var mongoClient = require('mongodb').MongoClient;
    var url = "mongodb://localhost:27017/chat";
    //res.setHeader('Content-Type', 'application/json');
    var user = req.params.user;
    // cek di online list, apakah udah ada di list
    var matchUsername = online.filter(function(value){ return value == req.params.user;});
    
    if (matchUsername.length > 0) {
        // delete
        // var tempy = matchUsername.findIndex(e => e == req.params.user);
        // console.log(tempy);
        // matchUsername.splice(tempy,1);
        // console.log(JSON.stringify(matchUsername));
    } else {
        online.push(user);
    }
    // push ke online
    var curOnline = {};
    curOnline.user = user;
    console.log(JSON.stringify(online));
    _flagCheck = setInterval(function() {
        if (online.filter(function(value){ return value == req.params.user;}).length == 0) {
            // buat mapping (?)
            var mapping = driveronline.map;
            var cont = mapping.splice(mapping.findIndex(e => e.driver === req.params.user),1);
            
<<<<<<< HEAD
            
            res.cookie('usrnmdrv',cont[0].cust);
            console.log(cont[0].cust);
            res.send(JSON.stringify({ "usrnmdrv" : cont[0].cust})); 
=======
            console.log("test : " + cont[0].cust);
            res.cookie('usrnmdrv','cont[0].cust',{domain:'localhost:8080'});
            res.send(cont[0].cust); 
>>>>>>> df9c39a3d9eb66670c77bacbd3639d4c0f3d2234
            //theCallback(); // the function to run once all flags are true
            clearInterval(_flagCheck);
        }
    }, 100);

    // ambil request headernya

});

// ambil semua ojek yang online

router.get('/', function(req, res) {
    res.send(JSON.stringify({"online" : online}));
});

router.delete('/:user', function(req, res) {
    clearInterval(_flagCheck);
    res.send("true");
})

module.exports.route = router;
module.exports.online = online;