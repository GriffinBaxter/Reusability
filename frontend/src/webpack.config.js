const GoogleFontsPlugin = require("google-fonts-webpack-plugin")

module.exports = {
    "entry": "index.js",
    /* ... */
    plugins: [
        new GoogleFontsPlugin({
            fonts: [
                {
                    family: "Oswald",
                    variants: ["sans-serif"]
                },
                { family: "Roboto",
                    variants: [ "400", "700italic" ]
                }
            ]
        })
    ]
}