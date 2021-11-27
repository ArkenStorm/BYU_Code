/*global axios, Vue*/
var app = new Vue({
  el: '#app',
  data: {
    products: [],
    cart: [],
    purchased: {}
  },
  methods: {
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
        this.products.forEach(function(product) {
            if (!this.purchased[product._id]) {
                this.purchased[product._id] = false;
            }
        })
        return true;
      } catch (error) {
        console.log(error);
      }
    },
    async purchaseProducts() {
        this.cart = [];
        let itemsToUpdate = {};
        itemsToUpdate.purchasedItems = [];
        this.products.forEach(product => {
           if (this.purchased[product._id]) {
               itemsToUpdate.purchasedItems.push(product);
               this.cart.push(product);
           }
        });
        await axios.put('/api/products', itemsToUpdate);
        Object.keys(this.purchased).forEach(key => {
          this.purchased[key] = false;
        });
    }
  },
  created: function() {
    this.getProducts();
  }
});