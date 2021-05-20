<template>

  <div id="ordering-options-menu">

    <div id="ordering-option-container">

      <div class="btn-group col-md-3 py-1" role="group">
        Order By Option
        <button type="button" class="btn green-button dropdown-toggle"
                data-bs-toggle="dropdown" aria-expanded="false">{{orderByOption}}
        </button>

        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <!--order by title-->
          <button type="button" class="btn green-button-transparent col-12"
                  @click="setOrderByOption(true, false)">
            Title
          </button>

          <!--order by location-->
          <button type="button" class="btn green-button-transparent col-12"
                  @click="setOrderByOption(false, true)">
            Location
          </button>
        </ul>
      </div>
    </div>

    <div id="ordering-direction-container">

      <div class="btn-group col-md-3 py-1" role="group">
        Order Direction
        <button type="button" class="btn green-button dropdown-toggle"
                data-bs-toggle="dropdown" aria-expanded="false">{{orderDirectionOption}}
        </button>

        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <!--order by ascending direction -->
          <button type="button" class="btn green-button-transparent col-12"
                  @click="setOrderDirectionOption(true)">
            Ascending
          </button>

          <!--order by descending direction -->
          <button type="button" class="btn green-button-transparent col-12"
                  @click="setOrderDirectionOption(false)">
            Descending
          </button>
        </ul>
      </div>
    </div>

    <button type="button" class="btn green-button-transparent col-12"
            @click="orderCards()">
      Order Cards
    </button>


  </div>

</template>

<script>
export default {
  name: "OrderingOptionsMenu",
  data() {
    return {
      orderByOption: "",         // default
      orderDirectionOption: "",  // default
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
      this.orderBy = `${this.orderByOption.toLocaleLowerCase()}${direction}`
    },

    /**
     * Order the cards
     */
    orderCards() {
      this.createOrderByParams()

      // now can use this.orderBy to request cards from backend

    },
  }
}
</script>

<style scoped>

</style>