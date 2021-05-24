import Cookies from "js-cookie";

/**
 * Check current user's permission
 * @returns {boolean} permission
 */
export function checkAccessPermission(linkBusinessAccount) {
    let permission = false;
    linkBusinessAccount.forEach(business => {
        if (business.id === this.$route.params.id){
            permission = true;
        }
    })
    return permission && Cookies.get('actAs') !== undefined && this.$route.params.id !== Cookies.get('actAs');
}