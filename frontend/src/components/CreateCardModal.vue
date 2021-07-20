<template>
  <div>
    <!-- Used to create a card -->
    <button id="open-create-card-modal-button" class="btn green-button-transparent col-12" @click="showModal">Create Card</button>

    <!-- Create Card Modal -->
    <div class="modal fade" ref="createCardModal" id="create-card-modal" tabindex="0">
      <div class="modal-dialog">
        <div class="modal-content">

          <!-- Modal header-->
          <div class="modal-header">
            <h5 class="modal-title">Create Card</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" tabindex="-1"  ></button>
          </div>

          <!-- Modal body -->
          <div class="modal-body">
            <form class="needs-validation" novalidate @submit.prevent>

              <!-- Modal error message -->
              <div class="row my-lg-2" v-if="modalError">
                <div class="col-12 mx-auto">
                  <div class="alert alert-danger">
                    {{modalError}}
                  </div>
                </div>
              </div>

              <!-- Select a section  -->
              <div class="row my-lg-2">
                <div class="col-md-6 my-2 my-lg-0">
                  <label for="section-selection" class="form-label">What section would you like to post your card?*</label>
                </div>
                <div class="col-md-6 my-2 my-lg-0">
                  <select id="section-selection" name="section-selection" :class="`form-select ${formErrorClasses.sectionSelectionError}`"
                          v-model="sectionSelected" @click="isSectionSelectedInvalid">
                    <option value="" disabled selected>Select section</option>
                    <option id="for-sale-option" :value="sections.FOR_SALE">For Sale</option>
                    <option id="exchange-option" :value="sections.EXCHANGE">Exchange</option>
                    <option id="wanted-option" :value="sections.WANTED">Wanted</option>
                  </select>
                  <div id="section-selection-invalid-feedback" class="invalid-feedback" v-if="formError.sectionSelectionError">
                    {{formError.sectionSelectionError}}
                  </div>
              </div>
              </div>

              <hr>

              <!-- Creator id input -->
              <div class="row my-lg-2 my-4" v-if="user.isAdministrator(userRole)">
                <div class="col-md-3 ">
                  <label for="card-creator-id" class="fw-bold">Creator Id*:</label>
                </div>
                <div class="col-md">
                  <input id="card-creator-id" :class="`form-control ${formErrorClasses.creatorIdError}`" v-model="creatorId" @input="isCreatorIdInvalid" @focusout="() => populateUserInfo(creatorId)">
                  <div class="invalid-feedback" v-if="formError.creatorIdError">
                    {{formError.creatorIdError}}
                  </div>
                </div>
              </div>

              <!-- Pre filled in information -->
              <div class="row my-lg-2">
                <!-- User's full name-->
                <div class="col-md-12 col-6 d-md-flex flex-md-row">
                    <label for="user-full-name" class="fw-bold me-md-2">Name:</label>
                    <p id="user-full-name">{{userFullName}}</p>
                </div>

                <!-- User's location details -->
                <div class="col-md-12 col-6 d-md-flex flex-md-row">
                    <label for="user-location" class="fw-bold me-md-2">Location:</label>
                    <p id="user-location">{{userLocation}}</p>
                </div>
              </div>

              <!-- Card title -->
              <div class="row my-lg-2">
                <div class="col-md-3 ">
                  <label for="card-title" class="fw-bold">Title*:</label>
                </div>
                <div class="col-md">
                  <input id="card-title" :class="`form-control ${formErrorClasses.titleError}`" v-model="title"
                  :maxlength="config.config.title.maxLength" @input="isTitleInvalid">
                  <div id="card-title-invalid-feedback" class="invalid-feedback" v-if="formError.titleError">
                    {{formError.titleError}}
                  </div>
                </div>
              </div>

              <!-- Card Description -->
              <div class="row my-4">
                <div class="col-md-3 ">
                  <label for="card-description" class="fw-bold">Description:</label>
                </div>
                <div class="col-md">
                  <textarea id="card-description" :class="`form-control ${formErrorClasses.descriptionError}`" v-model="description"
                  :maxlength="config.config.description.maxLength" @input="isDescriptionInvalid"/>
                  <div id="card-description-invalid-feedback" class="invalid-feedback" v-if="formError.descriptionError">
                    {{formError.descriptionError}}
                  </div>
                </div>
              </div>



              <!-- Card Keywords -->
              <div class="row my-4">
                <div class="col-md-3 ">
                  <label for="card-keywords" class="fw-bold">Keywords:</label>
                </div>
                <div class="col-md">
                  <div style="position: relative; height: 80px;" :class="`form-control ${formErrorClasses.keywordsError}`">
                    <div v-html="keywordsBackdrop" ref="keywordsBackdrop" class="form-control keywords-backdrop" style="resize: none; overflow-y: scroll" disabled />
                    <textarea ref="keywordsInput" id="card-keywords" class="form-control keywords-input " style="resize: none; overflow-y: scroll; " v-model="keywordsInput"
                    @scroll="handleKeywordsScroll" @keydown="handleKeywordsScroll"/>
                  </div>
                  <div id="autofill-container" ref="autofill-container">
                    <ul class="autofill-options hidden-all" id="autofill-list" ref="autofill-list">
                      <li
                          v-for="keyword in autocompleteKeywords"
                          v-bind:key="keyword.id"
                          v-bind:id="keyword.id"
                          v-on:click="updateKeyword(keyword.name)"
                      >{{ keyword.name }}</li>
                    </ul>
                  </div>
                  <div id="card-keywords-invalid-feedback" class="invalid-feedback" v-if="formError.keywordsError">
                    {{formError.keywordsError}}
                  </div>
                </div>
              </div>


            </form>
          </div>

          <!-- Modal footer -->
          <div class="modal-footer">
            <button id="create-card-button" type="button" class="btn green-button order-1" @click="createNewCard">Create</button>
            <button type="button" class="btn btn-secondary order-0" data-bs-dismiss="modal">Cancel</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {Modal} from 'bootstrap';
