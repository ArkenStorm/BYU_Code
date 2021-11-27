Vue.component('website', {
    template: `
    <div>    
        <navbar :selectedTab="selectedTab" @change-selected-tab="ChangeActiveTab"></navbar>
        <div v-if="selectedTab === 'home'">
            <home></home>
        </div>
        <div v-if="selectedTab === 'music'">
            <music></music>
        </div>
        <div v-if="selectedTab === 'games'">
            <games></games>
        </div>
        <div v-if="selectedTab === 'github'">
            <github></github>
        </div>
    </div>
    `,
    data: function() {
        return {
            selectedTab: "home"
        };
    },
    methods: {
        ChangeActiveTab: function(selectedTab){
            this.selectedTab = selectedTab
        }
    }
})

var website = new Vue({
    el: '#website'
})