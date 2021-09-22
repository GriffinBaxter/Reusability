/**
 * Summary. This file contains the definition for the ForgotPasswordRepository.
 *
 * Description. This file contains the definition for the ForgotPasswordRepository.
 *
 * @link   team-400/src/main/java/org/seng302/forgotPassword/ForgotPasswordRepository
 * @file   This file contains the definition for ForgotPasswordRepository.
 * @author team-400.
 * @since  22.9.2021
 */
package org.seng302.model.repository;

import org.seng302.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * ForgotPasswordRepository interface.
 * This class contains methods which can be used to retrieve ForgotPassword entities based on various criteria.
 */
@RepositoryRestResource
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, String> {
}
