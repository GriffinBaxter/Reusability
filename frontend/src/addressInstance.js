import axios from "axios";

// Create an instance to use for the Komoot Photon API
const addressInstance = axios.create({
    baseURL: "https://photon.komoot.io/api/",
    timeout: 10000
});

export default {
    // Sends a get request to the API with the given input as the query
    addressQuery: (input) => {
        return addressInstance.get(`/?q=${input}`)
    }

}
