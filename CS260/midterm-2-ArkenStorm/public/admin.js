/*global axios, Vue*/
var app = new Vue({
  el: '#admin',
  data: {
    products: [],
    productName: '',
    productPrice: '',
    pictureURL: ''
  },
  methods: {
    async addProduct() {
      try {
        let response = await axios.post('/api/products', {
            name: this.productName,
            price: this.productPrice,
            imageURL: this.pictureURL,
            numOrdered: 0
        })
        this.getProducts();
      } catch (error) {
        console.log(error);
      }
    },
    async getProducts() {
      try {
        let response = await axios.get("/api/items");
        this.products = response.data;
        this.products.sort((a,b) => {
            if (a.name.toLowerCase() < b.name.toLowerCase()) {
                return -1;
            }
            else if (a.name.toLowerCase() > b.name.toLowerCase()) {
                return 1;
            }
            return 0;
        })
        return true;
      } catch (error) {
        console.log(error);
      }
    },
    async deleteProduct(product) {
      try {
        let response = axios.delete("/api/products/" + product._id);
        this.getProducts();
        return true;
      } catch (error) {
        console.log(error);
      }
    },
  },
  created: function() {
    this.getProducts();
  }
});
