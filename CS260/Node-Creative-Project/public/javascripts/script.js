/* global axios, Vue*/
var app = new Vue({
  el: '#app',
  data: {
    forGlory: true,
    results: {},
    searchParams: '',
    trackSearch: true
  },
  methods: {
    hammerCheck() {
      if (this.forGlory) {
        this.glorify();
      }
      else {
        this.hammertime()
      }
    },
    async hammertime() {
      this.trackSearch = true;
      const response = await axios.get('/hammertime');
      console.log(response.data);
      this.results = response.data;
    },
    async glorify() {
      this.trackSearch = true;
      const response = await axios.get('/gloryhammer');
      console.log(response.data);
      this.results = response.data;
    },
    async searchArtists() {
      this.trackSearch = false;
      const response = await axios.get('/artists/' + this.searchParams);
      console.log(response.data);
      this.results = response.data;
    },
    async searchSongs() {
      this.trackSearch = true;
      const response = await axios.get('/songs/' + this.searchParams);
      console.log(response.data);
      this.results = response.data;
    },
    async login() {
        const response = await axios.get('/login');
        console.log(response.data);
    },
    async getGlory() {
      const response = await axios.get('/forglory');
      this.forGlory = response.data;
    },
    async setGlory() {
      try {
        let self = this;
        const response = await axios.post("/giveglory", {
          glory: self.forGlory
        });
      } catch (error) {
        console.log(error);
      }
    }
  },
  computed: {
    songs: function() {
      if (this.trackSearch) {
        return this.results.tracks != undefined ? this.results.tracks.items : [];
      }
    },
    artists: function() {
      if (!this.trackSearch) {
        return this.results.artists != undefined ? this.results.artists.items : [];
      }
    }
  },
  watch: {
    forGlory: function(newVal, oldVal) {
      this.setGlory();
    }
  },
  created: function() {
      this.getGlory();
      this.login();
  }
});