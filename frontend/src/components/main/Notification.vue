<template>
  <div class="accordion" id="accordionExample" style="width: 500px">
    <div class="accordion-item"
         v-for="card in allNoticeCards"
         v-bind:key="card.id">
      <h2 class="accordion-header" :id="'heading_' + card.id">
        <button class="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                :data-bs-target="'#collapse_' + card.id"
                aria-expanded="false"
                :aria-controls="'collapse_' + card.id">
          Your card ({{ card.title }}) will be expiry in {{ card.date }}.
        </button>
      </h2>
      <div :id="'collapse_' + card.id" class="accordion-collapse collapse" :aria-labelledby="'heading_' + card.id"
           data-bs-parent="#accordionExample">
        <div class="accordion-body">
          <strong>More info and button here</strong>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "Notification",
  data() {
    return {
      allNoticeCards: []
    }
  },
  props: {},
  function: {
  },
  beforeMount() {
    this.allNoticeCards.push({id: 1, title: "Beef", date: "2021-05-20T00:00"});
    this.allNoticeCards.push({id: 2, title: "PS5", date: "2021-05-20T00:00"});
    this.allNoticeCards.push({id: 3, title: "Rice", date: "2021-05-20T00:00"});
    this.allNoticeCards.push({id: 4, title: "Bean", date: "2021-05-20T00:00"});
  }
}
</script>

<style scoped>

</style>