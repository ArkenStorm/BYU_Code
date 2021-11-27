/*global Vue*/
/*global axios*/
Vue.component('github', {
    template: `
    <div class="flex flex-col items-center">
        <form @submit.prevent="getUserRepos">
            Enter Github Username: <input v-model="githubName" type="text"></input>
        </form>
        <p v-if="displayedName != ''" class="mt-2">{{ displayedName }}'s Public Repositories:</p>
        <ul class="bg-blue-darker rounded-lg text-yellow-dark" id="repoList">
            <li v-for="repo in repositories" class="mt-2 mb-2 pr-2">
                <a :href="repo.html_url">{{ repo.name }}</a>
            </li>
        </ul>
    </div>
    `,
    data: function() {
        return {
            githubName: '',
            displayedName: '',
            repositories: []
        }
    },
    methods: {
        async getUserRepos() {
            var url = 'https://api.github.com/users/' + this.githubName + '/repos';
            const response = await axios.get(url);
            this.repositories = response.data;
            this.displayedName = this.githubName;
            this.githubName = '';
        }
    }
})