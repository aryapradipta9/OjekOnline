var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var mongo = require('mongodb');
var async = require('async');
var mongoClient = require('mongodb').MongoClient;
var cookieParser = require('cookie-parser');

var url = "mongodb://localhost:27017/chat";
mongoClient.connect(url, function(err,db) {
  if (err) throw err;
	db.collection("chat2").find().toArray(function(err, result) {
		console.log(result);
	});
	db.close();
});

var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var index = require('./routes/index');
var users = require('./routes/users');
var addonline = require('./routes/addonline');
var driveronline = require('./routes/driveronline');
var chatojek = require('./routes/chatOjek');
var tokenList = require('./routes/tokenList');
var cors = require('cors');

var app = express();

async.parallel([
  function(callback) {
      setTimeout(function() {
        
          callback(null, 'one');
      }, 10000);
  },
  function(callback) {
      setTimeout(function() {
        console.log('hihi');
          callback(null, 'two');
      }, 100);
  }
],
// optional callback
function(err, results) {
  // the results array will equal ['one','two'] even though
  // the second function had a shorter timeout.
  if (err) throw err;
  console.log(results);
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(function(req, res, next) {
  res.header('Access-Control-Allow-Credentials', true);
  res.header('Access-Control-Allow-Origin', req.headers.origin);
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
  res.header('Access-Control-Allow-Headers', 'X-Requested-With, X-HTTP-Method-Override, Content-Type, Accept');
  next();
});
app.use(logger('dev'));
app.use(cors()); 
app.options('*', cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(cookieParser());

app.use('/', index);
app.use('/users', users);
app.use('/driver', addonline.route);
app.use('/addonline', driveronline.route);
app.use('/chat', chatojek);
app.use('/token', tokenList.route);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});




// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

console.log(driveronline.online[0]);

module.exports = app;
