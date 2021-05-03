<template>
  <div>
    <!-- Navbar -->
    <Navbar/>
    <!-- Listing Creation -->
    <create-listing v-bind:business-id="businessId"></create-listing>
    <!-- Listing Container -->
    <div class="container">
      <h1 id="pageTitle">{{ businessName }}'s Listings</h1>
      <div class="card p-1">
        <!-- Order Buttons -->
        <div class="row my-3" align="center">
          <div class="col-md">
            <button type="button" class="btn btn-outline-success w-75 my-1">New Listings</button>
          </div>
          <div class="col-md">
            <button type="button" class="btn btn-outline-success w-75 my-1">Closing Soon</button>
          </div>
          <div class="col-md">
            <button type="button" class="btn btn-outline-success w-75 my-1">Name</button>
          </div>
          <!-- Add new Button -->
          <div class="col-md" v-if="businessAdmin">
            <button type="button" class="btn btn-success w-75 my-1" data-bs-toggle="modal" data-bs-target="#listingCreationPopup">Add new</button>
          </div>
        </div>
        <!-- Listings -->
        <ListingItem
            v-for="item in listings"
            v-bind:key="item.index"
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
    <!-- Footer -->
    <Footer/>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import ListingItem from "@/components/ListingItem";
import Api from "@/Api";
import Cookies from "js-cookie";
import CreateListing from "@/components/CreateListing";
import Footer from "@/components/Footer";

export default {
name: "Listings",
  components: {Footer, CreateListing, ListingItem, Navbar},
  data() {
    return {
      allListings: [],
      listings: [],
      businessName: "",
      businessAdmin: false,
      businessId: -1
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
    getBusiness(id) {
      Api.getBusiness(id).then(response => (this.getBusinessData(response.data))).catch((error) => {
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
    getBusinessData(data) {
      this.businessName = data.name;
      // Checks if user is acting as business
      const actAs = Cookies.get('actAs');
      this.businessAdmin = actAs === String(data.id);
    },
    populatePage(data) {
      if (data.length === 0) {
        console.log('No listings')
      }
      for (let i=0; i < data.length; i++) {
        this.listings.push({
          productName: data[i].inventoryItem.product.name,
          description: data[i].inventoryItem.product.description,
          productId: data[i].inventoryItem.product.id,
          quantityPerSale: data[i].inventoryItem.quantity,
          quantity: data[i].quantity,
          price: data[i].price,
          listDate: data[i].created,
          closeDate: data[i].closes,
          moreInfo: data[i].moreInfo
        })
      }
    },
    fakeListings() {
      this.listings.push({
        productName: 'Beans',
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
        productId: 'APPLES',
        quantityPerSale: 2,
        quantity: 5,
        price: 5,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021'
      })
      this.listings.push({
        productName: 'XXX',
        productId: 'XXX',
        description: 'Watties baked beanz is natures super food. 99% fat free, high in protein, source of iron and a great source of dietary fibre. Watties baked beans are low gi, giving you long-lasting energy to keep you going for longer. Proudly made in nz.',
        quantityPerSale: 4,
        quantity: 3,
        price: 17.99,
        listDate: '28/4/2021',
        closeDate: '10/5/2021',
        expires: '24/5/2021',
        moreInfo: ''
      })
      this.listings.push({
        productName: 'YYY',
        productId: 'UHHHH',
        description: "",
        quantityPerSale: 4,
        quantity: 3,
        price: 17.99,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021',
        moreInfo: 'Seller may be willing to consider near offers'
      })
    }
  },
  mounted() {
    this.businessId = parseInt(this.$route.params.id);
    this.getBusiness(this.businessId);
    this.fakeListings();
    // this.getListings(this.businessId); //NOTE: Currently not working
  }
}
</script>

<style scoped>

#pageTitle {
  padding: 10px;
  text-align: center;
}

</style>