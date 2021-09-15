export default class Business{

    // This is a config for the business requirement details
    static config = {
        businessName: {
            name: "Name",
            minLength: 1,
            maxLength: 100,
            regex: /^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$",
        },
        description: {
            name: "Description",
            minLength: 0,
            maxLength: 600
        },
        businessType: {
            name: "Business type",
        },
        businessAddress: {
            name: "Business address",
            minLength: 0,
            maxLength: 255,
            regex: /^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$",
        },
        streetNumber: {
            name: "Street number",
            minLength: 0,
            maxLength: 255
        },
        streetName: {
            name: "Street name",
            minLength: 0,
            maxLength: 255
        },
        suburb: {
            name: "Suburb",
            minLength: 0,
            maxLength: 255
        },
        city: {
            name: "City",
            minLength: 0,
            maxLength: 255,
        },
        postcode: {
            name: "Postcode",
            minLength: 0,
            maxLength: 255
        },
        region: {
            name: "Region",
            minLength: 0,
            maxLength: 255
        },
        country: {
            name: "Country",
            minLength: 1,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-ZÀ-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '-]+$/
        },
        currencySymbol: {
            name: "Currency symbol"
        },
        currencyCode: {
            name: "Currency code"
        },
    };

    constructor({primaryAdministratorId, name, description, address, businessType, currencySymbol, currencyCode}) {
        this.data = {
            primaryAdministratorId,
            name,
            description,
            address,
            businessType,
            currencySymbol,
            currencyCode
        }

    }

}