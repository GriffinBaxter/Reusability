<!--This file creates the page that the user is taken to when the "listing does not exist" error occurs-->

<template>
  <div class="container py-4 mt-4 shadow text-font">
    Listing does not exist.
    &nbsp;
    <button class="btn btn-outline-primary green-button" @click="$router.push({path: '/home'})">Return Home</button>
    &nbsp;
    <button class="btn btn-outline-primary green-button" @click="$router.push({path: '/'})">Return to Login</button>
  </div>
</template>

<script>
export default {
  name: "NoSuchListing"
}
</script>
