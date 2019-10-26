package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
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

import static com.epam.esm.repository.constant.JpaConstant.ACTIVE_STATUS;
import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.NAME;

@Repository
public class CertificateRepository implements AbstractCertificateRepository {

    private EntityManager entityManager;

    public CertificateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Optional<GiftCertificate> findById(long id, boolean isActive) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        if (isActive) {
            query.where(criteriaBuilder.and(criteriaBuilder.equal(root.get(ACTIVE_STATUS), true), criteriaBuilder.equal(root.get(ID), id)));
        } else {
            query.where(criteriaBuilder.equal(root.get(ID), id));
        }
        List<GiftCertificate> resultList = entityManager.createQuery(query).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    @Override
    public void deleteById(long id) {
        Optional<GiftCertificate> optional = Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
        if (optional.isPresent()) {
            GiftCertificate giftCertificate = optional.get();
            giftCertificate.setActiveStatus(false);
            entityManager.merge(giftCertificate);
        } else throw new EmptyResultDataAccessException(0);
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        giftCertificate.setActiveStatus(true);
        if (giftCertificate.getId() == null) {
            entityManager.persist(giftCertificate);
            return giftCertificate;
        } else {
            return entityManager.merge(giftCertificate);
        }
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        query.where(
                criteriaBuilder.equal(
                        root.get(NAME), name
                )
        );
        List<GiftCertificate> resultList = entityManager.createQuery(query).getResultList();
        return !resultList.isEmpty() ? Optional.of(resultList.get(0)) : Optional.empty();
    }

    @Override
    public List<GiftCertificate> findAllSpecified(List<Specification<GiftCertificate>> specifications, Sortable<GiftCertificate> sortable, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> rootQuery = criteriaQuery.from(GiftCertificate.class);
        if (sortable != null) {
            criteriaQuery.orderBy(sortable.setOrder(rootQuery, criteriaQuery, criteriaBuilder));
        }
        if (specifications != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<GiftCertificate> s : specifications) {
                predicates.addAll(s.toPredicates(rootQuery, criteriaQuery, criteriaBuilder));
            }
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return pageable.setPageAndSize(entityManager.createQuery(criteriaQuery)).getResultList();
    }
}
