/*global axios */
/*global Vue */
var app = new Vue({
  el: '#app',
  data: {
    comments : [
      {title:'Comment 1', upvotes:5},
      {title:'Comment 2', upvotes:6},
      {title:'Comment 3', upvotes:1},
      {title:'Comment 4', upvotes:4},
      {title:'Comment 5', upvotes:3}
    ],
    newComment: ''
  },
  created: function() {
        this.getall();
  },
  methods: {
      addComment() {
        var url = "http://cloud9.arkenstorm.com:4200/comments";
        axios.post(url, {
              title: this.newComment,
              upvotes: 0
          })
          .then(response => {
              this.comments.push(response.data);
          })
          .catch(e => {
              console.log(e);
          });
        this.newComment = "";
      },
      incrementUpvotes(item){
        var url = "http://cloud9.arkenstorm.com:4200/comments/"+item._id+"/upvote";
        axios.put(url)
          .then(response => {
              item.upvotes = response.data.upvotes;
          })
          .catch(e => {
              console.log(e);
          });
      },
      async getall() {
        console.log("get all");
        var url = "http://cloud9.arkenstorm.com:4200/comments";
        try {
          let response = await axios.get(url);
          this.comments = response.data;
          return true;
        }
        catch (error) {
          console.log(error);
        }
      },
  },
  computed: {
      sortedComments() {
            return this.comments.sort((a, b) => {
                var rval = 0;
                if(a.upvotes > b.upvotes) {
                    rval = -1;
                } else if(a.upvotes < b.upvotes) {
                    rval = 1;
                }
                return(rval);
            })
        }
  }
});