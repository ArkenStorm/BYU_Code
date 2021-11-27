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
mongoose.connect('mongodb://localhost:27017/products', {
  useNewUrlParser: true
});

const productSchema = new mongoose.Schema({
  name: String,
  price: Number,
  imageURL: String,
  numOrdered: Number
});

const Product = mongoose.model('Product', productSchema);

app.post('/api/products', async (req, res) => {
  const product = new Product({
    name: req.body.name,
    price: req.body.price,
    imageURL: req.body.imageURL,
    numOrdered: 0
  });
  try {
    await product.save();
    res.send(product);
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
})

app.get('/api/items', async (req, res) => {
  try {
    let products = await Product.find();
    res.send(products);
  } catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
});

app.delete('/api/products/:id', async (req, res) => {
  try {
    let id = req.params.id;
    await Product.deleteOne({"_id": id});
    res.sendStatus(200);
  }
  catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
})

app.put('/api/products', async (req, res) => {
  try {
    req.body.purchasedItems.forEach(async item => {
        let id = item._id;
        let product = await Product.findOne({"_id": id});
        product.numOrdered += 1;
        product.save();
    });
    res.sendStatus(200);
  }
  catch (error) {
    console.log(error);
    res.sendStatus(500);
  }
})


app.listen(4205, () => console.log('Server listening on port 4205!'));