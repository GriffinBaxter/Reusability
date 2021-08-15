export default class Product {

    // This is a config for the product requirement details
    static config = {
        productID: {
            name: "Product ID",
            minLength: 3,
            maxLength: 15,
            regex: /^[A-Z0-9-]+$/,
            regexMessage: "Must only contain uppercase alphanumeric characters, numbers, or -",
        },
        barcode: {
            name: "Product barcode",
            minLength: 12,
            maxLength: 13,
            regex: /^[0-9-]+$/,
            regexMessage: "Must only contain numbers"
        },
        productName: {
            name: "Product name",
            minLength: 1,
            maxLength: 100,
            regex: /^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces or '#,.&()-"
        },
        description: {
            name: "Description",
            minLength: 0,
            maxLength: 600
        },
        manufacturer: {
            name: "Manufacturer",
            minLength: 0,
            maxLength: 100,
            regex: /^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '#,.&()-]*$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces or '#,.&()-"
        },
        recommendedRetailPrice: {
            name: "Recommended retail price",
            minLength: 0,
            maxLength: 16,
            regex: /^(?:[1-9]\d*|0)?(?:\.\d+)?$/,
            regexMessage: "Must be a positive double precision floating point number e.g 1.00"
        },
        images: {
            name: "Images"
        }
    };

    constructor({id, barcode, name, description, manufacturer, recommendedRetailPrice, created, images}) {
        this.data = {
            id,
            barcode,
            name,
            description,
            manufacturer,
            recommendedRetailPrice,
            created,
            images
        }

    }

}
