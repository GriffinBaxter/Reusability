package org.seng302.model.repository;

import org.seng302.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

        Path<String> nicknamePath = user.get("nickname");
        Path<String> firstNamePath = user.get("firstName");
        Path<String> lastNamePath = user.get("lastName");
        Path<String> middleNamePath = user.get("middleName");

        Expression<String> firstNameMiddleName = criteriaBuilder.concat(firstNamePath, " ");
        firstNameMiddleName = criteriaBuilder.concat(firstNameMiddleName, middleNamePath);

        Expression<String> firstNameLastName = criteriaBuilder.concat(firstNamePath, " ");
        firstNameLastName = criteriaBuilder.concat(firstNameLastName, lastNamePath);

        Expression<String> middleNameLastName = criteriaBuilder.concat(middleNamePath, " ");
        middleNameLastName = criteriaBuilder.concat(middleNameLastName, lastNamePath);

        Expression<String> fullName = criteriaBuilder.concat(firstNameMiddleName, " ");
        fullName = criteriaBuilder.concat(fullName, lastNamePath);


        List<Predicate> predicates = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.replaceAll("^\"+|\"+$", ""); // Remove quotations.
                predicates.add(criteriaBuilder.equal(nicknamePath, name));
                predicates.add(criteriaBuilder.equal(firstNameMiddleName, name));
                predicates.add(criteriaBuilder.equal(firstNameLastName, name));
                predicates.add(criteriaBuilder.equal(middleNameLastName, name));
                predicates.add(criteriaBuilder.equal(fullName, name));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(nicknamePath), "%" + name.toUpperCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(firstNameMiddleName), "%" + name.toUpperCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(firstNameLastName), "%" + name.toUpperCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(middleNameLastName), "%" + name.toUpperCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(fullName), "%" + name.toUpperCase() + "%"));
            }
        }
        query.select(user)
                .where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

        List<User> users = entityManager.createQuery(query).getResultList();
        return new PageImpl<>(users, pageable, users.size());
    }
}
