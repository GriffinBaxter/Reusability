<template>
  <div>
    <Navbar/>
    <div class="container">
      <h1>Listings</h1>
      <div class="card">
        <div class="row">
          <div class="col-md-9">
            Orderings..
          </div>
          <div class="col">
            <button type="button" class="btn btn-success">Add new</button>
          </div>
        </div>
        <ListingItem
            v-for="item in listings"
            v-bind:key="item.index"
            v-bind:image="item.image"
            v-bind:product-name="item.productName"
            v-bind:product-id="item.productId"
            v-bind:quantity="item.quantity"
            v-bind:price="item.price"
            v-bind:listDate="item.listDate"
            v-bind:close-date="item.closeDate"
            v-bind:best-before="item.bestBefore"
            v-bind:expires="item.expires"/>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import ListingItem from "@/components/ListingItem";
export default {
name: "Listings",
  components: {ListingItem, Navbar},
  data() {
    return {
      listings: []
    }
  },
  methods: {
    fakeListings() {
      this.listings.push({
        productName: 'Beans',
        image: '../../public/cans.jpg',
        productId: 'WATT-420-BEANS',
        quantity: 5,
        price: 6.5,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021'
      })
      this.listings.push({
        productName: 'Apples',
        image: '../../public/apples.jpg',
        productId: 'APPLES',
        quantity: 5,
        price: 3,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021'
      })
    }
  },
  mounted() {
    this.fakeListings();
  }
}
</script>

<style scoped>

</style>