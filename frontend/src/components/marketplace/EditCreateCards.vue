<template>
  <!-- Modal body -->
  <div class="modal-body">
    <form class="needs-validation" novalidate @submit.prevent>

      <!-- Modal error message -->
      <div class="row my-lg-2" v-if="modalError">
        <div class="col-12 mx-auto">
          <div class="alert alert-danger">
            {{ modalError }}
          </div>
        </div>
      </div>

      <!-- Select a section  -->
      <div class="row my-lg-2" v-if="currentModal === 'create'">
        <div class="col-md-6 my-2 my-lg-0">
          <label :for="'section-selection' + type" class="form-label">What section would you like to post your
            card?*</label>
        </div>
        <div class="col-md-6 my-2 my-lg-0">
          <select :id="'section-selection' + type" :name="'section-selection' + type"
                  :class="`form-select ${formErrorClasses.sectionSelectionError}`"
                  v-model="sectionSelected" @click="isSectionSelectedInvalid">
            <option value="" disabled selected>Select section</option>
            <option id="for-sale-option" :value="sections.FOR_SALE">For Sale</option>
            <option id="exchange-option" :value="sections.EXCHANGE">Exchange</option>
            <option id="wanted-option" :value="sections.WANTED">Wanted</option>
          </select>
          <div id="section-selection-invalid-feedback" class="invalid-feedback" v-if="formError.sectionSelectionError">
            {{ formError.sectionSelectionError }}
          </div>
        </div>
      </div>

      <div v-else>
        <label for="section-display" class="fw-bold">Section:</label>
        <p id="section-display">{{ sectionSelected }}</p>
      </div>

      <hr v-if="currentModal === 'create'">

      <!-- Creator id input -->
      <div class="row my-lg-2 my-4" v-if="user.isAdministrator(userRole) && currentModal === 'create'">

        <div class="col-md-3 ">
          <label for="card-creator" class="fw-bold">Select creator:</label>
        </div>
        <div class="col-md">
          <div id="autofill-container-users" @click="autofillClick" @keyup="keyPressedOnInput" ref="autofill-container">
            <input autocomplete="off" id="card-creator" @keydown="searchUsers()"
                   :class="`form-control ${formErrorClasses.creatorIdError}`" v-model="creatorInput" ref="card-creator">
            <div class="invalid-feedback" v-if="formError.creatorIdError">
              {{ formError.creatorIdError }}
            </div>
            <ul class="autofill-options hidden-all" id="autofill-list-users" ref="autofill-list-users">
              <li v-for="user in users" v-bind:key="user.id" tabindex="-1" v-bind:value="user.id"
                  data-bs-toggle="popover" data-bs-trigger="hover focus">
                {{ user.firstName }} {{ user.lastName }}
              </li>
            </ul>
          </div>
        </div>
      </div>

      <!-- Pre filled in information -->
      <div class="row my-lg-2">
        <!-- User's full name-->
        <div class="col-md-12 col-6 d-md-flex flex-md-row">
          <label for="user-full-name" class="fw-bold me-md-2">Name:</label>
          <p id="user-full-name">{{ userFullName }}</p>
        </div>

        <!-- User's location details -->
        <div class="col-md-12 col-6 d-md-flex flex-md-row">
          <label for="user-location" class="fw-bold me-md-2">Location:</label>
          <p id="user-location">{{ userLocation }}</p>
        </div>
      </div>


      <!-- Card title -->
      <div class="row my-lg-2">
        <div class="col-md-3 ">
          <label :for="'card-title' + type" class="fw-bold">Title*:</label>
        </div>
        <div class="col-md">
          <input :id="'card-title' + type" :class="`form-control ${formErrorClasses.titleError}`" v-model="title"
                 :maxlength="config.config.title.maxLength" @input="isTitleInvalid">
          <div id="card-title-invalid-feedback" class="invalid-feedback" v-if="formError.titleError">
            {{ formError.titleError }}
          </div>
        </div>
      </div>

      <!-- Card Description -->
      <div class="row my-4">
        <div class="col-md-3 ">
          <label :for="'card-description'" class="fw-bold">Description:</label>
        </div>
        <div class="col-md">
          <textarea :id="'card-description'"
                    :class="`form-control ${formErrorClasses.descriptionError}`"
                    v-model="description"
                    :maxlength="config.config.description.maxLength"
                    @input="isDescriptionInvalid"/>
          <div id="card-description-invalid-feedback" class="invalid-feedback" v-if="formError.descriptionError">
            {{ formError.descriptionError }}
          </div>
        </div>
      </div>

      <!-- Card Keywords -->
      <div class="row my-4">
        <div class="col-md-3 ">
          <label v-bind:for="'card-keywords-' + this.$props.currentModal" class="fw-bold">Keywords:</label>
        </div>
        <div class="col-md">
          <div style="position: relative; height: 80px;" :class="`form-control ${formErrorClasses.keywordsError}`">
            <div v-html="keywordsBackdrop" ref="keywordsBackdrop" class="form-control keywords-backdrop"
                 style="resize: none; overflow-y: scroll" disabled/>
            <textarea ref="keywordsInput" v-bind:id="'card-keywords-' + type"
                      class="form-control keywords-input " style="resize: none; overflow-y: scroll; "
                      v-model="keywordsInput"
                      @scroll="handleKeywordsScroll" @keydown="handleKeywordsScroll"/>
          </div>
          <div id="autofill-container" ref="autofill-container">
            <ul class="autofill-options" id="autofill-list" ref="autofill-list">
              <li
                  v-for="keyword in autocompleteKeywords"
                  v-bind:key="keyword.id"
                  v-bind:id="keyword.id"
                  v-on:click="updateKeyword(keyword.name)"
              >{{ keyword.name }}
              </li>
            </ul>
          </div>
          <div id="card-keywords-invalid-feedback" class="invalid-feedback" v-if="formError.keywordsError">
            {{ formError.keywordsError }}
          </div>
        </div>
      </div>

    </form>

    <div v-if="waitingForResponse">
      <LoadingDots></LoadingDots>
    </div>

  </div>
