export default class MarketplaceCard {
    static config = {

        title: {
            name: "Title",
            minLength: 1,
            maxLength: 20
        },
        description: {
            name: "Description",
            minLength: 0,
            maxLength: 108
        },
        keyword: {
            minLength: 2,
            maxLength: 20
        }
    }
}