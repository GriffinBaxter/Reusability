<template>

  <div id="ordering-options-menu" class="row">

    <!------------------------------------------- ordering by options menu ----------------------------------------->

    <div id="ordering-option-container" class="col">


      <div class="btn-group col" role="group">

<!--        <label id="order-by-options-text" class="">Order By</label>-->
        <button type="button" class="btn green-button dropdown-toggle order-by-options-btn"
                data-bs-toggle="dropdown" aria-expanded="false">{{orderByOption}}
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
    </div>

    <!------------------------------------------- ordering direction options menu ------------------------------------->

    <div id="ordering-direction-container" class="col">

      <div class="btn-group col" role="group">
<!--        <label id="order-direction-options-text">Order Direction</label>-->
        <button type="button" class="btn green-button dropdown-toggle order-direction-options-btn"
                data-bs-toggle="dropdown" aria-expanded="false">{{orderDirectionOption}}
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

    <!------------------------------------------- order button -------------------------------------------------------->

    <div id="order-by-btn" class="col">
      <button type="button" class="btn green-button-transparent col-12"
              @click="orderCards()">
        Order Cards
      </button>
    </div>

    <!------------------------------------- create card button -------------------------------------------------------->

    <div id="create-card-btn" class="col">
      <CreateCardModal @new-card-created="(e) => $emit('new-card-created', e)"></CreateCardModal>
    </div>

  </div>

</template>

<script>

import CreateCardModal from "@/components/CreateCardModal";

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
      if(title) {
        this.orderByOption = "Title"
      } else if(location) {
        this.orderByOption = "Location"
      }
    },
    /**
     * Sets the order by direction
     */
    setOrderDirectionOption(ascending) {
      if(ascending) {
        this.orderDirectionOption = "Ascending"
      } else {
        this.orderDirectionOption = "Descending"
      }
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

#ordering-option-container, #ordering-direction-container {
  max-width: 20%;
}

#order-by-btn, #create-card-btn {
  max-width: 20%;
  height: 40px;
}

.order-by-options-btn, .order-direction-options-btn {
  width: 200px;
  height: 40px;
  margin: 0
}

.dropdown-menu {
  margin-top: 0;
  border: none;
  width: 200px;
}

</style>