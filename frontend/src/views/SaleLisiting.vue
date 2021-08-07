<template>
  <div>
    <Navbar />

    <main class="listing-wrapper">

        <!-- Listing header -->
        <div class="title-content">
          <div id="Listing-name">Product Name</div>
          <div class="listing-dates-wrapper">
            <h6>Listing Date: [Listing Date]</h6>
            <h6>Closing Date: [closing Date]</h6>
          </div>
        </div>
      <div id="listing-content-wrapper">
        <div class="left-content">
          <!-- Image section -->
          <div class="listing-images-wrapper">
            <img :src="getMainImage()" alt="Product [product - name ] image" id="listing-image"/>
            <div class="images-carousel-wrapper">
              <div class="carousel-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-left" viewBox="0 0 16 16">
                  <path fill-rule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/>
                </svg>
              </div>
              <div class="images-carousel">
                <img class="sale-carousel-image" v-for="(image, index) in saleImages" :key="index" :src="image.thumbnailFilename" alt="Product [product - name ] image 2">
              </div>
              <div class="carousel-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-right" viewBox="0 0 16 16">
                  <path fill-rule="evenodd" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/>
                </svg>
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

  #Listing-name {
    font-size: 3rem;
    margin-left: -0.18rem;
    font-family: 'Merriweather Sans', sans-serif;
  }

  .merriweather {
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

  .listing-images-wrapper {
    margin-top: 1.25em;
  }

  .listing-dates-wrapper {
    display: flex;
    justify-content: left;
    flex-direction: column;
  }

  #listing-image {
    width: 100%;
    height: auto;
  }

  #price {
    font-weight: bold;
    font-size: 1.3em;
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

  .sale-carousel-image {
    width: 100%;
    height: auto;
  }

  .carousel-arrow {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  @media(min-width: 530px) {
    #listing-image {
      max-width: 530px;
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

  @media(min-width: 1140px) {
    .listing-wrapper {
      max-width: 1000px;
      min-height: 100vh;
    }

    #listing-content-wrapper {
      grid-template-columns: 1fr 1fr;
      grid-column-gap: 2.5em;
      margin-top: 2rem;
    }

    .listing-images-wrapper {
      margin-top: 0;
    }

    .buy-button-section {
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