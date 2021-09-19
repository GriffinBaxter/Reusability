<template>
  <div>
    <Navbar/>

    <main class="listing-wrapper">

      <!-- Listing header -->
      <div class="title-content">
        <div id="listing-name">{{ productName }}</div>
        <div></div>
        <div class="listing-dates-wrapper">
          <h6>Listing Date: {{ startDate }}</h6>
          <h6>Closing Date: {{ closeData }}</h6>
        </div>
      </div>
      <div id="listing-content-wrapper">
        <div class="left-content">
          <!-- Return to sales listings button-->
          <div class="return-button-wrapper mb-3 w-100" v-if="fromListings">
            <button class="btn btn-lg green-button w-100" @click="returnToSales()" id="return-button" tabindex="8">Return to Sale Listings</button>
          </div>
          <!-- Image section -->
          <div class="listing-images-wrapper">
            <div id="main-image-wrapper">
              <img :src="getMainImage()" :alt="'Product ' + productName + ' Image'" id="listing-image" class="no-highlight"/>
            </div>
            <div class="images-carousel-wrapper" v-if="saleImages.length > 1">
              <div class="carousel-arrow clickable no-highlight" @click="nextImage">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                     class="bi bi-chevron-left" viewBox="0 0 16 16">
                  <path fill-rule="evenodd"
                        d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/>
                </svg>
              </div>
              <div class="images-carousel">
                <img
                    :class="`sale-carousel-image no-highlight clickable ` + (mainImageIndex === imageIndex ? 'main-carousel-image' : '')"
                    v-for="(imageIndex, index) in getVisibleImages()" @click="mainImageIndex=imageIndex" :key="index"
                    :src="getCarouselImage(imageIndex)" :alt="'Product ' + productName + ' Image 2'">
              </div>
              <div class="carousel-arrow clickable no-highlight" @click="previousImage">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                     class="bi bi-chevron-right" viewBox="0 0 16 16">
                  <path fill-rule="evenodd"
                        d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/>
                </svg>
              </div>
            </div>
          </div>

          <!-- Business information -->
          <div class="business-wrapper">
            <h5>{{ businessName }}</h5>
            <div class="business-address-wrapper"
                 v-if="businessAddressLine1 !== null && businessAddressLine1.length > 1">
              {{ businessAddressLine1 }}
            </div>
            <div class="business-address-wrapper"
                 v-if="businessAddressLine2 !== null && businessAddressLine2.length > 0">
              {{ businessAddressLine2 }}
            </div>
            <div class="business-address-wrapper"
                 v-if="businessAddressLine3 !== null && businessAddressLine3.length > 2">
              {{ businessAddressLine3 }}
            </div>
            <div class="business-address-wrapper"
                 v-if="businessAddressLine4 !== null && businessAddressLine4.length > 2">
              {{ businessAddressLine4 }}
            </div>
            <!-- Go to business profile button -->
            <div class="goto-button-wrapper w-100">
              <button class="btn btn-lg green-button mt-2 w-100" @click="goToBusiness()" id="go-to-button">Go to Business Profile</button>
            </div>
          </div>
        </div>

        <div class="right-content">

          <!-- Buy Button -->
          <div class="buy-button-section">
            <h6 id="price" class="merryweather">
              {{ currencySymbol }}{{ price }} {{ currencyCode }}
            </h6>
            <div style="width: fit-content">
              <h6 id="bookmarks" class="merryweather" @click="changeBookmarkStatus" v-if="actingBusinessId == null">
                <svg id="marked" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                     class="bi bi-bookmark-heart-fill" viewBox="0 0 16 16" v-if="isBookmarked">
                  <path
                      d="M2 15.5a.5.5 0 0 0 .74.439L8 13.069l5.26 2.87A.5.5 0 0 0 14 15.5V2a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v13.5zM8 4.41c1.387-1.425 4.854 1.07 0 4.277C3.146 5.48 6.613 2.986 8 4.412z"/>
                </svg>
                <svg id="un-marked" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                     class="bi bi-bookmark-heart" viewBox="0 0 16 16" v-else>
                  <path fill-rule="evenodd"
                        d="M8 4.41c1.387-1.425 4.854 1.07 0 4.277C3.146 5.48 6.613 2.986 8 4.412z"/>
                  <path
                      d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z"/>
                </svg>
                {{ totalBookmarks }}
              </h6>
              <h6 v-else id="bookmarksAsBusiness" class="merryweather">
                <svg id="un-marked" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                     class="bi bi-bookmark-heart" viewBox="0 0 16 16">
                  <path fill-rule="evenodd"
                        d="M8 4.41c1.387-1.425 4.854 1.07 0 4.277C3.146 5.48 6.613 2.986 8 4.412z"/>
                  <path
                      d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z"/>
                </svg>
                {{ totalBookmarks }}
                <br>
                (Cannot bookmark as a business)
              </h6>
            </div>
            <div class="buy-button-wrapper">
              <button v-if="canBuy" class="buy-button merryweather w-100" @click="buy">
                Buy
              </button>
              <button class="delete-button btn btn-danger w-100" v-else-if="actingBusinessId === businessId"
                      @click="withdrawListingConfirmation($event)">
                Remove Listing
              </button>
              <button v-else class="buy-button-disabled merryweather w-100" disabled>
                Business cannot purchase listings.
              </button>
            </div>
              <div class="barcode-wrapper" v-if="barcode">
                <img :src="getBarcodeImage()" alt="Product Barcode Image" id="barcode-image" class="no-highlight"/>
                <div id="barcode-number">Barcode: {{ barcode }}</div>
              </div>
          </div>

          <!-- Product information -->
          <div class="product-information-wrapper">
            <div>{{ description }}</div>
            <div id="product-id-quantity-wrapper" class="product-information-space">
              <div>Product ID: {{ productId }}</div>
              <div>Quantity: {{ quantity }}</div>
            </div>
            <div class="product-information-space">
              <div v-if="manufactured">Manufactured: {{ manufactured }}</div>
              <div v-if="sellBy">Sell by: {{ sellBy }}</div>
              <div v-if="bestBefore">Best before: {{ bestBefore }}</div>
              <div>Expires: {{ expires }}</div>
              <div v-if="manufacturer">Manufacturer: {{ manufacturer }}</div>
            </div>
          </div>

        </div>
      </div>
    </main>

    <Footer/>

    <WithdrawListingConfirmationModal ref="withdrawListingConfirmationModal"
                                      :businessName="businessName"
                                      :productName="productName"
                                      :quantity="quantity.toString()"
                                      :price="price.toString()"
                                      :currencySymbol="currencySymbol"
                                      :currencyCode="currencyCode"
    />
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import Footer from "../components/main/Footer"
import DefaultImage from "../../public/default-image.jpg"
import Api from "../Api"
import {formatDate} from "../dateUtils";
import Cookies from "js-cookie";
import {checkNullity} from "../views/helpFunction";
import WithdrawListingConfirmationModal from "../components/listing/WithdrawListingConfirmationModal";

