<template>
  <div class="card" style="width: 18rem;">
    <div class="row">
      <div class="col">
        <img :src="require('../../../public/test.jpg')" class="card-img-top" alt="default-image">
      </div>
      <div class="col p-0">
          <div class="row">
            <div class="card-body" id="price-div">
              <h3>$2.99</h3>
            </div>
          </div>
          <div class="row">
            <div class="card-body">
              <a class="btn btn-primary green-button" id="seller-info-button">Seller Info</a>
            </div>
          </div>
        </div>

    </div>
    <div class="row">
      <div class="col">
        <div class="card-body pb-0 py-1">
        </div>
        <ul class="list-group list-group-flush">
          <li class="list-group-item pb-1 "><h5>Apple (Green) x2</h5></li>
          <li class="list-group-item">Closing Date: 2nd Sep 2021</li>
          <li class="list-group-item">Expires: 10th Nov 2021</li>
        </ul>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "BrowseListingCard"
}
</script>

<style scoped>

#seller-info-button {
  width: 82%;
}

/*#price-div {*/
/*  margin-left: 1.3em;*/
/*}*/

.card {
  margin: 0.5em 0.5em 0.5em 0.5em;

}

.card:hover {
  cursor: pointer;
  box-shadow: 2px 2px lawngreen;
}


</style>