</template>

<script>
import Api from "../../Api";
import cardConfig from "../../configs/MarketplaceCard"
import Cookies from "js-cookie";
import User, {UserRole} from "../../configs/User"
import Autofill from "../autofill";
import LoadingDots from "../LoadingDots";

export default {
  name: "EditCreateCardModal",
  components: {LoadingDots},
  props: {
    type: {
      type: String,
      default: "none",
      required: false
    },
    currentModal: {
      type: String,
      default: 'currentModal',
      required: false
    }
  },
  data() {
    return {
      user: User,

      users: [],

      /** For api error we display the error through this model error */
      modalError: "",
      /** */
      config: cardConfig,
      /** Keep one string to contain the invalid class mark*/
      isInvalidClass: "is-invalid",
      /** Contains the user's role */
      userRole: "",
      /** The keyword prefix added to all keywords */
      keywordPrefix: "#",

      /** Keeps track of the selected section */
      sectionSelected: "",
      /** Contains all the possible section values for the select field */
      sections: {
        FOR_SALE: "ForSale",
        WANTED: "Wanted",
        EXCHANGE: "Exchange"
      },
      /** Contains what is in the creator name input field */
      creatorInput: "",
      /** Contains the creators id */
      creatorId: "",
      /** Contains the user's full name to be displayed as a prefilled value*/
      userFullName: "",
      /** Contains the prefilled value of the user's address (only city and suburb)*/
      userLocation: "",
      /** Creator object */
      creator: {
        firstName: "",
        lastName: "",
        homeAddress: {
          suburb: "",
          city: ""
        }
      },
      /** Keeps track of the user's title input */
      title: "",
      /** Keeps track of the user's description input.*/
      description: "",
      /** */
      keywordsInput: "",
      /** */
      keywordsBackdrop: "",
      /** Used to detect if the submit button was attempted*/
      submitAttempted: false,

      /** Error messages */
      formError: {
        sectionSelectionError: "",
        titleError: "",
        descriptionError: "",
        keywordsError: "",
        creatorIdError: ""
      },

      /** Contains all the error classes for each input field */
      formErrorClasses: {
        sectionSelectionError: "",
        titleError: "",
        descriptionError: "",
        keywordsError: "",
        creatorIdError: ""
      },

      /** Contains the latest keyword search used to call the keyword searching API endpoint */
      latestApiCallSearch: null,

      /** Contains the list of current autocompletion keywords (based on currently selected keyword) */
      autocompleteKeywords: [],

      /** Contains the list of keyword IDs returned by 'createKeywordIfNotExisting' to use in the edit card PUT request */
      newKeywordIDs: [],

      autofillState: 'initial',

      waitingForResponse: false,
    }
  },
  methods: {
    /**
     * Determines if a section choice made by the user is valid. And updates the error
     * message accordingly and returns the class state. This also updates the form error class.
     *
     *  @return {boolean} Returns true if the input field is invalid.
     * */
    isSectionSelectedInvalid() {
      if (this.submitAttempted) {
        if (Object.values(this.sections).indexOf(this.sectionSelected) === -1) {
          this.formError.sectionSelectionError = "Please select a valid choice.";
          this.formErrorClasses.sectionSelectionError = this.isInvalidClass;
          return true
        }
      }
      this.formError.sectionSelectionError = "";
      this.formErrorClasses.sectionSelectionError = "";
      return false
    },
    /**
     * Determines if a title inputted by the user is valid. This also updated the title error
     * message accordingly and returns the class state. This also updates the form error class.
     *
     * @return {boolean} Returns true if the input field is invalid.
     * */
    isTitleInvalid() {
      if (this.submitAttempted) {
        if (this.title.length > cardConfig.config.title.maxLength || this.title.length < cardConfig.config.title.minLength) {
          this.formError.titleError = `The title must be between ${cardConfig.config.title.minLength} and ${cardConfig.config.title.maxLength} in length.`
          this.formErrorClasses.titleError = this.isInvalidClass;
          return true;
        }
      }
      this.formError.titleError = ""
      this.formErrorClasses.titleError = ""
      return false;
    },
    /**
     * Determines if the description inputted is valid within the rules of the Card. This updates the description
     * error message. This also updates the form error class.
     *
     * @return {boolean} Returns true if the input field is invalid.
     * */
    isDescriptionInvalid() {
      if (this.submitAttempted) {
        if (this.description !== null && (this.description.length > cardConfig.config.description.maxLength || this.description.length < cardConfig.config.description.minLength)) {
          this.formError.descriptionError = `The description length must be between ${cardConfig.config.description.minLength} and ${cardConfig.config.description.maxLength} in length.`
          this.formErrorClasses.descriptionError = this.isInvalidClass;
          return true
        }
      }
      this.formError.descriptionError = ""
      this.formErrorClasses.descriptionError = ""
      return false
    },
    /**
     * Determines if the keywords inputted is valid within the rules of the Card. This updates the keywords
     * error message. This also updates the form error class.
     *
     * @return {boolean} Returns true if the input field is invalid.
     * */
    isKeywordsInvalid() {
      if (this.submitAttempted) {
        let invalidKeywords = [];
        for (const keyword of this.getKeywords()) {
          if (keyword.length >= this.config.config.keyword.maxLength || keyword.length < this.config.config.keyword.minLength) {
            invalidKeywords.push(keyword)
          }
        }
        if (invalidKeywords.length > 0) {
          this.formError.keywordsError = `All keywords need to be between ${this.config.config.keyword.minLength} and ${this.config.config.keyword.maxLength} in length.`
          this.formErrorClasses.keywordsError = this.isInvalidClass
          return true
        }
      }
      this.formError.keywordsError = ""
      this.formErrorClasses.keywordsError = ""
      return false
    },
    /**
     * Determine if the creator id provided is invalid, and if so updates error messages and the creator
     * id variable accordingly.
     *
     * @return {boolean} True if the creator id provided is invalid. Otherwise false.
     * */
    isCreatorIdInvalid() {
      if (this.submitAttempted) {
        if (!this.user.isAdministrator(this.userRole)) {
          this.creatorId = Cookies.get("userID")

          // If the user id token is missing we logout the user, as he is not allowed to create a card.
          if (!this.creatorId) {
            this.modalError = "User id token is missing!"
            Api.signOut().then(() => {
              this.$router.push({name: 'Login'})
            }).catch(() => {
              this.$router.push({name: 'Login'})
            });
            return true;
          }

          // If the creator Id is missing we inform the user.
        } else if (this.creatorId.length <= 0) {
          this.formErrorClasses.creatorIdError = this.isInvalidClass
          this.formError.creatorIdError = "This field is required."
          return true;
        }
      }
      this.formErrorClasses.creatorIdError = ""
      this.formError.creatorIdError = ""
      return false;
    },
    /**
     * Ensures when the table scrolls that they remain at the same height.
     * */
    handleKeywordsScroll() {
      this.$refs.keywordsBackdrop.scrollTop = this.$refs.keywordsInput.scrollTop;
      this.$refs.keywordsInput.scrollTop = this.$refs.keywordsBackdrop.scrollTop;
    },
    /**
     * Populates the user's full name (first, last) and location (suburb, city) fields.
     * @param currentID Current User ID
     */
    populateUserInfo(currentID) {
      Api.getUser(currentID).then(response => {
        this.userFullName = response.data.firstName + " " + response.data.lastName
        this.creator.firstName = response.data.firstName;
        this.creator.lastName = response.data.lastName;
        if (!this.userRole) {
          this.userRole = response.data.role || UserRole.USER;
        }

        const city = response.data.homeAddress.city;
        const suburb = response.data.homeAddress.suburb;
        this.creator.homeAddress.city = response.data.homeAddress.city;
        this.creator.homeAddress.suburb = response.data.homeAddress.suburb;
        if (suburb && city) {
          this.userLocation = suburb + ", " + city
        } else if (suburb) {
          this.userLocation = suburb
        } else if (city) {
          this.userLocation = city
        } else {
          this.userLocation = "N/A"
        }
        this.modalError = "";
      }).catch(
          () => {
            this.modalError = "Could not get user credentials";
          })
    },
    /**
     * Get the list of unique keywords given by the keywords field.
     *
     * @return {string[]} A list of keywords given by the input field.
     */
    getKeywords() {
      // Get all the unique strings and filter out any spaces and or new lines from the keywords.
      let keywords = [...new Set(this.keywordsInput.split(" "))].filter((keyword) => keyword !== "" && keyword !== "\n");

      // Removes the keyword prefix.
      for (let i = 0; i < keywords.length; i++) {
        // Check that the first set of characters are indeed the prefix!
        if (keywords[i].substring(0, this.keywordPrefix.length) === this.keywordPrefix) {
          // Remove the prefix.
          keywords[i] = keywords[i].substring(this.keywordPrefix.length);
        }
      }

      return keywords;
    },
    /**
     * Ensures all the card data provided is valid.
     *
     * @return {boolean} true if all the input fields pass. Otherwise false.
     * */
    isCardDataValid() {
      let allInputFieldsPass = true;

      if (this.isSectionSelectedInvalid()) {
        allInputFieldsPass = false;
      }

      if (this.isTitleInvalid()) {
        allInputFieldsPass = false;
      }

      if (this.isDescriptionInvalid()) {
        allInputFieldsPass = false;
      }

      if (this.isKeywordsInvalid()) {
        allInputFieldsPass = false;
      }

      if (this.isCreatorIdInvalid()) {
        allInputFieldsPass = false;
      }

      // If not all input fields have passed we cancel the call.
      return allInputFieldsPass
    },
    /**
     * Performs an API call to the backend to create a new card.
     * */
    async createNewCard() {
      // Prevent the default submission click
      this.submitAttempted = true;

      if (!this.isCardDataValid()) {
        return;
      }

      this.waitingForResponse = true;

      // If we are not an admin then we need to update the creatorId.
      if (!this.user.isAdministrator(this.userRole)) {
        this.creatorId = Cookies.get("userID")
      }

      const keywords = this.getKeywords();
      for (const keyword of keywords) {
        await this.createKeywordIfNotExisting(keyword);
      }
      const keywordIds = this.newKeywordIDs;

      // Create the new card and assign it the content
      const newCard = {
        creatorId: this.creatorId,
        section: this.sectionSelected,
        title: this.title,
        description: this.description,
        keywordIds: keywordIds
      }

      Api.addNewCard(newCard).then(
          (res) => {
            this.waitingForResponse = false;
            if (res.status === 201) {
              this.$emit("new-card-created");
              this.$parent.hideModal()
              this.$router.go();
            }
          }
      ).catch(
          (error) => {
            this.waitingForResponse = false;
            if (error.response) {
              if (error.response.status === 400) {
                this.modalError = `Error: ` + error.response.data.message;
              } else if (error.response.status === 401) {
                this.modalError = `401: Access token missing`;
              } else if (error.response.status === 403) {
                this.modalError = `403: Cannot create card for another user if not GAA or DGAA.`;
              } else {
                this.modalError = `${error.response.status}: SOMETHING WENT WRONG`;
              }
            } else if (error.request) {
              this.modalError = "Server Timeout"
            } else {
              this.modalError = "Unexpected error occurred."
            }
          }
      )

    },
    /**
     * Function that performs a keyword POST to create the keyword if it does not exist.
     * The POST keyword creation endpoint returns the keyword ID for the newly
     * created keyword or the existing keyword if it already exists.
     * The returned keyword ID is stored in the newKeywordIDs array for use in other functions.
     * */
    async createKeywordIfNotExisting(keyword) {

      const newKeyword = {
        name: keyword
      }

      await Api.addNewKeyword(newKeyword).then(
          (res) => {
            this.newKeywordIDs.push(res.data.keywordId);
          }
      ).catch(
          (error) => {
            this.waitingForResponse = false;
            if (error.response) {
              if (error.response.status === 400) {
                this.modalError = `Error: ` + error.response.data.message;
              } else if (error.response.status === 401) {
                this.modalError = `401: Access token missing`;
              } else {
                this.modalError = `${error.response.status}: SOMETHING WENT WRONG`;
              }
            } else if (error.request) {
              this.modalError = "Server Timeout"
            } else {
              this.modalError = "Unexpected error occurred."
            }
          }
      )
    },
    /**
     * Performs an API call to the backend to edit the current card.
     * */
    async editCurrentCard() {

      // Prevent the default submission click
      this.submitAttempted = true;

      if (!this.isCardDataValid()) {
        return;
      }

      this.waitingForResponse = true;

      // Clear the list of keyword IDs
      this.newKeywordIDs = []

      // Check for existing keywords and create ones that don't already exist
      const keywords = this.getKeywords();
      for (const keyword of keywords) {
        await this.createKeywordIfNotExisting(keyword);
      }
      const keywordIds = this.newKeywordIDs;

      if (this.description === null) {
        this.description = "";
      }

      // Object to hold the updated fields
      const updatedCard = {
        title: this.title,
        description: this.description,
        keywordIds: keywordIds
      }

      Api.editCard(this.id, updatedCard).then(
          (res) => {
            this.waitingForResponse = false;
            if (res.status === 200) {
              this.$emit("new-card-created");
              this.$parent.hideModal()
              this.$router.go();
            }
          }
      ).catch(
          (error) => {
            this.waitingForResponse = false;
            if (error.response) {
              if (error.response.status === 400) {
                this.modalError = `Error: ` + error.response.data.message;
              } else if (error.response.status === 401) {
                this.modalError = `401: Access token missing`;
              } else if (error.response.status === 403) {
                this.modalError = `403: Cannot edit card for another user if not GAA or DGAA.`;
              } else {
                this.modalError = `${error.response.status}: SOMETHING WENT WRONG`;
              }
            } else if (error.request) {
              this.modalError = "Server Timeout"
            } else {
              this.modalError = "Unexpected error occurred."
            }
          }
      )
    },
    /**
     * Takes a string and adds the keyword prefix symbol to the front of the string.
     *  @param keyword {string} The keyword string
     *
     *  @return {String} The keyword modified to include the prefix if it is longer then 0.
     * */
    addKeywordPrefix(keyword) {

      // If the keyword is not a string, do not process it.
      if (typeof keyword !== "string") {
        throw new Error("keyword must be string!")
      }

      // Add the prefix to all strings that are not "" or do not already have that prefix
      if (keyword.length > 0) {
        if (keyword[0] !== this.keywordPrefix) {
          keyword = this.keywordPrefix + keyword;
        }
      }
      return keyword;
    },
    /**
     * Takes a keyword and ensures and returns the string such that it cannot have characters longer then the allocated
     * amount.
     * @param keyword {string} Takes the keyword string.
     *
     * @return {string} The modified keyword string that only includes the first {maxlength} characters.
     * */
    enforceKeywordMaxLength(keyword) {
      // If the keyword is not a string, do not process it.
      if (typeof keyword !== "string") {
        throw new Error("keyword must be string!")
      }

      // 'Cut off' the extra characters.
      if (keyword.length >= this.config.config.keyword.maxLength) {
        keyword = keyword.substring(0, this.config.config.keyword.maxLength);
      }

      return keyword
    },
    /**
     * Converts section from GET call
     */
    convertSection(section) {
      switch (section) {
        case 'FORSALE':
          this.sectionSelected = this.sections.FOR_SALE
          return;
        case 'WANTED':
          this.sectionSelected = this.sections.WANTED
          return;
        case 'EXCHANGE':
          this.sectionSelected = this.sections.EXCHANGE
          return;
      }
    },
    /**
     * Performs actions after a click or keyup has been detected on the page (depending on whether the click was inside
     * or outside the keyword text-box).
     */
    onClickOrKeyUp() {
      const elementID = 'card-keywords-' + this.$props.currentModal;
      if (elementID === document.activeElement.id) {
        this.updateCursorPosition()
      } else {
        this.autocompleteKeywords = [];
      }
    },
    /**
     * Performs actions after a key has been released, relating to arrow key navigation inside the keyword text-box.
     * This sets up the list of keywords for autocompletion and also the dismissal of the list after using a keyword
     * suggestion, if there is no match, or if a click has been registered outside the text-box.
     */
    updateCursorPosition() {
      const elementID = 'card-keywords-' + this.$props.currentModal;
      this.textCursorPosition = document.getElementById(elementID).selectionStart;
      this.currentKeyword = "";

      let currentKeywordStartEnd = this.getCurrentKeywordStartEnd();

      let keywordSearch = this.currentKeyword;
      if (this.keywordsInput === "" ||
          (this.keywordsInput.length === this.textCursorPosition &&
              this.keywordsInput.charAt(this.keywordsInput.length - 1) === " ")
      ) {
        keywordSearch = "";
      }

      if (this.latestApiCallSearch !== keywordSearch) {
        this.autocompleteKeywords = [];
        if (
            currentKeywordStartEnd !== false ||
            this.keywordsInput === "" ||
            (this.keywordsInput.length === this.textCursorPosition &&
                this.keywordsInput.charAt(this.keywordsInput.length - 1) === " ")
        ) {
          this.autocompleteKeywords = [];
          Api.searchKeywords(keywordSearch).then(response => {
            this.latestApiCallSearch = keywordSearch;

            let autocompleteKeywords = [];

            for (const keyword of response.data) {
              if (keywordSearch !== keyword.name) {
                autocompleteKeywords.push(keyword);
              }
            }

            this.autocompleteKeywords = autocompleteKeywords;
          })
        }
      }
    },
    /**
     * Updates the currently selected keyword with the given keyword (for keyword autocompletion).
     */
    updateKeyword(keyword) {
      if (this.keywordsInput === "" ||
          (this.keywordsInput.length === this.textCursorPosition &&
              this.keywordsInput.charAt(this.keywordsInput.length - 1) === " ")
      ) {
        this.keywordsInput = this.keywordsInput + keyword;
      } else {
        let currentKeywordStartEnd = this.getCurrentKeywordStartEnd();

        if (currentKeywordStartEnd !== false) {
          let [currentKeywordStart, currentKeywordEnd] = currentKeywordStartEnd;

          this.keywordsInput =
              this.keywordsInput.substring(0, currentKeywordStart) +
              this.keywordsInput.substring(currentKeywordStart, currentKeywordEnd).replace(
                  this.currentKeyword, keyword
              ) +
              this.keywordsInput.substring(currentKeywordEnd);
        }
      }
    },
    /**
     * Calculates and returns the start and end positions of the current keyword, returns false if invalid indexes are
     * retrieved.
     */
    getCurrentKeywordStartEnd() {
      if (this.textCursorPosition === undefined) {
        this.textCursorPosition = 0
      }

      let currentKeywordStart = this.keywordsInput.substring(0, this.textCursorPosition + 1).lastIndexOf("#");

      if (currentKeywordStart !== -1) {
        let currentKeywordEnd =
            this.keywordsInput.substring(this.textCursorPosition).indexOf(" ") + this.textCursorPosition;

        if (currentKeywordEnd - this.textCursorPosition !== -1) {
          this.currentKeyword = this.keywordsInput.substring(currentKeywordStart + 1, currentKeywordEnd);

          return [currentKeywordStart, currentKeywordEnd];

        } else {
          this.currentKeyword = this.keywordsInput.substring(currentKeywordStart + 1);

          return [currentKeywordStart, this.keywordsInput.length];
        }
      } else {
        return false;
      }
    },

    /**
     * Gets current state of the selected card from the API
     */
    async getCurrentData() {
      await Api.getDetailForACard(this.id).then(response => (this.populateData(response.data))).catch((error) => {
        if (error.response) {
          if (error.response.status === 400) {
            this.$router.push({path: '/pageDoesNotExist'});
          } else if (error.response.status === 401) {
            this.$router.push({path: '/invalidtoken'});
          } else if (error.response.status === 406) {
            this.$router.push({path: '/noCard'});
          }
        } else {
          this.$router.push({path: '/noCard'});
        }
      })
    },
    /**
     * Populates the input boxes with the current state of the card
     * @param data Data received from the backend
     */
    populateData(data) {
      this.convertSection(data.section)
      this.section = data.section;
      this.title = data.title;
      this.description = data.description;
      this.keywordsInput = this.convertKeywordsToString(data.keywords);
      this.populateUserInfo(data.creator.id);
      this.creatorId = data.creator.id;
    },
    /**
     * Converts a list of keywords to a string for populating current data
     * @param keywords keywords from the API call
     * @return String of keywords
     */
    convertKeywordsToString(keywords) {
      let keyString = ""
      for (let key in keywords) {
        keyString += keywords[key].name + " "
      }
      return keyString.slice(0, -1)
    },
    /**
     * Resets the Modals error messages
     * */
    resetErrors() {
      // Resetting all the form error classes
      this.formErrorClasses.sectionSelectionError = ""
      this.formErrorClasses.titleError = ""
      this.formErrorClasses.descriptionError = ""
      this.formErrorClasses.keywordsError = ""

      // Resetting all the form error messages
      this.formError.sectionSelectionError = ""
      this.formError.titleError = ""
      this.formError.descriptionError = ""
      this.formError.keywordsError = ""
    },
    /**
     * Sets the prefilled data for the edit modal
     * @param id Card id
     */
    setData(id) {
      this.resetErrors()
      this.id = id
      this.getCurrentData()
    },
    /**
     * Resets all input data for creating a new card
     */
    resetData() {
      this.id = null
      this.resetErrors()

      // Resetting all the variables between card creations.
      this.submitAttempted = false;
      this.description = ""
      this.title = ""
      this.sectionSelected = ""
      this.keywordsInput = ""
    },
    /**
     * Click event handler for the inventory ID input. Will toggle the autofill options display when needed and
     * also calls fillData, if applicable.
     *
     * This function is based off of the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    autofillClick() {
      const currentFocus = document.activeElement;
      const input = this.$refs["card-creator"];
      switch (this.autofillState) {
        case "initial":
          Autofill.toggleList('open', this.$refs["autofill-list-users"]);
          this.autofillState = 'opened';
          break;
        case 'opened':
          if (currentFocus === input) {
            Autofill.toggleList('closed', this.$refs["autofill-list-users"]);
            this.autofillState = 'initial';
          } else if (currentFocus.tagName === 'LI') {
            this.fillData(currentFocus);
            Autofill.toggleList('closed', this.$refs["autofill-list-users"]);
            this.autofillState = 'closed';
          }
          break;
        case 'filtered':
          if (currentFocus.tagName === 'LI') {
            this.fillData(currentFocus);
            Autofill.toggleList('closed', this.$refs["autofill-list-users"]);
            this.autofillState = 'closed';
          }
          break;
        case 'closed':
          Autofill.toggleList('open', this.$refs["autofill-list-users"]);
          this.autofillState = 'filtered';
          break;
      }
    },
    /**
     * Handles keyboard input when navigating autofill dropdown menu
     *
     * This function is adapted from the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    keyPressedOnInput(event) {
      const key = event.key;
      const currentFocus = document.activeElement;
      const input = this.$refs["autofill-input"];

      switch (key) {
        case 'Enter':
          if (this.autofillState === 'initial') {
            // If state = initial, toggle open and set state to opened
            Autofill.toggleList('open', this.$refs["autofill-list-users"]);
            this.autofillState = 'opened';
          } else if (this.autofillState === 'opened' && currentFocus.tagName === 'LI') {
            // If state = opened and focus on list, fill data and set state to closed
            this.fillData(currentFocus)
            Autofill.toggleList('closed', this.$refs["autofill-list-users"])
            this.autofillState = 'closed';
          } else if (this.autofillState === 'opened' && currentFocus === input) {
            // If state = opened and focus on input, close it
            Autofill.toggleList('closed', this.$refs["autofill-list-users"])
            this.autofillState = 'closed';
          } else if (this.autofillState === 'filtered' && currentFocus.tagName === 'LI') {
            // If state = filtered and focus on list, fill data and set state to closed
            this.fillData(currentFocus)
            Autofill.toggleList('closed', this.$refs["autofill-list-users"])
            this.autofillState = 'closed';
          } else if (this.autofillState === 'filtered' && currentFocus === input) {
            // If state = filtered and focus on input, set state to opened
            Autofill.toggleList('open')
            this.autofillState = 'opened';
          } else {
            // If state = closed, set state to filtered. I.e. open but keep existing input.
            Autofill.toggleList('open', this.$refs["autofill-list-users"])
            this.autofillState = ('filtered');
          }
          break;
        case 'Escape':
          if (this.autofillState === 'opened' || this.autofillState === 'filtered') {
            // Close the list
            Autofill.toggleList('closed', this.$refs["autofill-list-users"]);
            this.autofillState = 'initial';
          }
          break;
        case 'ArrowDown':
          if (this.autofillState === 'initial' || this.autofillState === 'closed') {
            // If state = initial or closed, set state to opened and moveFocus to first
            Autofill.toggleList('open')
            Autofill.moveFocus(input, 'forward', this.$refs["card-creator"], this.$refs["autofill-list-users"].children, document.activeElement)
            this.autofillState = 'opened';
          } else {
            // If state = opened/filtered and focus on input/list, moveFocus to first/next
            Autofill.toggleList('open', this.$refs["autofill-list-users"])
            Autofill.moveFocus(currentFocus, 'forward', this.$refs["card-creator"], this.$refs["autofill-list-users"].children, document.activeElement)
          }
          break;
        case 'ArrowUp':
          if (this.autofillState === 'initial' || this.autofillState === 'closed') {
            // If state = initial, set state to opened and moveFocus to last
            // If state = closed, set state to opened and moveFocus to last
            Autofill.toggleList('Open', this.$refs["autofill-list-users"])
            Autofill.moveFocus(input, 'back', this.$refs["card-creator"], this.$refs["autofill-list-users"].children, document.activeElement)
            this.autofillState = 'opened';
          } else {
            // If state = opened/filtered and focus on input/list, moveFocus to last/previous
            Autofill.moveFocus(currentFocus, 'back', this.$refs["card-creator"], this.$refs["autofill-list-users"].children, document.activeElement)
          }
          break;
        default:
          if (this.autofillState === 'initial') {
            // If state = initial, toggle open, filter and set state to filtered
            Autofill.toggleList('open', this.$refs["autofill-list-users"]);
            Autofill.filterOptions(this.$refs["card-creator"].value, this.$refs["autofill-list-users"].children, this.autofillState);
            this.autofillState = 'filtered';
          } else if (this.autofillState === 'opened') {
            // If state = opened, filter and set state to filtered
            Autofill.filterOptions(this.$refs["card-creator"].value, this.$refs["autofill-list-users"].children, this.autofillState);
            this.autofillState = 'filtered';
          } else if (this.autofillState === 'closed') {
            // If state = closed, filter and set state to filtered
            Autofill.filterOptions(this.$refs["card-creator"].value, this.$refs["autofill-list-users"].children, this.autofillState);
            this.autofillState = 'filtered';
          } else { // Already filtered
            Autofill.filterOptions(this.$refs["card-creator"].value, this.$refs["autofill-list-users"].children, this.autofillState);
          }
          break;
      }
    },
    /**
     * Sets the value of the inventory ID input to the ID of the given item. Does the same for quantity, total price and expiry date.
     *
     * This function is based off of the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    fillData(clickedUser) {
      let finalUser;
      for (let user of this.users) {
        if (user.id === clickedUser.value) {
          finalUser = user;
        }
      }
      const city = finalUser.homeAddress.city;
      const suburb = finalUser.homeAddress.suburb;
      this.currentUser = finalUser;
      this.userFullName = finalUser.firstName + " " + finalUser.lastName;

      if (suburb && city) {
        this.userLocation = suburb + ", " + city
      } else if (suburb) {
        this.userLocation = suburb
      } else if (city) {
        this.userLocation = city
      } else {
        this.userLocation = "N/A"
      }

      this.creatorId = finalUser.id;

      this.autofillInput = '';
    },
    /**
     * Searches for a list of users to display in the dropdown based on an input
     */
    searchUsers() {
      Api.searchUsers(this.creatorInput, "fullNameASC", 0).then((response) => {
        this.users = response.data;
      }, (error) => {
        if (error.response) {
          if (error.response.status === 401) {
            this.$router.push({path: '/invalidtoken'})
          } else {
            console.log(`${error.response.status} - Something went wrong`)
          }
        } else {
          console.log(`${error.response.status} - Something went wrong`)
        }
      })
    }
  },
  mounted() {
    const currentID = Cookies.get('userID');
    this.creatorId = currentID
    if (currentID) {
      this.populateUserInfo(currentID)
    }

    // Global event listener to toggle autofill list display
    let self = this;
    document.addEventListener('click', function (event) {
      if (!event.target.closest('#autofill-container-users') && self.autofillState !== 'closed' && self.$refs["autofill-list-users"]) {
        Autofill.toggleList('closed', self.$refs["autofill-list-users"]);
        self.autofillState = 'initial';
      }
    })

    document.addEventListener('keyup', this.onClickOrKeyUp)
    document.addEventListener('click', this.onClickOrKeyUp)
  },
  watch: {
    /**
     * Prevents the value from breaking the expected rules.
     * @param val The new value.
     */
    keywordsInput: function (val) {
      // Only allow spaces. new line --> space
      val = val.replace(/\n/g, " ").replace(/\n\s*\n/g, ' ');
      val = val.replace(/\s+/g, ' ').replace(/\s+#*/g, " ");

      let strings = val.split(" ");
      for (let i = 0; i < strings.length; i++) {
        // Add a hashtag in front of all strings besides
        strings[i] = this.addKeywordPrefix(strings[i])

        // Prevent string from being over the maximum length
        strings[i] = this.enforceKeywordMaxLength(strings[i])
      }

      val = strings.join(" ")

      // Assign the process val to the keyword input
      this.keywordsInput = val;

      // Defines the highlight tag
      const highlightHtml = (text) => `<strong class="keywordHighlight">${text}</strong>`

      // Get a list of unique strings from the array
      const uniqueStrings = [...new Set(val.split(" "))];
      let result = val.split(" ");

      // For each unique string we replace it with a highlight to surround it.
      for (const uniqueString of uniqueStrings) {
        if (uniqueString !== "") {
          for (let i = 0; i < result.length; i++) {
            if (result[i] === uniqueString) {
              result[i] = highlightHtml(result[i]);
            }
          }
        }
      }
      // Add the text back with the highlights
      this.keywordsBackdrop = result.join(" ") + " "

      // Ensures when new data is added the scroll bar is updated along with it.
      this.handleKeywordsScroll();

      // Update any error messages.
      this.isKeywordsInvalid()
    }
  }
}
</script>


<style>
/** ensure that any changes made here happen on both the front and back layer!!!! */

textarea.form-control {
  resize: none;
}

div.keywords-backdrop, textarea.keywords-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  letter-spacing: 2px;
  word-spacing: 2px;
  line-height: 2em;
  font-family: 'Roboto', sans-serif;
}

