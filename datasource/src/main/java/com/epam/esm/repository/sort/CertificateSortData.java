package com.epam.esm.repository.sort;

import com.epam.esm.entity.GiftCertificate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import static com.epam.esm.repository.constant.JpaConstant.*;

/**
 * implementation of {@link Sortable} for {@link GiftCertificate}
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateSortData implements Sortable<GiftCertificate> {

    private String sortParameter;

    public CertificateSortData(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    @Override
    public Order setOrder(Root<GiftCertificate> root, CriteriaQuery<GiftCertificate> query, CriteriaBuilder cb) {
        if (sortParameter != null) {
            return sortParameter.startsWith("-") ? cb.asc(root.get(refactorColumnName(sortParameter.replaceFirst("-", "")))) :
                    cb.desc(root.get(refactorColumnName(sortParameter)));
        }
        return null;
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
