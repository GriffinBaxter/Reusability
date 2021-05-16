<template>
  <div>
    <div class="container py-5">

      <!-- First Row [Exchange Items]-->
      <h2 class="font-weight-bold mb-2">Exchange Items</h2>
      <p class="font-italic text-muted mb-4">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
        tempor incididunt.</p>

      <div class="row pb-5 mb-4">
        <!-- Card-->
        <div class="col-lg-3 col-md-6 mb-4 mb-lg-0"
             style="padding: 12px"
             v-for="card in cards"
             v-bind:key="card.index">
          <div type="button"
               @click="selectACard(card.index)"
               data-bs-toggle="modal"
               data-bs-target="#cardDetailPopUp">
            <ExchangeCard v-bind:index="card.index"/>
          </div>
        </div>
      </div>

    </div>
  </div>

</template>

<script>
import ExchangeCard from './ExchangeCard'

export default {
  name: "Exchange",
  data() {
    return {
      cards: [],
      selectedCard: 0
    }
  },
  components: {
    ExchangeCard,
  },
  methods: {
    selectACard(index) {
      this.$emit('openCardDetail', index);
      this.selectedCard = index
    }
  },
  mounted() {
    this.cards.push({index: 1})
    this.cards.push({index: 2})
    this.cards.push({index: 3})
    this.cards.push({index: 4})
    this.cards.push({index: 5})
    this.cards.push({index: 6})
    this.cards.push({index: 7})
    this.cards.push({index: 8})

  }
}
</script>

<style scoped>

</style>