export default {
  name: "SaleListing",
  components: {
    Navbar,
    Footer,
    WithdrawListingConfirmationModal
  },
  data() {
    return {
      // product info
      productName: "",
      productId: "",
      price: 0,
      barcode: "",
      description: "",
      quantity: "",

      // image info
      mainImageIndex: 0,
      carouselStartIndex: 0,
      carouselNumImages: 3,
      saleImages: [],

      // Date info
      startDate: "",
      closeData: "",
      manufactured: "",
      sellBy: "",
      bestBefore: "",
      expires: "",
      manufacturer: "",

      // Business info
      businessId: 0,
      businessName: "",
      businessAddress: "",
      businessAddressLine1: "",
      businessAddressLine2: "",
      businessAddressLine3: "",
      businessAddressLine4: "",
      businessAdmins: [],
      currencySymbol: "",
      currencyCode: "",

      listingId: null,
      isBookmarked: null,
      totalBookmarks: 0,

      currentID: null,
      canBuy: true,

      // keep track of if user came from full listings page so they can return.
      fromListings: false,

      // CONSTANTS
      UPC_A: "upca",
      UPC_A_LENGTH: 12,
      EAN: "ean13",
      EAN_LENGTH: 13,

      actingBusinessId: null,
    }
  },
  methods: {
    withdrawListingConfirmation(event) {
      this.$refs.withdrawListingConfirmationModal.showModal(event);
    },

    deleteListing() {
      Api.deleteListing(this.listingId).then(() => {
        this.returnToSales();
      }).catch((err) => {
        if (err.response) {
          if (err.response.status === 406) {
            this.$router.push({name: "NoListing"})
          } else if (err.response.status === 403) {
            this.actingBusinessId = null
          } else if (err.response.status === 401) {
            this.$router.push({name: "InvalidToken"})
          }
        } else {
          console.log(err)
        }
      })

    },
    /**
     * Attempts to buy the viewed listing and if successful returns the user to their home page
     */
    buy() {
      Api.buyListing(this.listingId).then(() => {
        this.$router.push({name: 'Home'})
      }).catch((err) => {
        if (err.response) {
          if (err.response.status === 401) {
            this.$router.push({name: 'InvalidToken'})
          } else if (err.response.status === 403) {
            console.log(err.response.data.message)
          } else if (err.response.status === 406) {
            this.$router.push({path: '/noListing'});
          } else {
            console.log(err.response)
          }
        } else {
          console.log(err)
        }
      })
    },
    /**
     * change bookmark status
     */
    changeBookmarkStatus() {
      this.isBookmarked = !this.isBookmarked
      this.totalBookmarks = this.isBookmarked ? this.totalBookmarks + 1 : this.totalBookmarks - 1;
      Api.changeStatusOfAListing(this.listingId)
          .catch(() => {
            this.isBookmarked = !this.isBookmarked
            this.totalBookmarks = this.isBookmarked ? this.totalBookmarks - 1 : this.totalBookmarks + 1;
          })
    },
    /**
     * Retrieves the filename (url path) for the currently chosen image for the listing.
     *
     * @return {string} Returns the URL to the image source (either backend or default image if not found).
     */
    getMainImage() {
      if (this.mainImageIndex < this.saleImages.length && this.mainImageIndex >= 0 && this.saleImages[this.mainImageIndex].filename) {
        return Api.getServerResourcePath(this.saleImages[this.mainImageIndex].filename);
      }
      return DefaultImage;
    },
    /**
     * Retrieves the carousel (thumbnail) image for a given index.
     *
     * @param index Index of the image in the salesImages array.
     * @return {string} Returns the URL to the image source (either backend or default image if not found).
     */
    getCarouselImage(index) {
      if (this.saleImages.length > index && index >= 0 && this.saleImages[index].thumbnailFilename) {
        return Api.getServerResourcePath(this.saleImages[index].thumbnailFilename);
      }
      return DefaultImage;
    },
    /**
     * Retrieves the list of image indexes visible.
     *
     * @return {number[]} Returns a list of the images currently visible in the carousel (3 images)
     */
    getVisibleImages() {
      let visibleImageIndices = [];
      for (let i = this.carouselStartIndex; i < this.carouselStartIndex + this.carouselNumImages; i++) {
        visibleImageIndices.push(this.boundIndex(i, this.saleImages.length));
      }
      return visibleImageIndices;
    },
    /**
     * Bounds an index to the possible values within the given max length.
     *
     * @param index Index we want to bound.
     * @param maxLength Max length of the list.
     * @return {number} Returns the index within the bounds.
     */
    boundIndex(index, maxLength) {
      let newIndex = index;
      while (newIndex < 0) {
        newIndex += maxLength;
      }
      return newIndex % maxLength;
    },
    /**
     * Goes to the next image on the carousel.
     */
    nextImage() {
      if (this.saleImages.length < this.carouselNumImages) return;
      this.carouselStartIndex = this.boundIndex(this.carouselStartIndex + 1, this.saleImages.length);
    },
    /**
     * Goes to the previous image on the carousel.
     */
    previousImage() {
      if (this.saleImages.length < this.carouselNumImages) return;
      this.carouselStartIndex = this.boundIndex(this.carouselStartIndex - 1, this.saleImages.length);
    },
    /**
     * populate data to each individual value
     * @param data data
     */
    populateData(data) {
      // product info
      this.productName = data.inventoryItem.product.name;
      this.productId = data.inventoryItem.product.id;
      this.price = data.price;
      this.barcode = data.inventoryItem.product.barcode;
      this.description = data.inventoryItem.product.description;
      this.quantity = data.quantity;

      // image info
      this.saleImages = data.inventoryItem.product.images;
      this.carouselNumImages = data.inventoryItem.product.images.length;
      this.mainImageIndex = this.saleImages.findIndex((value) => value.isPrimary);
      this.carouselStartIndex = this.saleImages.findIndex((value) => value.isPrimary);

      // Date info
      this.startDate = formatDate(data.created, false);
      this.closeData = formatDate(data.closes, false);
      this.manufactured = data.inventoryItem.manufactured;
      this.sellBy = data.inventoryItem.sellBy;
      this.bestBefore = data.inventoryItem.bestBefore;
      this.expires = data.inventoryItem.expires;
      this.manufacturer = data.inventoryItem.product.manufacturer;

      // Business info
      this.businessId = data.inventoryItem.product.business.id;
      this.businessName = data.inventoryItem.product.business.name;
      this.businessAddress = data.inventoryItem.product.business.address;
      this.currencySymbol = (data.inventoryItem.product.business.currencySymbol === null ||
                            data.inventoryItem.product.business.currencySymbol === "") ? "$" : data.inventoryItem.product.business.currencySymbol;
      this.currencyCode = data.inventoryItem.product.business.currencyCode;

      // Administrators
      this.businessAdmins = data.inventoryItem.product.business.administrators;

      // Testing that we are acting as a user.
      this.canBuy = this.actingBusinessId === undefined || this.actingBusinessId === null;

      // address population
      if (this.businessAddress.streetNumber !== null && this.businessAddress.streetName !== null) {
        this.businessAddressLine1 = this.businessAddress.streetNumber + " " + this.businessAddress.streetName;
      } else {
        this.businessAddressLine1 = checkNullity(this.businessAddress.streetNumber) + checkNullity(this.businessAddress.streetName);
      }
      if (this.businessAddress.suburb !== "") {
        this.businessAddressLine2 =  this.businessAddress.suburb;
      }
      if (this.businessAddress.city !== null && this.businessAddress.postcode !== null) {
        this.businessAddressLine3 = this.businessAddress.city + ", " + this.businessAddress.postcode;
      } else {
        this.businessAddressLine3 = checkNullity(this.businessAddress.city) + checkNullity(this.businessAddress.postcode);
      }
      if (this.businessAddress.region !== null && this.businessAddress.country !== null) {
        this.businessAddressLine4 = this.businessAddress.region + ", " + this.businessAddress.country;
      } else {
        this.businessAddressLine4 = checkNullity(this.businessAddress.region) + checkNullity(this.businessAddress.country);
      }

      // bookmark info
      this.listingId = data.id;
      this.isBookmarked = data.isBookmarked;
      this.totalBookmarks = data.totalBookmarks;
    },
    /**
     * Redirect the user to the page for the profile of the business who listed the current sale.
     */
    goToBusiness() {
      const businessId = this.$route.params.businessId;
      this.$router.push({
        path: `/businessProfile/${businessId}`
      });
    },
    /**
     * Redirect the user back to the full sale listings page.
     */
    returnToSales() {
      this.$router.back();
    },
    /**
     * Retrieves the filename (url path) for the barcode image for the listing.
     * This is retrieved via an api bwip-js.
     *
     * Preconditions:  this.barcode is not null (i.e. this method is only called when this.barcode exists).
     *                 this.barcode represents a valid UPCA or EAN13 barcode.
     * Postconditions: a url which can be used to retrieve a barcode image.
     *
     * @return {string} Returns the URL to the image source.
     */
    getBarcodeImage() {
      let type;
      if (this.barcode.length === this.UPC_A_LENGTH) {
        type = this.UPC_A;
      } else if (this.barcode.length === this.EAN_LENGTH) {
        type = this.EAN;
      } else {
        return ""; // barcode is unsupported so we can't retrieve barcode image.
      }
      // return the url which can be used to retrieve the barcode image.
      return "https://bwipjs-api.metafloor.com/?bcid=" + type + "&text=" + this.barcode;
    },
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      // If the user has come from a page which contains listings then the return to listings button component
      // should be rendered.
      if (from.name === 'BrowseListings' || from.name === 'BusinessProfile') {
        vm.fromListings = true;
      }
      next();
    });
  },
  beforeMount() {
    const url = document.URL;
    const businessId = url.substring(url.lastIndexOf('/businessProfile/') + 17, url.lastIndexOf('/listings/'));
    const listingId = url.substring(url.lastIndexOf('/') + 1);
    const self = this;
    this.currentID = Cookies.get('userID');
    this.actingBusinessId = parseInt(Cookies.get("actAs"));

    Api.getDetailForAListing(businessId, listingId)
        .then(response => this.populateData(response.data))
        .catch(error => {
          self.$router.push({path: '/noListing'});
          console.log(error);
        });
  }
}
</script>