import Api from "../Api";
import cardConfig from "../configs/MarketplaceCard"
import Cookies from "js-cookie";
import User, { UserRole} from "../configs/User"

export default {
  name: "CreateCardModal",
  data() {
    return {
      modal: null,
      user: User,

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

      autocompleteKeywords: []
    }
  },
  methods: {
    /**
     * Shows the card creations modal
     *
     * @param event The click event.
     * */
    showModal(event) {
      // Prevent any default events by button clicks
      event.preventDefault();
      // Resetting all the variables between card creations.
      this.submitAttempted = false;
      this.description = ""
      this.title = ""
      this.sectionSelected = ""
      this.keywordsInput = ""
      this.creatorId = Cookies.get('userID') || "";
      this.populateUserInfo(this.creatorId);

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


      // Show the modal itself.
      this.modal.show();


      // Variables for keyword autocompletion calculations
      this.textCursorPosition = 0
      this.currentKeyword = ""
      this.autocompleteKeywords = []
    },
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
          this.formErrorClasses.sectionSelectionError =  this.isInvalidClass;
          return true
        }
      }
      this.formError.sectionSelectionError = "";
      this.formErrorClasses.sectionSelectionError =  "";
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
        if (this.title.length >= cardConfig.config.title.maxLength || this.title.length < cardConfig.config.title.minLength) {
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
        if (this.description.length >= cardConfig.config.description.maxLength || this.description.length < cardConfig.config.description.minLength) {
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
        if (invalidKeywords.length > 0 ) {
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
      let keywords = [...new Set(this.keywordsInput.split(" "))].filter( (keyword) => keyword !== "" && keyword !== "\n");

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
     * @param event {Event} The click event on the submission button.
     * */
    createNewCard(event) {
      // Prevent the default submission click
      event.preventDefault();
      this.submitAttempted = true;

      if (!this.isCardDataValid()) {
        return;
      }

      // If we are not an admin then we need to update the creatorId.
      if (!this.user.isAdministrator(this.userRole)) {
        this.creatorId = Cookies.get("userID")
      }

      // Create the new card and assign it the content
      const newCard = {
        creatorId: this.creatorId,
        section: this.sectionSelected,
        title: this.title,
        description: this.description,
        keywords: this.getKeywords()
      }

      Api.addNewCard(newCard).then(
          (res) => {
            if (res.status === 201) {
              this.$emit("new-card-created");
              this.modal.hide();
              this.$router.go();
            }
          }
      ).catch(
          (error) => {
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
    
    updateCursorPosition(e) {
      this.textCursorPosition = e.target.selectionStart;
      this.currentKeyword = "";
      this.autocompleteKeywords = [];

      let currentKeywordStartEnd = this.getCurrentKeywordStartEnd();

      let keywordSearch = this.currentKeyword;
      if (this.keywordsInput === "" ||
          (this.keywordsInput.length === this.textCursorPosition &&
          this.keywordsInput.charAt(this.keywordsInput.length - 1) === " ")
      ) {
        keywordSearch = "";
      }

      if (
          currentKeywordStartEnd !== false ||
          this.keywordsInput === "" ||
          (this.keywordsInput.length === this.textCursorPosition &&
          this.keywordsInput.charAt(this.keywordsInput.length - 1) === " ")
      ) {
        Api.searchKeywords(keywordSearch).then(response => {
          let autocompleteKeywords = [];

          for (let i = 0; i < response.data.length && i < 5; i++) {
            if (keywordSearch !== response.data[i].name) {
              autocompleteKeywords.push(response.data[i]);
            }
          }
          
          this.autocompleteKeywords = autocompleteKeywords;
        })
      }
    },

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

    getCurrentKeywordStartEnd() {
      let currentKeywordStart = this.keywordsInput.substring(0, this.textCursorPosition + 1).lastIndexOf("#");

      if (currentKeywordStart !== -1) {
        let currentKeywordEnd = this.keywordsInput.substring(this.textCursorPosition).indexOf(" ") + this.textCursorPosition;

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
    }

  },
  mounted() {
    this.modal = new Modal(this.$refs.createCardModal);

    const currentID = Cookies.get('userID');
    if (currentID) {
      this.populateUserInfo(currentID)
    }

    document.getElementById('card-keywords').addEventListener('keyup', e => {
      this.updateCursorPosition(e)
    })

    document.getElementById('card-keywords').addEventListener('click', e => {
      this.updateCursorPosition(e)
    })
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
      for (let i = 0; i < strings.length; i++ ) {
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

<style >

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
  
  #autofill-container {
    position: relative;
  }

  #card-keywords::-ms-expand {
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

</style>
