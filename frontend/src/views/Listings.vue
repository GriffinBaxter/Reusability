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
            v-bind:description="item.description"
            v-bind:product-id="item.productId"
            v-bind:quantity="item.quantity"
            v-bind:quantityPerSale="item.quantityPerSale"
            v-bind:price="item.price"
            v-bind:listDate="item.listDate"
            v-bind:close-date="item.closeDate"
            v-bind:best-before="item.bestBefore"
            v-bind:expires="item.expires"
            v-bind:moreInfo="item.moreInfo"/>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import ListingItem from "@/components/ListingItem";
import Api from "@/Api";
export default {
name: "Listings",
  components: {ListingItem, Navbar},
  data() {
    return {
      allListings: [],
      listings: []
    }
  },
  methods: {
    getListings(id) {
      /*
      Attempts to get listings from backend
      If successful, sends data to populatePage()
      If not, redirects to appropriate page
      */
      Api.getBusinessListings(id).then(response => (this.populatePage(response.data))).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noBusiness'});
        } else {
          this.$router.push({path: '/noBusiness'});
          console.log(error.message);
        }
      })
    },
    populatePage(data) {
      for (let i=0; i<data.length; i++) {
        this.listings.push({
          productName: data[i].inventoryItem.product.name,
          image: data[i].inventoryItem.product.images.filename,
          productId: data[i].inventoryItem.product.id,
          quantity: data[i].quantity,
          price: data[i].price,
          listDate: data[i].created,
          closeDate: data[i].closes
        })
      }
    },
    fakeListings() {
      this.listings.push({
        productName: 'Beans',
        image: '../../public/cans.jpg',
        productId: 'WATT-420-BEANS',
        description: 'Watties baked beanz is natures super food. 99% fat free, high in protein, source of iron and a great source of dietary fibre. Watties baked beans are low gi, giving you long-lasting energy to keep you going for longer. Proudly made in nz.',
        quantityPerSale: 4,
        quantity: 3,
        price: 17.99,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021',
        moreInfo: 'Seller may be willing to consider near offers'
      })
      this.listings.push({
        productName: 'Apples',
        image: '../../public/apples.jpg',
        productId: 'APPLES',
        quantityPerSale: 2,
        quantity: 5,
        price: 5,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021'
      })
    }
  },
  mounted() {
    // const businessId = this.$route.params.id;
    this.fakeListings();
    // this.getListings(businessId); //NOTE: Currently not working
  }
}
</script>

<style scoped>

</style>