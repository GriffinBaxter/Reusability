<!--This file creates the Vue app instance-->
<!--The page transition is also created here.-->
<!--The app instance watches for error routes and routes to the error page one occurs.-->

<template>
  <div id="app">
    <transition :name="transitionName" mode="out-in">
      <router-view :key="$route.path"></router-view>
    </transition>

  </div>
</template>

<script>


// Vue app instance
// it is declared as a reusable component in this case.
// For global instance https://vuejs.org/v2/guide/instance.html
// For comparison: https://stackoverflow.com/questions/48727863/vue-export-default-vs-new-vue
const app = {

  name: "app",
  components: {
    // list your components here to register them (located under 'components' folder)
    // https://vuejs.org/v2/guide/components-registration.html
  },
  // app initial state
  // https://vuejs.org/v2/guide/instance.html#Data-and-Methods
  data: () => {
    return {
      transitionName: "slide"
    };
  },
  watch: {
    '$route'(to) {
      document.title = to.meta.title ? `Reusability · ${to.meta.title}` : "Reusability";
      if (to.name === 'NoUser' || to.name === 'ServerTimeout' || to.name === 'InvalidToken') this.transitionName = "";
      const event = new CustomEvent('page-routing', {detail: ""})
      document.dispatchEvent(event);
    },
  }
};

// make the 'app' available
export default app;
</script>

<!---------------------------------------------------- App Styling ---------------------------------------------------->

<style>
[v-cloak] {
  display: none;
}

.slide-enter-active,
.slide-leave-active  {
  transition: opacity 0.4s, transform 0.4s;
}
.slide-enter {
  opacity: 0;
  transform: translate(30%) ;
}
.slide-leave-to {
  opacity: 0;
  transform: translate(-30%);
}
</style>
