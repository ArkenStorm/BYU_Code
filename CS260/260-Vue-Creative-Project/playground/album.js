Vue.component('album', {
    template: `
    <div class="selected-album">
        <img class="album-cover" :src="albumCover">
        <div class="album-details">
            <img class="album-thumbnail" :src="albumThumbnail">
            <div>
                <p class="album-title">{{ albumTitle }}</p>
                <ul class="album-song-list">
                    <li class="song-list-element" v-for="(song, index) in songList" :class="{activeSongListElement: selectedSong == index}" @click="changeSong(index)">{{ song }}</li>
                </ul>
            </div>
        </div>
        <div class="song-link">
            <iframe
            width="560"
            height="315"
            :src="audioLinks[selectedSong]"
            frameborder="0"
            allow="accelerometer;
            autoplay;
            encrypted-media;
            gyroscope;
            picture-in-picture"
            allowfullscreen></iframe>
        </div>
    </div>
    `,
    props: {
        albumTitle: {
            type: String,
            required: true
        },
        albumCover: {
            type: String,
            required: true
        },
        albumThumbnail: {
            type: String,
            required: true
        },
        songList: {
            type: Array,
            required: true
        },
        audioLinks: {
            type: Array,
            required: true
        },
        selectedSong: {
            type: Number,
            required: true
        }
    },
    methods: {
        changeSong: function(index) {
            this.selectedSong = index
        }
    }
})