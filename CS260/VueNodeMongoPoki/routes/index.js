var express = require('express');
var router = express.Router();
var request = require('request');

/* GET home page. */
router.get('/', function(req, res) {
  res.sendFile('index.html', { root: 'public' });
});

router.get('/pokemon', function(req, res) {
  collection.find().toArray(function(err, result) {
    if (err) { console.log(err); }
    else if (result.length) {
      res.send(result);
    }
    else {
      console.log("No Documents found");
    }
  });
});

router.post('/pokemon', function(req, res) {
  collection.insertOne(req.body, function(err, result) {
    if (err) {
      console.log(err);
    }
    else {
      console.log('Inserted document into the "pokemon" collection.');
      res.end('{"success" : "Updated Successfully", "status" : 200}');
    }
  });
});

var mongodb = require('mongodb');
var MongoClient = mongodb.MongoClient;
var dbUrl = 'mongodb://localhost:27017/';
var collection;
MongoClient.connect(dbUrl, { useNewUrlParser: true }, function(err, client) {
  if (err) {
    console.log('Unable to connect to the mongoDB server. Error:', err);
  }
  else {
    console.log('Connection established to', dbUrl);
    
    var db = client.db('pokemon'); //Connect to the database pokemon
    db.createCollection('poke', function(err, result) { // If it exists, it will just connect to it
      collection = result;
      // Only do the insert if the collection is empty
      collection.stats(function(err, stats) {
        if (err) { console.log(err) }
        if (stats.count == 0) { // If we havent inserted before, put the default in
          collection.insertMany(pokemon, function(err, result) {
            if (err) { console.log(err) }
            else {
              console.log('Inserted documents into the "poke" collection. The documents inserted with "_id" are:', result.length, result);
            }
          });
        }
      });
    }); // Get the collection
  }
});


module.exports = router;

/**
 * This array of pokemon will represent a piece of data in our 'database'
 */
var pokemon = [
  {
    name: 'Pikachu',
    avatarUrl: 'http://rs795.pbsrc.com/albums/yy232/PixKaruumi/Pokemon%20Pixels/Pikachu_Icon__free__by_Aminako.gif~c200'
  },
  {
    name: 'Charmander',
    avatarUrl: 'http://24.media.tumblr.com/tumblr_ma0tijLFPg1rfjowdo1_500.gif'

  },
  {
    name: 'Mew',
    avatarUrl: 'http://media3.giphy.com/media/J5JrPT8r1xGda/giphy.gif'
  },
  {
    name: 'Cubone',
    avatarUrl: 'http://rs1169.pbsrc.com/albums/r511/nthndo/tumblr_ljsx6dPMNm1qii50go1_400.gif~c200'
  },
  {
    name: 'Cleffa',
    avatarUrl: 'http://media1.giphy.com/media/pTh2K2xTJ1nag/giphy.gif'
  },
  {
    name: 'Gengar',
    avatarUrl: 'https://s-media-cache-ak0.pinimg.com/originals/7e/3b/67/7e3b67c53469cc4302035be70a7f2d60.gif'
  }
];

