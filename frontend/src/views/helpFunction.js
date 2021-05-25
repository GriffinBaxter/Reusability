import Cookies from "js-cookie";

/**
 * Check current user's permission
 * @returns {boolean} permission
 */
export function checkAccessPermission() {
    return Cookies.get('actAs') !== undefined && this.$route.params.id !== Cookies.get('actAs');
}