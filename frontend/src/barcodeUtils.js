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
 * Detects the barcode number (EAN or UPC) from a live camera feed per-frame.
 */
function detectBarcodeLiveStream(width, div, callback) {
    Quagga.init({
        inputStream : {
            name : "Live",
            type : "LiveStream",
            target: document.querySelector(div),
            constraints: {
                width: width,
                height: 345,
            },
        },
        decoder : {
            readers : ["upc_reader", "ean_reader"]
        }
    }, function(err) {
        if (err) {
            if (err.message === "Could not start video source") {
                document.querySelector(div).innerHTML =
                    "<span style='color: red;'>Error: Camera in use by another program.</span>";
            } else if (err.message === "Permission denied" || err.message === "Permission dismissed") {
                document.querySelector(div).innerHTML =
                    "<span style='color: red;'>Error: Insufficient browser permissions to use camera.</span>";
            } else {
                document.querySelector(div).innerHTML =
                    "<span style='color: red;'>Error: Camera not found/available.</span>";
            }
            return
        }
        Quagga.start();
        Quagga.onDetected(callback)
        Quagga.onProcessed(callback)
    });
}

/**
 * Retrieves the barcode number (EAN or UPC) from a live camera feed, based on the most commonly occurring barcode
 * per each frame scan.
 */
function retrieveBarcodeLiveStream(outerThis, width, div, callback) {
    outerThis.liveStreaming = true;
    let barcodeOccurrences = {};
    detectBarcodeLiveStream(width, div, function (barcodeObject) {

        const drawingCanvas = Quagga.canvas.dom.overlay;
        drawingCanvas.style.display = 'none';

        if (barcodeObject !== undefined && barcodeObject.codeResult !== undefined) {
            outerThis.barcodeFound = true;
            if (Object.prototype.hasOwnProperty.call(barcodeOccurrences, barcodeObject.codeResult.code)) {
                barcodeOccurrences[barcodeObject.codeResult.code] += 1
            } else {
                barcodeOccurrences[barcodeObject.codeResult.code] = 1
            }
        }

        if (!outerThis.liveStreaming && outerThis.barcodeFound) {
            let barcodeScanned = null;
            let barcodeScannedNum = 0;

            Object.keys(barcodeOccurrences).forEach(function(barcode) {
                if (barcodeOccurrences[barcode] > barcodeScannedNum) {
                    barcodeScannedNum = barcodeOccurrences[barcode]
                    barcodeScanned = barcode;
                }
            })

            outerThis.barcode = barcodeScanned;
        }
        if (!outerThis.liveStreaming) {
            Quagga.stop();
            try {
                document.querySelector(div).innerHTML = "";
                outerThis.barcodeFound = false;
                callback();
            } catch {
                outerThis.barcodeFound = false;
                callback();
            }
        }
    });
}

/**
 * Initially stops Quagga if it's already in-use, then runs the barcode retrieval process.
 */
export function getBarcodeLiveStream(outerThis, width, div, callback) {
    try {
        Quagga.stop();
        retrieveBarcodeLiveStream(outerThis, width, div, callback)
    } catch {
        retrieveBarcodeLiveStream(outerThis, width, div, callback)
    }
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
