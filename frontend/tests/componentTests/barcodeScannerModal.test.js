/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import barcodeScannerModal from "../../src/components/BarcodeScannerModal";
import {describe, expect, test} from "@jest/globals";

const factory = (values = {}) => {
    return shallowMount(barcodeScannerModal, {
        data () {
            return {
                ...values
            }
        },
    })
}

describe("Testing the barcode scanner modal's functionality", () => {

    test('Testing the barcode scanner modal has the image upload and live scan buttons when livestreaming ' +
        'is available', () => {
        const wrapper = factory({
            liveStreamAvailable: true,
            liveStreaming: false,
            barcodeFound: false,
        });
        expect(wrapper.find('#modal-scan-by-uploading-image-button').exists()).toBeTruthy();
        expect(wrapper.find('#modal-scan-using-camera-button').exists()).toBeTruthy();
        expect(wrapper.find('#modal-stop-scanning-button').exists()).toBeFalsy();
        expect(wrapper.find('#modal-save-scanned-barcode-button').exists()).toBeFalsy();
    })

    test('Testing the barcode scanner modal has the image upload but not the live scan button when ' +
        'livestreaming is not available', () => {
        const wrapper = factory({
            liveStreamAvailable: false,
            liveStreaming: false,
            barcodeFound: false,
        });
        expect(wrapper.find('#modal-scan-by-uploading-image-button').exists()).toBeTruthy();
        expect(wrapper.find('#modal-scan-using-camera-button').exists()).toBeFalsy();
        expect(wrapper.find('#modal-stop-scanning-button').exists()).toBeFalsy();
        expect(wrapper.find('#modal-save-scanned-barcode-button').exists()).toBeFalsy();
    })

    test('Testing the barcode scanner modal has the stop scanning button when live streaming the camera feed',
        () => {
            const wrapper = factory({
                liveStreamAvailable: true,
                liveStreaming: true,
                barcodeFound: false,
            });
            expect(wrapper.find('#modal-scan-by-uploading-image-button').exists()).toBeTruthy();
            expect(wrapper.find('#modal-scan-using-camera-button').exists()).toBeFalsy();
            expect(wrapper.find('#modal-stop-scanning-button').exists()).toBeTruthy();
            expect(wrapper.find('#modal-save-scanned-barcode-button').exists()).toBeFalsy();
    })

    test('Testing the barcode scanner modal has the save barcode button when live streaming the camera feed ' +
        'with a found barcode', () => {
            const wrapper = factory({
                liveStreamAvailable: true,
                liveStreaming: true,
                barcodeFound: true,
            });
            expect(wrapper.find('#modal-scan-by-uploading-image-button').exists()).toBeTruthy();
            expect(wrapper.find('#modal-scan-using-camera-button').exists()).toBeFalsy();
            expect(wrapper.find('#modal-stop-scanning-button').exists()).toBeFalsy();
            expect(wrapper.find('#modal-save-scanned-barcode-button').exists()).toBeTruthy();
    })
})
