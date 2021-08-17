<template>
  <div class="container py-4 mt-4 shadow">
    Card does not exist.
    &nbsp;
    <button class="btn btn-outline-primary green-button" @click="$router.push({path: '/home'})">Return Home</button>
    &nbsp;
    <button class="btn btn-outline-primary green-button" @click="$router.push({path: '/'})">Return to Login</button>
  </div>
</template>

<script>
export default {
  name: "NoSuchCard"
}
</script>
