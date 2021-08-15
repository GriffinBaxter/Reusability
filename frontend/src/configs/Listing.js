export default class Listing{

    // This is a config for the Inventory Item requirement details
    static config = {
        inventoryItemId: {
            name: "Inventory Item ID",
            minLength: 1,
            maxLength: 15,
            regex: /^[0-9]+$/,
            regexMessage: "Must select an Inventory Item",
        },
        quantity: {
            name: "Quantity",
            minLength: 1,
            maxLength: 9,
            regex: /^[0-9]+$/,
            regexMessage: "Must only contain numbers",
        },
        price: {
            name: "Price",
            minLength: 1,
            maxLength: 16,
            regex: /^(?:[1-9]\d*|0)?(?:\.\d+)?$/,
            regexMessage: "Must be a positive double precision floating point number e.g 1.00"
        },
        moreInfo: {
            name: "MoreInfo",
            minLength: 0,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '-.]*$/
        },
        closes: {
            name: "manufactured"
        }
    };

    constructor({inventoryItemId, quantity, price, moreInfo, closes}) {
        this.data = {
            inventoryItemId,
            quantity,
            price,
            moreInfo,
            closes
        }

    }

}
