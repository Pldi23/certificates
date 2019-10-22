package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.SearchCriteria;
import com.epam.esm.repository.predicate.CriteriaBuilderHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

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

import static com.epam.esm.repository.constant.JpaConstant.*;

@Repository
@Log4j2
public class EMCertificateRepository implements AbstractCertificateRepository {

    private EntityManager entityManager;
    private CriteriaBuilderHelper certificateNamePredicate;

    public EMCertificateRepository(EntityManager entityManager, CriteriaBuilderHelper certificateNamePredicate) {
        this.entityManager = entityManager;
        this.certificateNamePredicate = certificateNamePredicate;
    }

    @Override
    public List<GiftCertificate> findAll(String sortParam, int page, int size, boolean isActive) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        if (isActive) {
            query.where(criteriaBuilder.equal(root.get(ACTIVE_STATUS), true));
        }
        if (sortParam != null) {
            query.orderBy(sortParam.startsWith("-") ? criteriaBuilder.desc(root.get(refactorColumnName(sortParam.replaceFirst("-", "")))) :
                    criteriaBuilder.asc(root.get(refactorColumnName(sortParam))));
        }
        return entityManager.createQuery(query).setMaxResults(size).setFirstResult(page * size - size).getResultList();
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
    public List<GiftCertificate> findByTagsId(long tagId, String sortParam, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        query.where(
                criteriaBuilder.equal(
                        root.join(TAGS).get(ID), tagId
                )
        );
        if (sortParam != null) {
            query.orderBy(sortParam.startsWith("-") ? criteriaBuilder.desc(root.get(refactorColumnName(sortParam.replaceFirst("-", "")))) :
                    criteriaBuilder.asc(root.get(refactorColumnName(sortParam))));
        }
        return entityManager.createQuery(query).setMaxResults(size).setFirstResult(page * size - size).getResultList();
    }

    @Override
    public List<GiftCertificate> findByCriteria(SearchCriteria searchCriteria, String sortParameter, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> rootQuery = criteriaQuery.from(GiftCertificate.class);
        List<Predicate> predicates = new ArrayList<>();
        certificateNamePredicate.buildSearchCriteriaPredicates(predicates, searchCriteria, criteriaBuilder, rootQuery);
        CriteriaQuery<GiftCertificate> cq =
                criteriaQuery
                        .select(rootQuery)
                        .where(predicates.toArray(new Predicate[predicates.size()]));
        if (sortParameter != null) {
            cq.orderBy(sortParameter.startsWith("-") ? criteriaBuilder.desc(rootQuery.get(refactorColumnName(sortParameter.replaceFirst("-", "")))) :
                    criteriaBuilder.asc(rootQuery.get(refactorColumnName(sortParameter))));
        }

        TypedQuery<GiftCertificate> query = entityManager.createQuery(cq);
        query.setFirstResult(page * size - size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> findByOrder(Long orderId, String sortParameter, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> rootQuery = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(rootQuery);
        criteriaQuery.where(
                criteriaBuilder.equal(rootQuery.join(ORDER_CERTIFICATE).get(ORDER), orderId));
        if (sortParameter != null) {
            criteriaQuery.orderBy(sortParameter.startsWith("-") ? criteriaBuilder.desc(rootQuery.get(refactorColumnName(sortParameter.replaceFirst("-", "")))) :
                    criteriaBuilder.asc(rootQuery.get(refactorColumnName(sortParameter))));
        }
        return entityManager.createQuery(criteriaQuery).setMaxResults(size).setFirstResult(page * size - size).getResultList();
    }

    @Override
    public long count() {
        Query query = entityManager.createNativeQuery("select count(*) from certificate");
        return ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public BigDecimal getPrice(Long id) {
        Query query = entityManager.createNativeQuery("select price from certificate where id = ?");
        query.setParameter(1, id);
        return (BigDecimal) query.getSingleResult();
    }


    private String refactorColumnName(String columnName) {
        String refactored;
        switch (columnName) {
            case REQUEST_EXPIRATION_DATE:
                refactored = EXPIRATION_DATE;
                break;
            case REQUEST_MODIFICATION_DATE:
                refactored = MODIFICATION_DATE;
                break;
            case REQUEST_CREATION_DATE:
                refactored = CREATION_DATE;
                break;
            default:
                refactored = columnName;
        }
        return refactored;
    }
}
