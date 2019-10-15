package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDetails;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
@Repository
public class EMUserRepository implements AbstractUserRepository {

    private EntityManager entityManager;

    public EMUserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
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

    public List<UserDetails> construct(long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<BigDecimal> maxQuery = criteriaBuilder.createQuery(BigDecimal.class);
        Root<Order> from = maxQuery.from(Order.class);
        maxQuery.select(criteriaBuilder.max(from.join("giftCertificates").get("price")))
                .where(criteriaBuilder.equal(from.join("user").get("id"), id));
        System.out.println(entityManager.createQuery(maxQuery).getResultList());


//        CriteriaQuery<Tag> tagQuery = criteriaBuilder.createQuery(Tag.class);
//        Root<Tag> tagFrom = maxQuery.from(Order.class);
//        tagQuery.select()
//
//
//                .select(criteriaBuilder.count(tagFrom
//                .join("orders")
//                .join("giftCertificates")
//                .join("tags")
//                .get("title"))).where(criteriaBuilder.equal(tagFrom.join("user").get("id"), id));

//                criteriaBuilder.count(
//                .join("orders")
//                .join("giftCertificates")
//                .join("tags")
//                .get("title"))).where(criteriaBuilder.equal(tagFrom.join("user").get("id"), id));


        CriteriaQuery<UserDetails> q = criteriaBuilder.createQuery(UserDetails.class);
        Root<User> root = q.from(User.class);
        q.select(criteriaBuilder.construct(UserDetails.class,
                criteriaBuilder.max(root
                        .join("orders")
                        .join("giftCertificates")
                        .join("tags")
                        .get("title")),
                criteriaBuilder.sum(root
                        .join("orders")
                        .join("giftCertificates")
                        .get("price"))))
                .where(criteriaBuilder.equal(root.get("id"), id))
        ;
        return entityManager.createQuery(q).getResultList();
    }
}
