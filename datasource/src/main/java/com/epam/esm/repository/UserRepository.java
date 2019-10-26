package com.epam.esm.repository;

import com.epam.esm.entity.User;
import com.epam.esm.repository.page.Pageable;
import com.epam.esm.repository.predicate.Specification;
import com.epam.esm.repository.sort.Sortable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.constant.JpaConstant.EMAIL;
import static com.epam.esm.repository.constant.JpaConstant.ID;


@Repository
public class UserRepository implements AbstractUserRepository {

    private EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
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

    @Override
    public List<User> findAllSpecified(List<Specification<User>> specifications, Sortable<User> sortable, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.groupBy(root.get(ID));
        if (sortable != null) {
            query.orderBy(sortable.setOrder(root, query, criteriaBuilder));
        }
        if (specifications != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<User> s : specifications) {
                predicates.addAll(s.toPredicates(root, query, criteriaBuilder));
            }
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return pageable.setPageAndSize(entityManager.createQuery(query)).getResultList();
    }
}
