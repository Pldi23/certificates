package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

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
    public List<Tag> findAll() {
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
    public List<Tag> findTagsByCertificate(long certificateId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.where(criteriaBuilder.equal(certificateRoot.get("id"), certificateId));
        Join<GiftCertificate, Tag> certificates = certificateRoot.join("tags");
        CriteriaQuery<Tag> cq = criteriaQuery.select(certificates).distinct(true);
        TypedQuery<Tag> q = entityManager.createQuery(cq);
        return q.getResultList();
    }

    @Override
    public Optional<Tag> findByTitle(String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        query.where(
                criteriaBuilder.equal(
                        root.get("title"), title
                )
        );
        return Optional.ofNullable(entityManager.createQuery(query).getSingleResult());
    }

    public List<Tag> findPaginated(String sort, int page, int size){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = criteriaBuilder
                .createQuery(Long.class);
        countQuery.select(criteriaBuilder
                .count(countQuery.from(Tag.class)));
        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();

        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder
                .createQuery(Tag.class);
        Root<Tag> from = criteriaQuery.from(Tag.class);

        CriteriaQuery<Tag> select = criteriaQuery.select(from).orderBy(criteriaBuilder.asc(from.get(sort)));

        TypedQuery<Tag> typedQuery = entityManager.createQuery(select);
        while (page < count.intValue()) {
            typedQuery.setFirstResult(page - 1);
            typedQuery.setMaxResults(size);
            System.out.println("Current page: " + typedQuery.getResultList());
            page += size;
        }
        return typedQuery.getResultList();
    }
}
