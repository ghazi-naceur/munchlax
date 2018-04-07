var express = require('express')
  ,cors = require('cors')
  , app = express();

//app.user(bodyParser.json());
// after the code that uses bodyParser and other cool stuff
var originsWhitelist = [
  'http://localhost:4200','http://localhost:8080'
];
var corsOptions = {
  origin: function(origin, callback){
        var isWhitelisted = originsWhitelist.indexOf(origin) !== -1;
        callback(null, isWhitelisted);
  },
  credentials:true
}
//here is the magic
app.use(cors());

// var cors = require('cors');



// var app = express();
// app.use(cors()); // Encore pour activer CORS !!!
