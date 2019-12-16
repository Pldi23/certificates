package com.epam.esm.repository;

import com.epam.esm.entity.OrderCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.constant.QueryConstant;
import com.epam.esm.repository.page.Pageable;
import com.epam.esm.repository.predicate.Specification;
import com.epam.esm.repository.sort.Sortable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.constant.JpaConstant.CERTIFICATE;
import static com.epam.esm.repository.constant.JpaConstant.FIXED_PRICE;
import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.ORDER;
import static com.epam.esm.repository.constant.JpaConstant.TAGS;
import static com.epam.esm.repository.constant.JpaConstant.TITLE;
import static com.epam.esm.repository.constant.JpaConstant.USER;

@Repository
public class TagRepository implements AbstractTagRepository {

    private EntityManager entityManager;

    public TagRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
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
    public void deleteById(long id) {
        Optional<Tag> optionalTag = Optional.ofNullable(entityManager.find(Tag.class, id));
        if (optionalTag.isPresent()) {
            entityManager.remove(optionalTag.get());
        } else {
            throw new EmptyResultDataAccessException(0);
        }
    }

    @Override
    public List<Tag> findMostCostEffectiveTagByUser(long userId) {

        Query q = entityManager.createNativeQuery(QueryConstant.MOST_COST_EFFECTIVE_TAG_BY_USER, Tag.class);

        q.setParameter(1, userId);

        return (List<Tag>) q.getResultList();
    }

    @Override
    public List<Tag> findAllSpecified(List<Specification<Tag>> specifications, Sortable<Tag> sortable, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        CriteriaQuery<Tag> cq = criteriaQuery.select(root)
                .groupBy(root.get(ID));
        if (sortable != null) {
            cq.orderBy(sortable.setOrder(root, cq, criteriaBuilder));
        }
        if (specifications != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<Tag> s : specifications) {
                predicates.addAll(s.toPredicates(root, cq, criteriaBuilder));
            }
            cq.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return pageable.setPageAndSize(entityManager.createQuery(cq)).getResultList();
    }

    @Override
    public long countLastPage(List<Specification<Tag>> specifications, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cQuery = builder.createQuery(Long.class);
        Root<Tag> from = cQuery.from(Tag.class);
        CriteriaQuery<Long> select = cQuery.select(builder.count(from));
        if (specifications != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<Tag> s : specifications) {
                predicates.addAll(s.toPredicates(from, cQuery, builder));
            }
            select.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        TypedQuery<Long> typedQuery = entityManager.createQuery(select);
        Long numOfOrders = typedQuery.getSingleResult();
        return numOfOrders % size == 0 ? numOfOrders / size : numOfOrders / size + 1;

    }

    @Override
    public BigDecimal getTagCost(long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
        Root<OrderCertificate> root = criteriaQuery.from(OrderCertificate.class);
        criteriaQuery
                .select(criteriaBuilder.sum(root.get(FIXED_PRICE)))
                .where(criteriaBuilder.equal(root.join(CERTIFICATE).join(TAGS).get(ID), id));
        BigDecimal result = entityManager.createQuery(criteriaQuery).getSingleResult();
        return result != null ? result : new BigDecimal(0);
    }

    @Override
    public BigDecimal getTagCostByUser(long tagId, long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
        Root<OrderCertificate> root = criteriaQuery.from(OrderCertificate.class);
        criteriaQuery
                .select(criteriaBuilder.sum(root.get(FIXED_PRICE)))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(root.join(CERTIFICATE).join(TAGS).get(ID), tagId)),
                        criteriaBuilder.equal(root.join(ORDER).get(USER), userId));
        BigDecimal result = entityManager.createQuery(criteriaQuery).getSingleResult();
        return result != null ? result : new BigDecimal(0);
    }

    @Override
    public long getTagOrdersAmount(long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<OrderCertificate> root = criteriaQuery.from(OrderCertificate.class);
        criteriaQuery.select(criteriaBuilder.count(root.get(FIXED_PRICE)))
                .where(criteriaBuilder.equal(root.join(CERTIFICATE).join(TAGS).get(ID), id));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public long getTagOrdersAmount(long tagId, long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<OrderCertificate> root = criteriaQuery.from(OrderCertificate.class);
        criteriaQuery.select(criteriaBuilder.count(root.get(FIXED_PRICE)))
                .where(criteriaBuilder.and(
                        criteriaBuilder.equal(root.join(CERTIFICATE).join(TAGS).get(ID), tagId)),
                        criteriaBuilder.equal(root.join(ORDER).get(USER), userId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Optional<Tag> findByTitle(String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get(TITLE), title));

        return !entityManager.createQuery(query).getResultList().isEmpty() ?
                Optional.of(entityManager.createQuery(query).getSingleResult()) :
                Optional.empty();
    }
}
