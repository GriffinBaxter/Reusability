import Quagga from "quagga";
import OpenFoodFacts from "./openFoodFactsInstance";

/**
 * Retrieves the barcode number (EAN or UPC) from an uploaded image.
 */
export function getBarcodeStatic(outerThis, callback) {
    let file = outerThis.$refs.image.files[0];

    Quagga.decodeSingle({
        decoder: {
            readers: ["upc_reader", "ean_reader"]
        },
        locate: true,
        src: URL.createObjectURL(file)
    }, function (result) {
        if (result && result.codeResult) {
            outerThis.barcodeErrorMsg = "";
            outerThis.barcode = result.codeResult.code;
        } else {
            outerThis.barcodeErrorMsg = "Barcode not found in image";
        }
        callback();
    });
}

/**
 * Autofills data from the barcode, using the OpenFoodFacts API.
 */
export function autofillProductFromBarcode(outerThis, callback) {
    OpenFoodFacts.retrieveProductByBarcode(outerThis.barcode).then((result) => {
        if (result.data.status === 1) {
            outerThis.toastErrorMessage = "";
            let quantity = ""
            if (result.data.product.quantity !== undefined) {
                quantity = " " + result.data.product.quantity;
            }
            if (
                outerThis.productName === "" &&
                result.data.product.product_name !== undefined && result.data.product.product_name !== ""
            ) {
                outerThis.productName = result.data.product.product_name + quantity;
            }
            if (outerThis.manufacturer === "" && result.data.product.brands !== undefined) {
                outerThis.manufacturer = result.data.product.brands;
            }
            if (outerThis.description === "" && result.data.product.generic_name !== undefined) {
                outerThis.description = result.data.product.generic_name;
            }
        } else {
            outerThis.toastErrorMessage = "Could not autofill, product may not exist in database";
        }
        callback();
    });
}
