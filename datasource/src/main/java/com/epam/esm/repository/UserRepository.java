package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.constant.JpaConstant.EMAIL;
import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.NUM_ORDERS_ASC_SORT_PARAMETER;
import static com.epam.esm.repository.constant.JpaConstant.NUM_ORDERS_DESC_SORT_PARAMETER;
import static com.epam.esm.repository.constant.JpaConstant.ORDERS;


@Repository
public class UserRepository implements AbstractUserRepository {

    private EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll(String sortParam, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        if (sortParam != null && (sortParam.equals(NUM_ORDERS_DESC_SORT_PARAMETER) || sortParam.equals(NUM_ORDERS_ASC_SORT_PARAMETER))) {
            query
                    .groupBy(root.get(ID))
                    .orderBy(sortParam.startsWith("-") ?
                    criteriaBuilder.asc(criteriaBuilder.countDistinct(root.join(ORDERS).get(ID))) :
                    criteriaBuilder.desc(criteriaBuilder.countDistinct(root.join(ORDERS).get(ID))));
        } else if (sortParam != null) {
            query.orderBy(sortParam.startsWith("-") ?
                    criteriaBuilder.asc(root.get(sortParam.replaceFirst("-", ""))) :
                    criteriaBuilder.desc(root.get(sortParam)));
        }
        return entityManager.createQuery(query).setMaxResults(size).setFirstResult(page * size - size).getResultList();
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));

    }

    @Override
    public void deleteById(long id) {
        Optional<User> optional = Optional.ofNullable(entityManager.find(User.class, id));
        if (optional.isPresent()) {
            entityManager.remove(optional.get());
        } else throw new EmptyResultDataAccessException(0);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(
                criteriaBuilder.equal(
                        root.get(EMAIL), email
                )
        );

        return !entityManager.createQuery(query).getResultList().isEmpty() ?
                Optional.of(entityManager.createQuery(query).getSingleResult()) :
                Optional.empty();
    }
}
