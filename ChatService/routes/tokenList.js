var express = require('express');
var router = express.Router();

var tokenList = [];

router.post('/:user', function(req, res){
    var user = req.params.user;
    var token = req.body.token;
    var inp = {"user" : user, "token" : token};
    tokenList.push(inp);
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify({"status" : "ok"}));
});

router.get('/:user', function(req, res){
    var user = req.params.user;
    var token = tokenList.find(o => o.user === user);
    //var inp = {"token" : token};
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(token));
});

module.exports = router;