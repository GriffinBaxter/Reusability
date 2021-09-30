<template>

  <div class="btn-toolbar w-100 align-items-end" role="toolbar" aria-label="Toolbar with button groups">
    <div class="btn-group me-2" role="group" aria-label="First group">
      <!------------------------------------------ ordering by options menu ------------------------------------------->
      <div class="btn-group col" role="group">
        <button type="button" class="btn green-button dropdown-toggle order-by-options-btn"
                data-bs-toggle="dropdown" aria-expanded="false">{{ orderByOption }}
        </button>

        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <!--order by title-->
          <li class="btn green-button-transparent col-12 order-by-options-btn"
                  @click="setOrderByOption(true, false, false)">
            Title
          </li>

          <!--order by location-->
          <li class="btn green-button-transparent col-12 order-by-options-btn"
                  @click="setOrderByOption(false, true, false)">
            Location
          </li>

          <!--order by created-->
          <li class="btn green-button-transparent col-12 order-by-options-btn"
                  @click="setOrderByOption(false, false, true)">
            Created
          </li>
        </ul>
      </div>

      <!---------------------------------------- ordering direction options menu -------------------------------------->
      <div class="btn-group col" role="group">
        <button type="button" class="btn green-button dropdown-toggle order-direction-options-btn"
                data-bs-toggle="dropdown" aria-expanded="false">{{ orderDirectionOption }}
        </button>

        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <!--order by ascending direction -->
          <li class="btn green-button-transparent col-12 order-direction-options-btn"
                  @click="setOrderDirectionOption(true)">
            Ascending
          </li>

          <!--order by descending direction -->
          <li class="btn green-button-transparent col-12 order-direction-options-btn"
                  @click="setOrderDirectionOption(false)">
            Descending
          </li>
        </ul>
      </div>
    </div>

    <!--------------------------------------- create card button ------------------------------------------------------>
    <div class="btn-group me-2" role="group" aria-label="Second group" style="margin-top: 6px">
      <CreateCardModal @new-card-created="(e) => $emit('new-card-created', e)" v-bind:type="type"></CreateCardModal>
    </div>

    <div class="float-end ms-auto">
      <PageSize :current-page-size="pageSize" :page-sizes="pageSizes" v-on:selectedPageSize="updatePageSize"></PageSize>
    </div>

  </div>

</template>

<script>

import CreateCardModal from "./CreateCardModal";
import PageSize from "../../components/PageSize";

export default {
  name: "OrderingOptionsMenu",
  components: {
    PageSize,
    CreateCardModal
  },
  data() {
    return {
      orderByOption: "Select Order By",         // default
      orderDirectionOption: "Select Direction",  // default
      orderBy: this.$route.query["orderBy"] || "dateDESC", // gets orderBy from URL or (if not there) sets to default

      pageSizes: ["6", "12", "18"], // a list of page size options
      pageSize: this.$route.query["pageSize"] || "6" // default page size
    }
  },
  props: {
    type: {
      type: String,
      required: true
    },
  },
  methods: {

    /**
     * Sets the order by option
     */
    setOrderByOption(title, location, created) {
      if (title) {
        this.orderByOption = "Title"
      } else if (location) {
        this.orderByOption = "Location"
      } else if (created) {
        this.orderByOption = "Created"
      }
      this.orderCards();
    },
    /**
     * Sets the order by direction
     */
    setOrderDirectionOption(ascending) {
      if (ascending) {
        this.orderDirectionOption = "Ascending"
      } else {
        this.orderDirectionOption = "Descending"
      }
      this.orderCards();
    },

    /**
     * Builds the order by value that will be sent to the backend to order the cards by
     */
    createOrderByParams() {
      const direction = (this.orderDirectionOption === "Ascending") ? "ASC" : "DESC"

      let orderByOptionString = this.orderByOption.toLocaleLowerCase();
      if (this.orderByOption === "Select Order By") {
        orderByOptionString = "created"
      }

      return `${orderByOptionString}${direction}`
    },

    /**
     * Order the cards
     */
    orderCards() {
      const order = this.createOrderByParams()

      // Checks the orderBy has changed to prevent NavigationDuplicated Errors
      if (order !== this.orderBy) {
        this.orderBy = order;
        this.$parent.$emit("orderedCards", this.orderBy)
      }

      // now can use this.orderBy to request cards from backend

    },

    /**
     * When a user selects a page size using the PageSize component then the current page size should be
     * updated and the results should be retrieved from the backend.
     * @param selectedPageSize the newly selected page size.
     */
    updatePageSize(selectedPageSize) {
      this.pageSize = selectedPageSize;
      this.$parent.$emit("selectedPageSize", this.pageSize);
    }
  }
}
</script>

<style scoped>

.order-by-options-btn, .order-direction-options-btn {
  width: 150px;
  margin-top: 6px;
}

.dropdown-menu {
  margin-top: 0;
  border: none;
  width: 200px;
}

</style>