<style scoped>
h5, h1 {
  font-family: 'Merriweather Sans', sans-serif;
}

.clickable {
  cursor: pointer;
}

#listing-name {
  font-size: 3rem;
  margin-left: -0.18rem;
  font-family: 'Merriweather Sans', sans-serif;
}

.merryweather {
  font-family: 'Merriweather Sans', sans-serif;
}

h6 {
  margin: 0;
}

.listing-wrapper {
  padding: 1.5em;
}

#listing-content-wrapper {
  display: grid;
  grid-template-columns: 1fr;
  grid-row-gap: 2.5em;
}

.images-carousel-wrapper {
  display: none;
}

.no-highlight::-moz-selection {
  background-color: transparent;
}

.no-highlight::selection {
  background-color: transparent;
}

.listing-images-wrapper {
  margin-top: 1.25em;
}

.listing-dates-wrapper {
  display: flex;
  justify-content: left;
  flex-direction: column;
}

#listing-image {
  max-width: 100%;
  max-height: 100%;
}

#main-image-wrapper {
  max-width: 530px;
  max-height: 530px;
  overflow: auto;
  border: 1px #2c2c2c solid;
  background-color: rgba(125, 125, 125, 0.1);
  display: flex;
  justify-content: center;
  align-items: center;
}

#price {
  font-weight: bold;
  font-size: 2.5rem;
}

