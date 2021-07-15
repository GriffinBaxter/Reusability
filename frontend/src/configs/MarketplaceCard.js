export default class MarketplaceCard {
    static config = {

        title: {
            name: "Title",
            minLength: 1,
            maxLength: 51
        },
        description: {
            name: "Description",
            minLength: 0,
            maxLength: 301
        },
        keyword: {
            minLength: 2,
            maxLength: 20
        }
    }
}