div.keywords-backdrop {
  background-color: #fff;
  white-space: pre-wrap;
  white-space: -moz-pre-wrap;
  white-space: -o-pre-wrap;
  word-wrap: break-word;
  -ms-word-wrap: break-word;
  margin: 0;
  border: 1px solid transparent;
  color: transparent;
}

textarea.keywords-input, textarea.keywords-input:focus {
  background-color: transparent;
  color: black;
  caret-color: #000;
  z-index: 10;
}

strong.keywordHighlight {
  font-family: 'Roboto', sans-serif;
  color: transparent;
  outline: 1px solid #888888;
  outline-offset: 1px;
  font-weight: 400;
  letter-spacing: 2px;
  word-spacing: 2px;
  line-height: 2em;
}

/*********************************************************************
                        Autofill styling

     This CSS is a modified version of the examples found on
  Julie Grundy's tutorial for creating a custom select element:

  https://24ways.org/2019/making-a-better-custom-select-element/
*********************************************************************/

#autofill-container, #autofill-container-users {
  position: relative;
}

#card-creator::-ms-expand {
  display: none;
}

.autofill-options {
  border: 1px solid lightgray;
  border-radius: 0 0 0.25em 0.25em;
  line-height: 1.25;
  padding: 0;
  list-style-type: none;
  cursor: pointer;
  z-index: 2;
  position: absolute;
  width: 100%;
  background-color: #ffffff;
}

.autofill-options li {
  padding: 1em;
}

.autofill-options li:hover, .autofill-options li:focus {
  background: #1EBA8C;
  color: #fff;
}

.hidden-all {
  display: none;
}

</style>
