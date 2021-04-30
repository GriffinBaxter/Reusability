<template>
  <div>

    <!--nav bar-->
    <navbar/>

    <!--inventory container-->
    <div class="container p-5 mt-3" id="profileContainer">
      <div class="row">
        <div class="col-xl-2 mb-2">

          <div class="card text-center shadow-sm">
            <div class="card-body">

              <!--business's profile image-->
              <img class="rounded-circle img-fluid" :src="require('../../public/sample_profile_image.jpg')"
                   alt="Profile Image"/>

              <!--business's name-->
              <div class="mt-3">
                <h5>{{ name }}</h5>
                <div class="text-secondary">{{ description }}</div>
              </div>
            </div>
            <!--            <div class="card text-center shadow-sm-3">-->
            <!--              <div class="card-body">-->
            <!--                <button class="btn btn-lg text-secondary" id="editProfileButton">Edit Profile</button>-->
            <!--              </div>-->
            <!--            </div>-->
          </div>

        </div>

        <div class="col">
          <div class="card card-body">
            <h2 align="center">Inventory</h2>

            <hr/>

            <!--filter-->
            <div class="row px-2 align-content-center">
              <!--order by product name-->
              <div id="order-by-product-name" class="col py-2 header-col col-hover rounded-3 me-2 text-center"
                   tabindex="3">
                <b>Product Name</b>
              </div>

              <!--order by product id-->
              <div id="order-by-product-id" class="col py-2 header-col col-hover rounded-3 me-2 text-center"
                   tabindex="3">
                <b>Product Id</b>
              </div>

              <!--order by quantity-->
              <div id="order-by-quantity" class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3">
                <b>Quantity</b>
              </div>

              <!--order by price per item-->
              <div id="order-by-price-per-item" class="col py-2 header-col col-hover rounded-3 me-2 text-center"
                   tabindex="3">
                <b>Price Per Item</b>
              </div>

              <!--order by total price-->
              <div id="order-by-total-price" class="col py-2 header-col col-hover rounded-3 me-2 text-center"
                   tabindex="3">
                <b>Total Price</b>
              </div>
            </div>
            <div class="row px-2 py-3">
              <!--order by manufactured-->
              <div id="order-by-manufactured" class="col py-2 header-col col-hover rounded-3 me-2 text-center"
                   tabindex="3">
                <b>Manufactured</b>
              </div>

              <!--order by sell by-->
              <div id="order-by-sell-by" class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3">
                <b>Sell By</b>
              </div>

              <!--order by best before-->
              <div id="order-by-best-before" class="col py-2 header-col col-hover rounded-3 me-2 text-center"
                   tabindex="3">
                <b>Best Before</b>
              </div>

              <!--order by expires-->
              <div id="order-by-expires" class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3">
                <b>Expires</b>
              </div>
            </div>

            <!--inventory items-->
            <inventory-item
                v-for="inventory in inventories"
                v-bind:key="inventory.index"
                v-bind:image="inventory.image"
                v-bind:product-name="inventory.productName"
                v-bind:product-id="inventory.productId"
                v-bind:quantity="inventory.quantity"
                v-bind:price-per-item="inventory.pricePerItem"
                v-bind:total-price="inventory.totalPrice"
                v-bind:manufactured="inventory.manufactured"
                v-bind:sell-by="inventory.sellBy"
                v-bind:best-before="inventory.bestBefore"
                v-bind:expires="inventory.expires"/>

            <!--pagination-->
            <nav>
              <ul class="pagination justify-content-center">
                <li class="page-item">
                  <a class="page-link" href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                  </a>
                </li>
                <li class="page-item"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item">
                  <a class="page-link" href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                  </a>
                </li>
              </ul>
            </nav>

          </div>
        </div>


      </div>


      <!--footer-->

    </div>
    <Footer></Footer>
  </div>
</template>


<script>
import Footer from "@/components/Footer";
import InventoryItem from "@/components/inventoryItem";
import Navbar from "@/components/Navbar";

export default {
  components: {
    Navbar,
    InventoryItem,
    Footer
  },
  data() {
    return {
      name: "scott",
      description: "asdasdas",

      inventories: [],
      image: "../../public/apples.jpg",
      productName: "Watties Baked Beans - 420g can",
      productId: "WATT-420-BEANS",
      quantity: 4,
      pricePerItem: 6.5,
      totalPrice: 21.99,
      manufactured: "2021-04-23",
      sellBy: "2021-04-23",
      bestBefore: "2021-04-23",
      expires: "2021-04-23"
    }
  },
  methods: {},
  mounted() {
    //example
    this.inventories.push({
      index: 0,
      image: this.image,
      productName: this.productName,
      productId: this.productId,
      quantity: this.quantity,
      pricePerItem: this.pricePerItem,
      totalPrice: this.totalPrice,
      manufactured: this.manufactured,
      sellBy: this.sellBy,
      bestBefore: this.bestBefore,
      expires: this.expires
    });
    this.inventories.push({
      index: 1,
      image: this.image,
      productName: this.productName,
      productId: this.productId,
      quantity: this.quantity,
      pricePerItem: this.pricePerItem,
      totalPrice: this.totalPrice,
      manufactured: this.manufactured,
      sellBy: this.sellBy,
      bestBefore: this.bestBefore,
      expires: this.expires
    });
    this.inventories.push({
      index: 2,
      image: this.image,
      productName: this.productName,
      productId: this.productId,
      quantity: this.quantity,
      pricePerItem: this.pricePerItem,
      totalPrice: this.totalPrice,
      manufactured: this.manufactured,
      sellBy: this.sellBy,
      bestBefore: this.bestBefore,
      expires: this.expires
    });
    this.inventories.push({
      index: 3,
      image: this.image,
      productName: this.productName,
      productId: this.productId,
      quantity: this.quantity,
      pricePerItem: this.pricePerItem,
      totalPrice: this.totalPrice,
      manufactured: this.manufactured,
      sellBy: this.sellBy,
      bestBefore: this.bestBefore,
      expires: this.expires
    });
    this.inventories.push({
      index: 4,
      image: this.image,
      productName: this.productName,
      productId: this.productId,
      quantity: this.quantity,
      pricePerItem: this.pricePerItem,
      totalPrice: this.totalPrice,
      manufactured: this.manufactured,
      sellBy: this.sellBy,
      bestBefore: this.bestBefore,
      expires: this.expires
    });
  }
}
</script>


<style scoped>

</style>