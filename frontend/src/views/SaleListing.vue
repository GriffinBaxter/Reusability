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
          <!-- Image section -->
          <div class="listing-images-wrapper">
            <div id="main-image-wrapper">
              <img :src="getMainImage()" alt="Product [product - name ] image" id="listing-image" class="no-highlight"/>
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
                    :src="getCarouselImage(imageIndex)" alt="Product [product - name ] image 2">
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
          </div>
        </div>

        <div class="right-content">

          <!-- Buy Button -->
          <div class="buy-button-section">
            <h6 id="price" class="merryweather">
              $ {{ price }}
            </h6>
            <div style="width: fit-content">
              <h6 id="bookmarks" class="merryweather" @click="changeBookmarkStatus">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                     class="bi bi-bookmark-heart-fill" viewBox="0 0 16 16" v-if="isBookmarked">
                  <path
                      d="M2 15.5a.5.5 0 0 0 .74.439L8 13.069l5.26 2.87A.5.5 0 0 0 14 15.5V2a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v13.5zM8 4.41c1.387-1.425 4.854 1.07 0 4.277C3.146 5.48 6.613 2.986 8 4.412z"/>
                </svg>
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                     class="bi bi-bookmark-heart" viewBox="0 0 16 16" v-else>
                  <path fill-rule="evenodd"
                        d="M8 4.41c1.387-1.425 4.854 1.07 0 4.277C3.146 5.48 6.613 2.986 8 4.412z"/>
                  <path
                      d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z"/>
                </svg>
                {{ totalBookmarks }}
              </h6>
            </div>
            <div class="buy-button-wrapper">
              <div class="buy-button merryweather">
                Buy
              </div>
              <div class="barcode-wrapper">
                <div id="barcode-number" v-if="barcode">Barcode: {{ barcode }}</div>
              </div>
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
  </div>
</template>

<script>
import Navbar from "../components/main/Navbar";
import Footer from "../components/main/Footer"
import DefaultImage from "../../public/default-product.jpg"
import Api from "../Api";

export default {
  name: "SaleListing",
  components: {
    Navbar,
    Footer
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
      businessName: "",
      businessAddress: "",
      businessAddressLine1: "",
      businessAddressLine2: "",
      businessAddressLine3: "",
      businessAddressLine4: "",

      listingId: null,
      isBookmarked: null,
      totalBookmarks: 0,
    }
  },
  methods: {
    /**
     * change bookmark status
     */
    changeBookmarkStatus() {
      this.isBookmarked = !this.isBookmarked
      this.totalBookmarks = this.isBookmarked ? this.totalBookmarks + 1 : this.totalBookmarks - 1;
      Api.changeStatusOfAListing(this.listingId)
          .catch(() => {
            this.isBookmarked = !this.isBookmarked
            this.totalBookmarks = this.isBookmarked ? this.totalBookmarks + 1 : this.totalBookmarks - 1;
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
      this.startDate = data.created;
      this.closeData = data.closes;
      this.manufactured = data.inventoryItem.manufactured;
      this.sellBy = data.inventoryItem.sellBy;
      this.bestBefore = data.inventoryItem.bestBefore;
      this.expires = data.inventoryItem.expires;
      this.manufacturer = data.inventoryItem.product.manufacturer;

      // Business info
      this.businessName = data.inventoryItem.product.business.name;
      this.businessAddress = data.inventoryItem.product.business.address;

      // address population
      this.businessAddressLine1 = (this.businessAddress.streetNumber !== "" && this.businessAddress.streetName !== "")
          ? this.businessAddress.streetNumber + " " + this.businessAddress.streetName
          : this.businessAddress.streetNumber + this.businessAddress.streetName;
      this.businessAddressLine2 = (this.businessAddress.suburb !== "") ? this.businessAddress.suburb : "";
      this.businessAddressLine3 = (this.businessAddress.city !== "" && this.businessAddress.postcode !== "")
          ? this.businessAddress.city + ", " + this.businessAddress.postcode
          : this.businessAddress.city + this.businessAddress.postcode;
      this.businessAddressLine4 = (this.businessAddress.region !== "" && this.businessAddress.country !== "")
          ? this.businessAddress.region + ", " + this.businessAddress.country
          : this.businessAddress.region + this.businessAddress.country;

      // bookmark info
      this.listingId = data.id;
      this.isBookmarked = data.isBookmarked;
      this.totalBookmarks = data.totalBookmarks;
    }
  },
  beforeMount() {
    const url = document.URL;
    const businessId = url.substring(url.lastIndexOf('/businessProfile/') + 17, url.lastIndexOf('/listings/'));
    const listingId = url.substring(url.lastIndexOf('/') + 1);
    const self = this;
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