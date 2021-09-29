<template>
  <div class="float-end">

  <label for="page-size-button" class="py-3">
    Page Size:
  </label>

  <div class="btn-group col d-inline-block p-2" role="group">

    <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
            id="page-size-button"
            data-bs-toggle="dropdown" aria-expanded="false">{{ selectedPageSize }}
    </button>

    <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
      <li>
        <div class="btn green-button-transparent col-12 order-by-options-btn"
             v-for="size in pageSizes" v-bind:key="size"
             @click="selectedPageSize = size; emitPageSize()">
          {{ size }}
        </div>
      </li>
    </ul>

  </div>

  </div>
</template>

<script>
export default {
  name: "PageSize",
  props: {
    pageSizes: {
      type: Array,
      default: () => [5, 10, 15],
      required: true
    }
  },
  data()  {
    return {
      // the page size the user has selected.
      selectedPageSize: "",
    }
  },
  methods: {
    /**
     * When a user has selected a page size this size should be emitted so that other components/pages can
     * adjust their page sizes accordingly.
     */
    emitPageSize() {
      this.$emit('selectedPageSize', this.selectedPageSize);
    }
  }
}
</script>

<style scoped>

</style>