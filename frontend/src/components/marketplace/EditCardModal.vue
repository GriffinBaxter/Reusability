<template>
  <div>
    <!-- Edit Card Modal -->
    <div class="modal fade" ref="editCardModal" id="edit-card-modal" tabindex="0">
      <div class="modal-dialog">
        <div class="modal-content">

          <!-- Modal header-->
          <div class="modal-header">
            <h5 class="modal-title">Edit Card</h5>
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
                  <label for="section-selection" class="form-label">Section*:</label>
                </div>
                <div class="col-md-6 my-2 my-lg-0">
                  <select id="section-selection" name="section-selection" :class="`form-select ${formErrorClasses.sectionSelectionError}`"
                          v-model="sectionSelected">
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
                  <div id="card-keywords-invalid-feedback" class="invalid-feedback" v-if="formError.keywordsError">
                    {{formError.keywordsError}}
                  </div>
                </div>
              </div>

            </form>
          </div>

          <!-- Modal footer -->
          <div class="modal-footer">
            <button id="edit-card-button" type="button" class="btn green-button order-1" >Save</button>
            <button type="button" class="btn btn-secondary order-0" data-bs-dismiss="modal">Cancel</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {Modal} from 'bootstrap';
import cardConfig from "../../configs/MarketplaceCard"
import Cookies from "js-cookie";
import Api from "../../Api";

export default {
  name: "EditCardModal",
  data() {
    return {
      modal: null,

      /** For api error we display the error through this model error */
      modalError: "",

      /** */
      config: cardConfig,
      /** Keep one string to contain the invalid class mark*/
      isInvalidClass: "is-invalid",

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

      userId: null,
      id: null,

      title: "",
      description: "",
      keywordsInput: "",
      keywordsBackdrop: "",

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
      }
    }
  },
  methods: {
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
     * Gets current state of the selected card from the API
     */
    async getCurrentData() {
      await Api.getDetailForACard(this.id).then(response => (this.populateData(response.data))).catch((error) => {
        console.log(error.message);
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
     * Ensures when the table scrolls that they remain at the same height.
     * */
    handleKeywordsScroll() {
      this.$refs.keywordsBackdrop.scrollTop = this.$refs.keywordsInput.scrollTop;
      this.$refs.keywordsInput.scrollTop = this.$refs.keywordsBackdrop.scrollTop;
    },
    /**
     * Shows the card edit modal
     * */
    showModal(id) {

      this.id = id

      this.getCurrentData()

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
    }
  },
  mounted() {
    this.modal = new Modal(this.$refs.editCardModal);

    this.userId = Cookies.get('userID');

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

</style>
