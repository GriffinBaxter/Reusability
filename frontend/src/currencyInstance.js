import axios from "axios";

// Create an instance to use for the REST Countries API
const currencyInstance = axios.create({
    baseURL: "https://restcountries.eu/rest/v2",
    timeout: 3000
});

export default {
    // Sends a get request to the API with the given input as the query
    currencyQuery: (name) => {
        return currencyInstance.get(`/name/${name}?fields=currencies`)
    }

}