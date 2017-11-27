var express = require('express');
var router = express.Router();

// entri data dan ambil data ke token


var tokenList = [];

router.post('/:user/:token', function(req, res){
    var user = req.params.user;
    var token = req.params.token;
    var inp = {"username" : user, "token" : token};
    tokenList.push(inp);
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify({"status" : "ok"}));
    console.log(JSON.stringify(tokenList));
});

router.get('/:user', function(req, res){
    var user = req.params.user;
    var token = tokenList.find(o => o.user === user);
    //var inp = {"token" : token};
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(tokenList));
});

module.exports.route = router;
module.exports.tokenList = tokenList;