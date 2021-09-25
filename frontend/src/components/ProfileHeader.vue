<!--This file creates the profile header for the Profile page.-->
<!--It contains the search bar for searching for users by first name.-->

<template>
  <div>
    <div class="pt-3 text-font container-fluid search-bar-positioning" id="search-container">
      <div class="row" id="search-bar-container" style="padding-top: 3px">
        <div class="input-group" id="search-inputs">
          <div class="btn-group" id="radio-buttons">
            <button id="user-radio-button" type="button" :class="`btn green-button-transparent ${this.searchType === 'User' ? 'active': ''}`" @click="changeSearchType('User')">User</button>
            <button id="business-radio-button" type="button" :class="`btn green-button-transparent ${this.searchType === 'Business' ? 'active': ''}`" @click="changeSearchType('Business')">Business</button>
          </div>
          <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)" :placeholder="placeholder">
          <button class="btn green-search-button" id="search-button" @click="searchClicked()"><i class="fas fa-search" aria-hidden="true"></i></button>
        </div>
      </div>
      <br>
    </div>

    <div v-if="searchType === 'Business'" id="business-type-combo-box-container" class="col" style="margin-bottom: 10px;">
      <label style="padding-left: 12px; padding-right: 10px;" for="business-type-combo-box">Business Type:</label>
      <select id="business-type-combo-box" name="business-type" v-model="selectedBusinessType">
        <option value="Any" id="default-option">Any</option>
        <option v-for="option in businessTypes" :id="option.businessType" :key="option.businessType" :value="option.value">
          {{ option.value }}
        </option>
      </select>
    </div>
  </div>
</template>


<script>

export default {
  name: "ProfileHeader",
  data() {
    return {
      searchType: "User",
      // Business type related variables
      selectedBusinessType: "Any",
      businessTypes: [
        { businessType: 'accommodation-and-food-services', value: 'Accommodation and Food Services' },
        { businessType: 'retail-trade', value: 'Retail Trade' },
        { businessType: 'charitable-organisation', value: 'Charitable Organisation' },
        { businessType: 'non-profit-organisation', value: 'Non Profit Organisation' }
      ],
    }
  },
  computed: {
    /**
     * Determines which placeholder text to use based on the search type.
     */
    placeholder() {
      return this.searchType === 'User' ? 'Search all users' : "Search all businesses";
    }
  },
  methods: {

    /**
     * When the enter key is pressed, the query is run with the search value and the user is routed to the search page.
     */
    enterPressed(event) {
      if (event.keyCode === 13) {
        // Enter pressed
        const inputQuery = this.$refs.searchInput.value;
        // Checks current route isn't target route
        if (this.$route.name === 'Search' && this.$route.query['type'] === this.searchType && this.$route.query['searchQuery'] === inputQuery) {
          if (this.searchType === 'Business') {
            if (this.$route.query['businessType'] === this.selectedBusinessType) {
              return
            }
          } else {
            return;
          }
        }

        if (this.searchType === 'User') {
          this.$router.push({ path: '/search', query: { type: `User`, searchQuery: `${inputQuery}`, orderBy: `fullNameASC`, page: "1"}});
          this.$emit('requestUsers', inputQuery);
        } else if (this.searchType === 'Business') {
          this.$router.push({ path: '/search', query: { type: `Business`, searchQuery: `${inputQuery}`, businessType: `${this.selectedBusinessType}`, orderBy: `nameASC`, page: "1"}});
          this.$emit('requestBusinesses', inputQuery, this.selectedBusinessType);
        }
      }
    },

    /**
     * When the search button is clicked, the query is run with the search value and the user is routed to the search page.
     */
    searchClicked() {
      const inputQuery = this.$refs.searchInput.value;
      // Checks current route isn't target route
      if (this.$route.name === 'Search' && this.$route.query['type'] === this.searchType && this.$route.query['searchQuery'] === inputQuery) {
        if (this.searchType === 'Business') {
          if (this.$route.query['businessType'] === this.selectedBusinessType) {
            return
          }
        } else {
          return;
        }
      }
      if (this.searchType === 'User') {
        this.$router.push({ path: '/search', query: { type: `User`, searchQuery: `${inputQuery}`, orderBy: `fullNameASC`, page: "1"}});
        this.$emit('requestUsers', inputQuery);
      } else if (this.searchType === 'Business') {
        this.$router.push({ path: '/search', query: { type: `Business`, searchQuery: `${inputQuery}`, businessType: `${this.selectedBusinessType}`, orderBy: `nameASC`, page: "1"}});
        this.$emit('requestBusinesses', inputQuery, this.selectedBusinessType);
      }
    },

    /**
     * Changes the search type to the received input.
     */
    changeSearchType(type) {
      this.searchType = type;
    }
  },
  mounted() {
    const type = this.$route.query.type;
    if (type) {
      this.changeSearchType(type);
    }
  }
}
</script>

<!------------------------------------------------ Search Bar Styling ------------------------------------------------->

<style scoped>

#search-bar:focus {
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}

.active {
  color: #FFFFFF;
  background-color: #1EBA8C;
  border: 1px solid #1EBA8C;
}

</style>
