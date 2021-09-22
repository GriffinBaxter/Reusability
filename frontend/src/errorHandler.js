/**
 * If a request in to the backend results in an error, then this method will deal with the error.
 * @param error the error received from the backend.
 */
export async function manageError(error) {
    if (error.request && !error.response)      { await this.$router.push({path: '/timeout'});      }
    else if (error.response.status === 401)    { await this.$router.push({path: '/invalidtoken'}); }
    else if (error.response.status === 403)    { await this.$router.push({path: '/forbidden'});    }
    else if (error.response.status === 406)    { await this.$router.push({path: '/noBusiness'});   }
    else { await this.$router.push({path: '/noBusiness'}); console.log(error.message); }
}
