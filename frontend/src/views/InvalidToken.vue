<!--This file creates the page that the user is taken to when the "401 Access token is missing or invalid" error occurs-->

<template>
  <div class="container py-4 mt-4 shadow">
    You have been logged out, someone may have logged in as your user from another device.
    <br>
    (401 Access token is missing or invalid)
    <br><br>
    <button class="btn btn-outline-primary green-button" @click="$router.push({path: '/'})">Return to Login</button>
  </div>
</template>

<script>
export default {
  name: "InvalidToken"
}
</script>
