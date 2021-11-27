/* global axios, Vue*/
var app = new Vue({
  el: '#app',
  data: {
    cities: [],
    prefix: '',
    owlprefix: '',
    results: []
  },
  methods: {
    fetchREST() {
      var url = "getcity?q="+ this.prefix;
      fetch(url)
        .then((data) => {
          return (data.json());
        })
        .then((citylist) => {
          this.cities = [];
          for (let i = 0; i < citylist.length; i++) {
            this.cities.push({ name: citylist[i].city });
          };
        });
    },
    async owlProxy() {
      const response = await axios.get('/owlbot/' + this.owlprefix);
      console.log(response.data);
      this.results = response.data;
    }
  },
});
