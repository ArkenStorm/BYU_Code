Vue.component('singleGame', {
    template: `
    <div>
        <p class="game-title text-blue-darker text-center">{{ gameTitle }}</p>
        <img class="game-image" :src="gameImage" @mouseover="changeToGif" @mouseleave="changeToStatic">
    </div>
    `,
    props: {
        gameObject: {
            type: Object,
            required: true
        }
    },
    data: function() {
        return {
            isHovering: false
        };
    },
    computed: {
        gameImage: function() {
            return this.isHovering ? this.gameObject.gameGifImage : this.gameObject.gameStaticImage
        },
        gameTitle: function() {
            return this.gameObject.gameName
        }
    },
    methods: {
        changeToGif: function() {
            this.isHovering = true
        },
        changeToStatic: function() {
            this.isHovering = false
        }
    }
})

/*

*/