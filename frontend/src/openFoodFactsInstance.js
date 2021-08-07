import axios from "axios";

// Create an instance to use for the Open Food Facts API
const openFoodFactsInstance = axios.create({
    baseURL: "https://world.openfoodfacts.org/api/v0",
    timeout: 10000
});

export default {
    // Sends a get request to the API with the given input as the query
    retrieveProductByBarcode: (barcode) => {
        return openFoodFactsInstance.get(`/product/${barcode}.json`)
    }

}