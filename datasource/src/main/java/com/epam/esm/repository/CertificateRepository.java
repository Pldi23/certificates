package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.page.Pageable;
import com.epam.esm.repository.predicate.Specification;
import com.epam.esm.repository.sort.Sortable;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.repository.constant.JpaConstant.ACTIVE_STATUS;
import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.NAME;

@Log4j2
@Repository
public class CertificateRepository implements AbstractCertificateRepository {

    private EntityManager entityManager;

    public CertificateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Optional<GiftCertificate> findById(long id, Boolean isActive) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        if (isActive != null) {
            query.where(criteriaBuilder.and(criteriaBuilder.equal(root.get(ACTIVE_STATUS), isActive),
                    criteriaBuilder.equal(root.get(ID), id)));
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

    @Transactional
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

    /**
     *
     * @param giftCertificates list of certificates to save
     * @return true if all certificates saved successfully, and false if at least one of them already exists
     */
    @Override
    public boolean saveMany(List<GiftCertificate> giftCertificates) {
        return giftCertificates.stream().allMatch(this::saveFunctionally);
    }

    private boolean saveFunctionally(GiftCertificate giftCertificate) {
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("insert_certificates_list_and_return_boolean");
        storedProcedureQuery.registerStoredProcedureParameter("name_in", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("description_in", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("price_in", BigDecimal.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("creation_date_in", Date.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("modification_date_in", Date.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("expiration_date_in", Date.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("active_status_in", Boolean.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("tag_title_list", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("insert_certificates_list_and_return_boolean", Boolean.class, ParameterMode.OUT);

        storedProcedureQuery.setParameter("name_in", giftCertificate.getName());
        storedProcedureQuery.setParameter("description_in", giftCertificate.getDescription());
        storedProcedureQuery.setParameter("price_in", giftCertificate.getPrice());
        Date from = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        storedProcedureQuery.setParameter("creation_date_in", from);
        storedProcedureQuery.setParameter("modification_date_in", null);
        storedProcedureQuery.setParameter("expiration_date_in", Date.from(giftCertificate.getExpirationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        storedProcedureQuery.setParameter("active_status_in", giftCertificate.getActiveStatus());
        storedProcedureQuery.setParameter("tag_title_list", giftCertificate.getTags().stream().map(Tag::getTitle).collect(Collectors.joining(",")));


        storedProcedureQuery.execute();
        return  (boolean) storedProcedureQuery.getOutputParameterValue("insert_certificates_list_and_return_boolean");
    }


    public boolean existsByNames(List<GiftCertificate> giftCertificates) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cQuery = builder.createQuery(Long.class);
        Root<GiftCertificate> from = cQuery.from(GiftCertificate.class);
        CriteriaQuery<Long> select = cQuery.select(builder.count(from));
        select.where(builder.or(giftCertificates.stream().map(giftCertificate ->
                builder.equal(from.get("name"), giftCertificate.getName())).toArray(Predicate[]::new)));
        TypedQuery<Long> typedQuery = entityManager.createQuery(select);
        Long nunOfCertificates = typedQuery.getSingleResult();
        return nunOfCertificates >= 1;
    }

    @Override
    public Optional<GiftCertificate> findByName(String name, Boolean isActive) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        if (isActive != null) {
            query.where(criteriaBuilder.and(criteriaBuilder.equal(root.get(ACTIVE_STATUS), isActive),
                    criteriaBuilder.equal(root.get(NAME), name)));
        } else {
            query.where(criteriaBuilder.equal(root.get(NAME), name));
        }
        List<GiftCertificate> resultList = entityManager.createQuery(query).getResultList();
        return !resultList.isEmpty() ? Optional.of(resultList.get(0)) : Optional.empty();
    }

    @Override
    public List<GiftCertificate> findAllSpecified(List<Specification<GiftCertificate>> specifications, Sortable<GiftCertificate> sortable, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> rootQuery = criteriaQuery.from(GiftCertificate.class);
        if (sortable != null) {
            criteriaQuery.orderBy(sortable.setOrder(rootQuery, criteriaQuery, criteriaBuilder), criteriaBuilder.desc(rootQuery.get(ID)));
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

    @Override
    public long countLastPage(List<Specification<GiftCertificate>> specifications, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cQuery = builder.createQuery(Long.class);
        Root<GiftCertificate> from = cQuery.from(GiftCertificate.class);
        CriteriaQuery<Long> select = cQuery.select(builder.count(from));
        if (specifications != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<GiftCertificate> s : specifications) {
                predicates.addAll(s.toPredicates(from, cQuery, builder));
            }
            select.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        TypedQuery<Long> typedQuery = entityManager.createQuery(select);
        Long numOfOrders = typedQuery.getSingleResult();
        return numOfOrders % size == 0 ? numOfOrders / size : numOfOrders / size + 1;
    }
}
