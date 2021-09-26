import Api from "../../Api";

/**
 * Adds the backend server URL to the user's primary profile image URL if they have images.
 * @param images A list of the user's profile images
 * @return String a full URL to the primary profile image of the user (or the default image if they don't have one)
 */
export function getThumbnailImageSrc(images) {
    if (images.length === 0) {
        return require('../../../public/default-image.jpg')
    }
    const primaryImage = images.filter((image) => image.isPrimary);
    return Api.getServerURL() + "/" + primaryImage[0].thumbnailFilename;
}
