var express = require('express');
var fs = require('fs');
var request = require('request')
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.sendFile('index.html', { root:  'public' });
});

router.get('/getcity',function(req,res,next) {
  fs.readFile(__dirname + '/cities.dat.txt',function(err,data) {
    if(err) throw err;
    var cities = data.toString().split("\n");
    var myRe = new RegExp("^" + req.query.q);
    var jsonresult = [];
    for(var i = 0; i < cities.length; i++) {
      var result = cities[i].search(myRe);
      if(result != -1) {
        jsonresult.push({city: cities[i]})
      }
    }
    res.status(200).json(jsonresult);
  });
});

var owlbot = "https://owlbot.info/api/v1/dictionary/";
router.get("/owlbot/:word", function(req, res) {
  let word = req.params.word;
  let url = owlbot + word;
  request(url).pipe(res);
});

module.exports = router;
