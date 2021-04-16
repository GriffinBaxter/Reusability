<template>
  <transition name="modal-fade">
    <div class="modal-backdrop">
      <div class="modal" role="dialog" aria-labelledby="modalTitle" aria-describedby="modalDescription" >
        <section class="modal-body" id="modalDescription">
          <!-- create product form's container-->
          <div class="container shadow py-5 px-4 bg-white mb-5" id="create-form">
            <div class="row">
              <div class="col">
                <h3 class="display-6 m-2 mb-5 text-center">Create Product</h3>
              </div>
            </div>

            <!--create product form, needs validation-->
            <form id="create" class="needs-validation mb-3 px-5" novalidate @submit.prevent>

              <!--cancel and save buttons-->
              <div class="d-grid gap-2 d-lg-block">
                <button id="cancel-button" tabindex="2" class="btn btn-lg btn-outline-primary green-button-transparent"
                        type="button" @click="close" aria-label="Close modal"> Cancel </button>
                <button id="save-button" tabindex="1" class="btn btn-lg btn-primary float-lg-end green-button"
                        type="button" @click="close" aria-label="Close modal"> Save </button>
              </div>
            </form>
          </div>
        </section>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'Modal',
  methods: {
    close() {
      this.$emit('close');
    },
  },
};
</script>

<style>
.modal-backdrop {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: #FFFFFF;
  box-shadow: 2px 2px 20px 1px;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
}

.modal-body {
  position: relative;
  padding: 20px 10px;
}

.modal-fade-enter,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity .5s ease;
}

/*--------------------- Hide arrows from input numbers ---------------------*/
/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type=number] {
  -moz-appearance: textfield;
}

input:focus, textarea:focus, #cancel-button:focus, #save-button:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
</style>