Vue.component('music', {
    template: `
    <div>
        <div>
            <span class="album-selection">
                <span v-for="(album, index) in albums" :key="album.albumID" @click="updateAlbum(index)" @click="resetSong">
                    <div class="list-element" :class="{activeSelection: selectedAlbum == album.albumID}">
                        <img class="list-thumbnail" :src="album.albumThumbnail">
                        <p class="list-title">{{ album.albumTitle }}</p>
                    </div>
                </span>
            </span>
        </div>

        <album :albumCover="cover"
        :albumThumbnail="thumbnail"
        :albumTitle="title"
        :songList="albumList"
        :audioLinks="songLinks"
        :selectedSong="songSelect"></album>
    </div>
    `,
    data: function() {
        return {
            selectedAlbum: 0,
            selectedSong: 0,
            albums: [
                {
                    albumID: 0,
                    albumCover: "https://s3-us-west-2.amazonaws.com/en-samurai-gamers-images/wp-content/uploads/2017/08/03232734/Pentakill-II-Album-Art.jpg",
                    albumThumbnail: "https://pbs.twimg.com/media/DGKx8SYXsAAmQQH.jpg",
                    albumTitle: "Pentakill II: Grasp of the Undying",
                    albumList: ['Cull', 'Mortal Reminder', 'Tear of the Goddess', 'Infinity Edge', 'Dead Man\'s Plate', 'The Hex Core Mk-2', 'The Bloodthirster', 'Frozen Heart', 'Rapid Firecannon', 'Blade of the Ruined King'],
                    audioLinks: ["https://www.youtube.com/embed/sOhFHWk4oXg", "https://www.youtube.com/embed/1UeMVfHN2P8", "https://www.youtube.com/embed/btSlfXILTkU", "https://www.youtube.com/embed/lTLqmPoJbJc", "https://www.youtube.com/embed/9ASxLLTu1ZY", 
                    "https://www.youtube.com/embed/_FlbUukLJos", "https://www.youtube.com/embed/KBq_KtuSIF0", "https://www.youtube.com/embed/QJRLhRhcfgs", "https://www.youtube.com/embed/VO1aUkF5Kbc", "https://www.youtube.com/embed/gUEKZpjPYNU"]
                },
                {
                    albumID: 1,
                    albumCover: "https://upload.wikimedia.org/wikipedia/en/thumb/6/6a/Sabaton_-_The_Last_Stand_cover.jpg/220px-Sabaton_-_The_Last_Stand_cover.jpg",
                    albumThumbnail: "https://www.nuclearblast.de/static/articles/247/247815.jpg/1000x1000.jpg",
                    albumTitle: "Sabaton: The Last Stand",
                    albumList: ['Sparta', 'Last Dying Breath', 'Blood of Bannockburn', 'Diary of an Unknown Soldier', 'The Lost Battalion', 'Rorke\'s Drift', 'The Last Stand', 'Hill 3234', 'Shiroyama', 'Winged Hussars', 'The Last Battle', 'Camouflage', 'All Guns Blazing'],
                    audioLinks: ["https://www.youtube.com/embed/p1SlBlB5pzU", "https://www.youtube.com/embed/mbmxn11u19Y", "https://www.youtube.com/embed/Oi7xBe5-M8k", "https://www.youtube.com/embed/Jhkz5NdM6ZA", "https://www.youtube.com/embed/7jTgkTEDDog", "https://www.youtube.com/embed/kVW5rjA5O7U", 
                    "https://www.youtube.com/embed/CB3H05OhVDI", "https://www.youtube.com/embed/3qMT20Z2NEU", "https://www.youtube.com/embed/Ylyqoxh-cXk", "https://www.youtube.com/embed/rcYhYO02f98", "https://www.youtube.com/embed/whFJGCyeRzI", "https://www.youtube.com/embed/exe-qs0RDQs", "https://www.youtube.com/embed/RtDbJBxB5C4"]
                }
            ]
        };
    },
    computed: {
        cover: function() {
            return this.albums[this.selectedAlbum].albumCover
        },
        thumbnail: function() {
            return this.albums[this.selectedAlbum].albumThumbnail
        },
        title: function() {
            return this.albums[this.selectedAlbum].albumTitle
        },
        albumList: function() {
            return this.albums[this.selectedAlbum].albumList
        },
        songLinks: function() {
            return this.albums[this.selectedAlbum].audioLinks
        },
        songSelect: function() {
            return this.selectedSong
        }
    },
    methods: {
        updateAlbum: function(index) {
            this.selectedAlbum = index
        },
        resetSong: function() {
            this.selectedSong = 0
        }
    }
})