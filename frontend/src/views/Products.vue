<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-3 m-3">
        <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
      </div>
    </div>

    <button id="create-product-button" type="button" class="btn btn-lg btn-primary float-lg-end" @click="showModal">Create Product</button>

    <CreateProductModal v-show="isModalVisible" @close="closeModal">
    </CreateProductModal>

  </div>
</template>

<script>
import CreateProductModal from "@/components/CreateProductModal";

export default {
  name: 'Products',
  components: {
    CreateProductModal,
  },
  data() {
    return {
      isModalVisible: false,
    };
  },
  methods: {
    showModal() {
      this.isModalVisible = true;
    },
    closeModal() {
      this.isModalVisible = false;
    }
  }
};
</script>

<style scoped>

#create-product-button {
  background-color: #1EBA8C;
  border-color: #1EBA8C;
}

#create-product-button:hover {
  background-color: transparent;
  color: #1EBA8C;
}

/*------------------ Hide arrows from input numbers ---------------------*/
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

input:focus, textarea:focus, #create-product-button:focus{
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #1EBA8C; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
/*------------------------------------------------------------------------*/

</style>