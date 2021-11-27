var app = new Vue({
    el: '#app',
    data: {
        name: '',
        cmc: '',
        colors: '',
        supertypes: '',
        types: '',
        subtypes: '',
        rarity: '',
        setCode: '',
        setName: '',
        cardText: '',
        flavorText: '',
        power: '',
        toughness: '',
        json: '',
        loading: false
    },
    computed: {
        url() {
            let url = "https://api.magicthegathering.io/v1/cards?";
            url += "name=" + this.name + "&cmc=" + this.cmc + "&colors=" + this.colors + "&supertypes=" + this.supertypes + "&types=" + this.types + "&subtypes=" + this.subtypes + "&rarity=" + this.rarity;
            url += "&set=" + this.setCode + "&setName=" + this.setName + "&text=" + this.cardText + "&flavor=" + this.flavorText + "&power=" + this.power + "&toughness=" + this.toughness;
            return url;
        }
    },
    methods: {
        cardSearch() {
            this.loading = true;
            fetch(this.url)
            .then(response => {
               return response.json();
            }).then(result => {
                this.json = result;
                this.loading = false;
            });
        }
    }
})

function cardSearch() {
    
    //get the cards
    fetch(url)
    .then(response => {
       return response.json();
    }).then(json => {
        // display the cards
        console.log(json);
        
    });
}