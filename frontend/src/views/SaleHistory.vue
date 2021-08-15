<template>
  <div>
    <div id="main">
      <!--nav bar-->
      <Navbar></Navbar>

    </div>
    <Footer/>
  </div>

</template>

<script>
import Navbar from "../components/main/Navbar";
import Footer from "../components/main/Footer";

export default {
  name: "SaleHistory",
  components: {
    Footer,
    Navbar
  },
}
</script>

<style scoped>

</style>