/**
 * Check current user's permission
 * @returns {boolean} permission
 */
export function checkAccessPermission(id, actAs) {
    return actAs !== undefined && id !== actAs;
}