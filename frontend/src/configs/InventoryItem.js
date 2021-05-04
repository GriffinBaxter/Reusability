export default class InventoryItem {

    // This is a config for the Inventory Item requirement details
    static config = {
        productId: {
            name: "Product ID",
            minLength: 3,
            maxLength: 15,
            regex: /^[A-Z0-9-]+$/,
            regexMessage: "Must only contain uppercase alphanumeric characters, numbers, or -",
        },
        quantity: {
            name: "Quantity",
            minLength: 1,
            maxLength: 12,
            regex: /^[0-9]+$/,
            regexMessage: "Must only contain numbers",
        },
        pricePerItem: {
            name: "Price Per Item",
            minLength: 0,
            maxLength: 16,
            regex: /^(?:[1-9]\d*|0)?(?:\.\d+)?$/,
            regexMessage: "Must be a positive double precision floating point number e.g 1.00"
        },
        totalPrice: {
            name: "Total Price",
            minLength: 0,
            maxLength: 16,
            regex: /^(?:[1-9]\d*|0)?(?:\.\d+)?$/,
            regexMessage: "Must be a positive double precision floating point number e.g 1.00"
        },
        manufactured: {
            name: "manufactured"
        },
        sellBy: {
            name: "Sell By"
        },
        bestBefore: {
            name: "Best Before"
        },
        expires: {
            name: "Expires",
        },
    };

    constructor({productId, quantity, pricePerItem, totalPrice, manufactured, sellBy, bestBefore, expires}) {
        this.data = {
            productId,
            quantity,
            pricePerItem,
            totalPrice,
            manufactured,
            sellBy,
            bestBefore,
            expires
        }

    }

}