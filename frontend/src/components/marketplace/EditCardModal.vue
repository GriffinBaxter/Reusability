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
                         :maxlength="config.config.title.maxLength">
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
                            :maxlength="config.config.description.maxLength"/>
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
                    <textarea ref="keywordsInput" id="card-keywords" class="form-control keywords-input " style="resize: none; overflow-y: scroll; " v-model="keywordsInput"/>
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
     * Shows the card edit modal
     * */
    showModal(id) {

      this.id = id

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
