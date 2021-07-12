<template>

  <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
    <div class="btn-group me-2" role="group" aria-label="First group" style="">
      <!------------------------------------------ ordering by options menu ------------------------------------------->
      <div class="btn-group col" role="group">
        <button type="button" class="btn green-button dropdown-toggle order-by-options-btn"
                data-bs-toggle="dropdown" aria-expanded="false">{{ orderByOption }}
        </button>

        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <!--order by title-->
          <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                  @click="setOrderByOption(true, false)">
            Title
          </button>

          <!--order by location-->
          <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                  @click="setOrderByOption(false, true)">
            Location
          </button>
        </ul>
      </div>

      <!---------------------------------------- ordering direction options menu -------------------------------------->
      <div class="btn-group col" role="group">
        <button type="button" class="btn green-button dropdown-toggle order-direction-options-btn"
                data-bs-toggle="dropdown" aria-expanded="false">{{ orderDirectionOption }}
        </button>

        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <!--order by ascending direction -->
          <button type="button" class="btn green-button-transparent col-12 order-direction-options-btn"
                  @click="setOrderDirectionOption(true)">
            Ascending
          </button>

          <!--order by descending direction -->
          <button type="button" class="btn green-button-transparent col-12 order-direction-options-btn"
                  @click="setOrderDirectionOption(false)">
            Descending
          </button>
        </ul>
      </div>
    </div>

    <!--------------------------------------- create card button ------------------------------------------------------>
    <div class="btn-group me-2" role="group" aria-label="Second group" style="margin-top: 6px">
      <CreateCardModal @new-card-created="(e) => $emit('new-card-created', e)"></CreateCardModal>
    </div>
  </div>

</template>

<script>

import CreateCardModal from "../CreateCardModal";

export default {
  name: "OrderingOptionsMenu",
  components: {
    CreateCardModal
  },
  data() {
    return {
      orderByOption: "Select Order By",         // default
      orderDirectionOption: "Select Direction",  // default
      orderBy: "dateDESC",
    }
  },
  methods: {

    /**
     * Sets the order by option
     */
    setOrderByOption(title, location) {
      if (title) {
        this.orderByOption = "Title"
      } else if (location) {
        this.orderByOption = "Location"
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

      this.orderBy = `${orderByOptionString}${direction}`
    },

    /**
     * Order the cards
     */
    orderCards() {
      this.createOrderByParams()

      this.$parent.$emit("orderedCards", this.orderBy)

      // now can use this.orderBy to request cards from backend

    },
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