#bookmarks {
  font-weight: bold;
  cursor: pointer;
  font-size: 1.5rem;
  margin-top: 0.4em;
}

#bookmarksAsBusiness {
  font-weight: bold;
  font-size: 1.5rem;
  margin-top: 0.4em;
}

#bookmarks:hover {
  color: #00d9a9;
  fill: #00d9a9;
}

.business-wrapper {
  margin-top: 1.25em;
}

.buy-button-section {
  margin-top: 2rem;
  width: 100%;
}

.buy-button-disabled {
  display: flex;
  justify-content: center;
  align-items: center;

  background-color: lightgray;
  border: 1px solid #19b092;
  color: #19b092;

  padding: 0.65em 0;
  margin: 1.45em 0;
  border-radius: 0.25em;

  cursor: pointer;
  transition: 150ms ease-in-out;

  font-size: 1.5em;
}

.delete-button {
  display: flex;
  justify-content: center;
  align-items: center;

  padding: 0.65em 0;
  margin: 1.45em 0;
  border-radius: 0.25em;

  cursor: pointer;
  transition: 150ms ease-in-out;

  font-size: 1.5em;
}

.buy-button {
  display: flex;
  justify-content: center;
  align-items: center;

  background-color: #fff;
  border: 1px solid #19b092;
  color: #19b092;

  padding: 0.65em 0;
  margin: 1.45em 0;
  border-radius: 0.25em;

  cursor: pointer;
  transition: 150ms ease-in-out;

  font-size: 1.5em;
}

