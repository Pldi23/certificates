package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.constant.QueryConstant;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.constant.JpaConstant.*;

@Repository
public class EMTagRepository implements AbstractTagRepository {

    private EntityManager entityManager;

    public EMTagRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Optional<Tag> findById(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public List<Tag> findAll(String sortParam, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        return entityManager.createQuery(query.select(root)).getResultList();
    }

    @Override
    public void deleteById(long id) {
        Optional<Tag> optionalTag = Optional.ofNullable(entityManager.find(Tag.class, id));
        if (optionalTag.isPresent()) {
            entityManager.remove(optionalTag.get());
        } else {
            throw new EmptyResultDataAccessException(0);
        }
    }

    @Override
    public List<Tag> findTagsByCertificate(long certificateId, String sortParam, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.where(criteriaBuilder.equal(certificateRoot.get(ID), certificateId));
        Join<GiftCertificate, Tag> certificates = certificateRoot.join(TAGS);
        CriteriaQuery<Tag> cq = criteriaQuery.select(certificates).distinct(true);
        if (sortParam != null) {
                cq.orderBy(sortParam.startsWith("-") ? criteriaBuilder.desc(certificates.get(sortParam.replaceFirst("-", ""))) :
                        criteriaBuilder.asc(certificates.get(sortParam)));
        }
        TypedQuery<Tag> q = entityManager.createQuery(cq).setMaxResults(size).setFirstResult(page * size - size);
        return q.getResultList();
    }

    @Override
    public List<Tag> findTagsByOrder(long orderId, String sortParam, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.get(ID), orderId));
        Join<Order, Tag> orders = orderRoot.join(ORDER_CERTIFICATE).join(CERTIFICATE).join(TAGS);
        CriteriaQuery<Tag> cq = criteriaQuery.select(orders).distinct(true);
        if (sortParam != null) {
            cq.orderBy(sortParam.startsWith("-") ? criteriaBuilder.desc(orders.get(sortParam.replaceFirst("-", ""))) :
                    criteriaBuilder.asc(orders.get(sortParam)));
        }
        TypedQuery<Tag> q = entityManager.createQuery(cq);
        return q.setMaxResults(size).setFirstResult(page * size - size).getResultList();
    }

    @Override
    public List<Tag> findTagsByUserWithCriteria(long userId, String sortParam, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.where(criteriaBuilder.equal(orderRoot.join(USER).get(ID), userId));
        Join<Order, Tag> orders = orderRoot
                .join(ORDER_CERTIFICATE)
                .join(CERTIFICATE)
                .join(TAGS);
        CriteriaQuery<Tag> cq = criteriaQuery.select(orders)
                .groupBy(orders.get(ID));
        if (sortParam != null && sortParam.equals("cost")) {
                cq.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(orderRoot.join(ORDER_CERTIFICATE).get(FIXED_PRICE))));
        } else if (sortParam != null && sortParam.equals("-cost")) {
            cq.orderBy(criteriaBuilder.asc(criteriaBuilder.sum(orderRoot.join(ORDER_CERTIFICATE).get(FIXED_PRICE))));
        } else if (sortParam != null) {
            cq.orderBy(sortParam.startsWith("-") ? criteriaBuilder.desc(orders.get(sortParam.replaceFirst("-", ""))) :
                    criteriaBuilder.asc(orders.get(sortParam)));
        }
        TypedQuery<Tag> q = entityManager.createQuery(cq).setMaxResults(size).setFirstResult(page * size - size);
        return q.getResultList();
    }

    @Override
    public List<Tag> findMostCostEffectiveTagByUser(long userId) {

        Query q = entityManager.createNativeQuery(QueryConstant.MOST_COST_EFFECTIVE_TAG_BY_USER, Tag.class);

        q.setParameter(1, userId);

        return (List<Tag>) q.getResultList();
    }

    @Override
    public long count() {
        Query queryTotal = entityManager.createQuery
                ("Select count(t.id) from Tag t");
        return  (long)queryTotal.getSingleResult();
//        Query q = entityManager.createNativeQuery("select count(*) from tag");
//        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public List<Tag> findPopulars(int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        Join<Order, Tag> orders = orderRoot.join(ORDER_CERTIFICATE)
                .join(CERTIFICATE)
                .join(TAGS);
        CriteriaQuery<Tag> cq = criteriaQuery.select(orders)
                .groupBy(orders.get(ID))
                .orderBy(criteriaBuilder.desc(criteriaBuilder.sum(orderRoot.join(ORDER_CERTIFICATE).get(FIXED_PRICE))));

        TypedQuery<Tag> typedQuery = entityManager.createQuery(cq);
        return typedQuery.setFirstResult(page * size - size).setMaxResults(size).getResultList();
    }

    @Override
    public Optional<Tag> findByTitle(String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        query.where(
                criteriaBuilder.equal(
                        root.get(TITLE), title
                )
        );

        return !entityManager.createQuery(query).getResultList().isEmpty() ?
                Optional.of(entityManager.createQuery(query).getSingleResult()) :
                Optional.empty();
    }

    @Override
    public List<Tag> findPaginated(String sort, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> from = criteriaQuery.from(Tag.class);
        CriteriaQuery<Tag> select = criteriaQuery.select(from);
        if (sort != null) {
            select.orderBy(sort.startsWith("-") ? criteriaBuilder.desc(from.get(sort.replaceFirst("-", ""))) :
                    criteriaBuilder.asc(from.get(sort)));
        }

        TypedQuery<Tag> typedQuery = entityManager.createQuery(select).setFirstResult(page * size - size).setMaxResults(size);
        return typedQuery.getResultList();
    }
}
