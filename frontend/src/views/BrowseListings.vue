<template>
  <div>
    <div id="main">
      <!-- Navbar -->
      <Navbar/>

    </div>
    <!-- Footer -->
    <Footer class="footer"/>
  </div>
</template>

<script>
import Navbar from "../components/main/Navbar";
import Footer from "../components/main/Footer";

export default {
  name: "Listings",
  components: {Footer, Navbar},
  data() {
    return {}
  },
  computed: {
  },
  methods: {
  }
}

</script>

<style scoped>

</style>