Vue.component('navbar', {
    template: `
    <div class="nav-bar">
        <a :class="{active: selectedTab == 'home'}" @click="ChangeSelectedTab('home')" href="#home">Home</a>
        <a :class="{active: selectedTab == 'music'}" @click="ChangeSelectedTab('music')" href="#music">Music</a>
        <a :class="{active: selectedTab == 'games'}" @click="ChangeSelectedTab('games')" href="#games">Games</a>
        <a :class="{active: selectedTab == 'github'}" @click="ChangeSelectedTab('github')" href="#github">Github</a>
    </div>
    `,
    props: {
        selectedTab: {
            type: String,
            required: true
        }
    },
    methods: {
        ChangeSelectedTab: function(selectedTab){
            this.$emit('change-selected-tab', selectedTab)
        }
    }
})