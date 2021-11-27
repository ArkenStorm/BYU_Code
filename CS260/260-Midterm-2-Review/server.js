const express = require('express');
const bodyParser = require("body-parser");

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: false
}));

app.use(express.static('public'));

const mongoose = require('mongoose');

// connect to the database
mongoose.connect('mongodb://localhost:27017/candidates', {
  useNewUrlParser: true
});

const candidateSchema = new mongoose.Schema({
  name: String,
  votes: Number
});

const Candidate = mongoose.model('Candidate', candidateSchema);

app.post('/api/candidates', async (req, res) => {
  const candidate = new Candidate({
    name: req.body.name,
    votes: req.body.votes
  });
  try {
    await candidate.save();
    res.send(candidate);
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
})

app.get('/api/items', async (req, res) => {
  try {
    let candidates = await Candidate.find();
    res.send(candidates);
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
});

app.delete('/api/candidates/:id', async (req, res) => {
  try {
    let id = req.params.id;
    await Candidate.deleteOne({"_id": id});
    res.sendStatus(200);
  }
  catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
})

app.put('/api/candidates/:id', async (req, res) => {
  try {
    let id = req.params.id;
    let candidate = await Candidate.findOne({"_id": id});
    candidate.name = req.body.name;
    candidate.votes = req.body.votes;
    candidate.save();
    res.sendStatus(200);
  }
  catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
})


app.listen(4205, () => console.log('Server listening on port 4205!'));