<template>
  <div>
    <Navbar />

    <main class="listing-wrapper">
      <div class="image-title-content">
        <h1>Product Name</h1>
        <div class="listing-dates-wrapper">
          <h6>Listing Date: [Listing Date]</h6>
          <h6>Closing Date: [closing Date]</h6>
        </div>
        <div class="product-images-wrapper">
          <img :src="getMainImage()" alt="Product [product - name ] image" id="product-image"/>
          <div class="images-carousel">
            <img class="sale-carousel-image" v-for="(image, index) in saleImages" :key="index" :src="image.thumbnailFilename" alt="Product [product - name ] image 2">
          </div>
        </div>
      </div>
      <div></div>
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
  .listing-wrapper {
    display: flex;
    flex-direction: column;
  }
</style>