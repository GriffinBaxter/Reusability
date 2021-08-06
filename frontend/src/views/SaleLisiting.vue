<template>
  <div>
    <Navbar />

    <main class="listing-wrapper">

      <div class="left-content">
        <!-- Listing header -->
        <div class="title-content">
          <h1>Product Name</h1>
          <div class="listing-dates-wrapper">
            <h6>Listing Date: [Listing Date]</h6>
            <h6>Closing Date: [closing Date]</h6>
          </div>
        </div>

        <!-- Image section -->
        <div class="listing-images-wrapper">
          <img :src="getMainImage()" alt="Product [product - name ] image" id="listing-image"/>
          <div class="images-carousel-wrapper">
            <div class="images-carousel">
              <img class="sale-carousel-image" v-for="(image, index) in saleImages" :key="index" :src="image.thumbnailFilename" alt="Product [product - name ] image 2">
            </div>
          </div>
        </div>

        <!-- Bussiness information -->
        <div class="business-wrapper">
          <h5>Business Name</h5>
          <div class="business-address-wrapper">
            [business address]
          </div>
        </div>
      </div>

      <div class="right-content">

        <!-- Buy Button -->
        <div class="buy-button-section">
          <h6 id="price" class="merriweather">
            $3000.00
          </h6>
          <div class="buy-button-wrapper">
            <div class="buy-button merriweather">
              Buy
            </div>
            <div class="barcode-wrapper">
              <div id="barcode-number">Barcode: [barcode number]</div>
            </div>
          </div>
        </div>

        <!-- Product information -->
        <div class="product-information-wrapper">
          <div>[Description]</div>
          <div id="product-id-quantity-wrapper" class="product-information-space">
            <div>Product ID: [Product code]</div>
            <div>Quantity: [Quantity]</div>
          </div>
          <div class="product-information-space">
            <div>Manufactured: [Manufactured]</div>
            <div>Sell by: [Sell by]</div>
            <div>Best before: [Best before]</div>
            <div>Expires: [Expires]</div>
            <div>Manufacturer: [Manufacturer]</div>
          </div>
        </div>

      </div>

    </main>

    <Footer />
  </div>
</template>

<script>
  import Navbar from "@/components/main/Navbar";
  import Footer from "@/components/main/Footer"
  import DefaultImage from "../../public/default-product.jpg"

  export default {
    name: "SaleListing",
    components: {
      Navbar,
      Footer
    },
    data() {
      return {
        mainImageIndex: 0,
        saleImages: [
          {
            thumbnailFilename: "default-product.jpg",
            filename: "default-product.jpg"
          },
          {
            thumbnailFilename: "default-product.jpg",
            filename: "default-product.jpg"
          },
          {
            thumbnailFilename: "default-product.jpg",
            filename: "default-product.jpg"
          }
        ],
      }
    },
    methods: {
      getMainImage() {
        if (this.mainImageIndex < this.saleImages.length) {
          return this.saleImages[this.mainImageIndex].filename;
        }
        return DefaultImage;
      }
    }
  }
</script>

<style scoped>
  h5, h1 {
    font-family: 'Merriweather Sans', sans-serif;
  }

  .merriweather {
    font-family: 'Merriweather Sans', sans-serif;
  }

  h6 {
    margin: 0;
  }

  .listing-wrapper {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: 1fr 1fr;
    grid-row-gap: 2.5em;
    padding: 1.5em;
  }

  .images-carousel-wrapper {
    display: none;
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
    min-width: 100%;
    height: auto;
  }

  #price {
    font-weight: bold;
    font-size: 1.3em;
  }

  .business-wrapper {
    margin-top: 1.25em;
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

</style>