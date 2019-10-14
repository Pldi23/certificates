package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class EMCertificateRepository implements AbstractCertificateRepository {

    private EntityManager entityManager;

    public EMCertificateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<GiftCertificate> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
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
    public List<GiftCertificate> findByTagsId(long tagId) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
//        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
//        criteriaQuery.where(criteriaBuilder.equal(tagRoot.get("id"), tagId));
//        Join<Tag, GiftCertificate> tags = tagRoot.join("certificates");
//
//
//        CriteriaQuery<GiftCertificate> cq = criteriaQuery.select(tags).distinct(true);
//        TypedQuery<GiftCertificate> q = entityManager.createQuery(cq);
//        return q.getResultList();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        query.where(
                criteriaBuilder.equal(
                        root.get("tag_id"), tagId
                )
        );
        return entityManager.createQuery(query).getResultList();
    }
}
