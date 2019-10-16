package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.SearchCriteria;
import com.epam.esm.repository.hibernate.predicate.CriteriaBuilderHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EMCertificateRepository implements AbstractCertificateRepository {

    private EntityManager entityManager;
    private CriteriaBuilderHelper certificateNamePredicate;

    public EMCertificateRepository(EntityManager entityManager, CriteriaBuilderHelper certificateNamePredicate) {
        this.entityManager = entityManager;
        this.certificateNamePredicate = certificateNamePredicate;
    }

    @Override
    public List<GiftCertificate> findAll(String sortParam, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root)
        .orderBy(sortParam.startsWith("-") ? criteriaBuilder.desc(root.get(refactorColumnName(sortParam.replaceFirst("-", "")))) :
                criteriaBuilder.asc(root.get(refactorColumnName(sortParam))));
        return entityManager.createQuery(query).setMaxResults(size).setFirstResult(page * size - size).getResultList();
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public void deleteById(long id) {
        Optional<GiftCertificate> optional = Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
        if (optional.isPresent()) {
            entityManager.remove(optional.get());
        } else throw new EmptyResultDataAccessException(0);
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
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
                        root.get("name"), name
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
                        root.join("tags").get("id"), tagId
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

    private String refactorColumnName(String columnName) {
        String refactored;
        switch (columnName) {
            case "expirationdate":
                refactored = "expirationDate";
                break;
            case "modificationdate":
                refactored = "modificationDate";
                break;
            case "creationdate":
                refactored = "creationDate";
                break;
            default:
                refactored = columnName;
        }
        return refactored;
    }
}
