Vue.component('games', {
    template: `
    <div>
        <div class="all-tiles">
            <singleGame class="game-tile" v-for="(game, index) in games" :key="game.gameID" :gameObject="games[index]" :gameTitle="game.gameName"></singleGame>
        </div>
    </div>
    `,
    data: function() {
        return {
            games: [
                {
                    gameID: 0,
                    gameStaticImage: "leaguelogo.jpg",
                    gameGifImage: "https://pa1.narvii.com/6094/1631e1402320e35a245f993979b34df2de32b636_hq.gif",
                    gameName: "League of Legends"
                },
                {
                    gameID: 1,
                    gameStaticImage: "https://images-na.ssl-images-amazon.com/images/I/41LG2WCr9QL.jpg",
                    gameGifImage: "archonfusion.webp",
                    gameName: "StarCraft II"
                },
                {
                    gameID: 2,
                    gameStaticImage: "https://www.smashbros.com/assets_v2/img/movie/20180613_1.jpg",
                    gameGifImage: "smashult.webp",
                    gameName: "Super Smash Bros. Ultimate"
                },
                {
                    gameID: 3,
                    gameStaticImage: "https://steamcdn-a.akamaihd.net/steam/apps/252950/header_alt_assets_5.jpg?t=1549059561",
                    gameGifImage: "goooooooooooooooooal.webp",
                    gameName: "Rocket League"
                }
            ]
        };
    },
    computed: {
        
    },
    methods: {
        
    }
})