.buy-button:hover {
  background-color: #19b092;
  border: 1px solid #fff;
  color: #fff;
}

#barcode-number {
  margin-bottom: 1rem;
}

.product-information-space {
  margin-top: 1rem;
}

.images-carousel {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-column-gap: .8em;
  max-width: 90%;
}

/* These two classes are used, but are not being picked up... :*( */
.sale-carousel-image {
  width: 100%;
  height: auto;
}

.main-carousel-image {
  border: black solid 1px;
}

.carousel-arrow {
  display: flex;
  justify-content: center;
  align-items: center;
}

@media (min-width: 530px) {
  #listing-image {
    max-width: 530px;
  }

  #main-image-wrapper {
    width: 530px;
    height: 530px;
  }

  .buy-button-section {
    margin-top: 0;
  }

  .listing-wrapper {
    margin: 0 auto;
    max-width: 530px;
  }

  .images-carousel-wrapper {
    display: flex;
    justify-content: space-between;
    margin-top: 1.25em;
  }
}

@media (min-width: 1140px) {
  .listing-wrapper {
    max-width: 1000px;
    min-height: 100vh;
  }

  .listing-dates-wrapper {
    flex-direction: row;
  }

  .return-button-wrapper {
    width: 50%;
  }

  .listing-dates-wrapper h6 {
    margin-right: 1.3em;
  }

  #listing-content-wrapper {
    grid-template-columns: 1fr 1fr;
    grid-column-gap: 2.5em;
    margin-top: 2rem;
  }

  .listing-images-wrapper {
    margin-top: 0;
  }

  .right-content {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    width: 100%;
  }
}

</style>