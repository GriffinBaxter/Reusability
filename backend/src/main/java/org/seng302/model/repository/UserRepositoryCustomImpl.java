package org.seng302.model.repository;

import org.seng302.model.Business;
import org.seng302.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Search for users by user names.
     * @param names A list of user names.
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching user results.
     *
     * Preconditions:  A non-null list of names to search for users.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching user results.
     */
    @Override
    public Page<User> findAllUsersByNames(List<String> names, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);

        Root<User> user = query.from(User.class);

        Path<String> namePath = user.get("name");

        List<Predicate> predicates = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.replaceAll("^\"+|\"+$", ""); // Remove quotations.
                predicates.add(criteriaBuilder.equal(namePath, name));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(namePath), "%" + name.toUpperCase() + "%"));
            }
        }
        query.select(user)
                .where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

        List<User> users = entityManager.createQuery(query).getResultList();
        return new PageImpl<>(users, pageable, users.size());
    }

    /**
     * Search for users by their first, middle, last name or nick name, ignoring case.
     * Takes a pageable object for pagination and allows partial matches (not case sensitive).
     * @param names A list of names that need to be searched for.
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching user results
     */
    @Query(value="select u from User u where upper(CONCAT(u.firstName, ' ', u.middleName, ' ', u.lastName)) LIKE CONCAT('%',upper(?1),'%') or upper(u.nickname) LIKE CONCAT('%',upper(?1),'%') or upper(CONCAT(u.firstName, ' ', u.lastName)) LIKE CONCAT('%',upper(?1),'%') or upper(CONCAT(u.firstName, ' ', u.middleName)) LIKE CONCAT('%',upper(?1),'%') or upper(CONCAT(u.middleName, ' ', u.lastName)) LIKE CONCAT('%',upper(?1),'%')")
    Page<User> findAllUsersByNames(@Param("names") List<String> names, Pageable pageable);
}
