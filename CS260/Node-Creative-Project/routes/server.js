var express = require('express');
var router = express.Router();

var SpotifyWebApi = require('spotify-web-api-node');
var scopes = ['user-read-private', 'user-read-email','playlist-modify-public','playlist-modify-private'];
const security = require('../auth.json');

var spotifyApi = new SpotifyWebApi({
  clientId: security.clientId,
  clientSecret: security.clientSecret
})

router.get('/', function(req, res, next) {
  res.sendFile('index.html', { root:  'public' });
});

router.get('/login', (req,res) => {
 spotifyApi.clientCredentialsGrant().then(
  function(data) {
    console.log('The access token expires in ' + data.body['expires_in']);
    console.log('The access token is ' + data.body['access_token']);
 
    // Save the access token so that it's used in future calls
    spotifyApi.setAccessToken(data.body['access_token']);
    res.send("Logged in");
  },
  function(err) {
    console.log('Something went wrong when retrieving an access token', err);
  }
);
})

let forGlory = true;

router.get('/forglory', (req, res) => {
  res.send(forGlory);
})

router.post('/giveglory', (req, res) => {
  forGlory = req.body.glory;
  res.send("you done did it correctly");
})

router.get('/hammertime', (req,res) => {
  forGlory = false;
    spotifyApi.searchTracks('hammer')
    .then(function(data) {
        res.send(data.body);
    }, function(err) {
        console.error(err);
    });
})

router.get('/gloryhammer', (req,res) => {
  forGlory = true;
    spotifyApi.searchTracks('Gloryhammer')
    .then(function(data) {
        res.send(data.body);
    }, function(err) {
        console.error(err);
    });
})

router.get("/artists/:artist", function(req, res) {
  let artist = req.params.artist;
  spotifyApi.searchArtists(artist)
  .then(function(data) {
    res.send(data.body);
  }, function(err) {
    console.error(err);
  });
});

router.get("/songs/:title", function(req, res) {
  let title = req.params.title;
  spotifyApi.searchTracks(title)
  .then(function(data) {
    res.send(data.body);
  }, function(err) {
    console.log('Something went wrong!', err);
  });
});

module.exports = router;

