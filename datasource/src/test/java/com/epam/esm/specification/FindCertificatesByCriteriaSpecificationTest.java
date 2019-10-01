package com.epam.esm.specification;

import com.epam.esm.config.DataSourceConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.*;
import com.epam.esm.DatabaseSetupExtension;
import com.epam.esm.repository.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class})
public class FindCertificatesByCriteriaSpecificationTest extends DatabaseSetupExtension {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    @Qualifier("CertificateRepository")
    private Repository<GiftCertificate> certificateRepository;

    @Test
    public void testIdCriteria() {
        IdCriteria idCriteria = new IdCriteria(ParameterSearchType.IN, List.of(2L,4L, 3L));
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .withIdCriteria(idCriteria)
                .build();
        SqlSpecification sqlSpecification = new FindCertificatesByCriteriaSpecification(searchCriteria, null, null);
        log.debug(sqlSpecification.sql());
        assertEquals(2, certificateRepository.query(sqlSpecification).size());
    }

    @Test
    public void testNameCriteria() {
        NameCriteria nameCriteriaLike = new NameCriteria(TextSearchType.LIKE, List.of("s"));
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .withNameCriteria(nameCriteriaLike)
                .build();
        SqlSpecification sqlSpecification = new FindCertificatesByCriteriaSpecification(searchCriteria, null, null);
        log.debug(sqlSpecification.sql());
        assertEquals(2, certificateRepository.query(sqlSpecification).size());
    }

    @Test
    public void testSixCriteriaAndSort() {
        NameCriteria nameCriteriaIn = new NameCriteria(TextSearchType.IN, List.of("sport car","bike", "skydiving"));
        IdCriteria idCriteria = new IdCriteria(ParameterSearchType.IN, List.of(2L,1L, 3L));
        DescriptionCriteria descriptionCriteria = new DescriptionCriteria(TextSearchType.NOT_LIKE, List.of("description"));
        ExpirationDateCriteria expirationDateCriteria = new ExpirationDateCriteria(ParameterSearchType.BETWEEN,
                List.of(LocalDate.of(2019,1,1), LocalDate.of(2021,1,1)));
        ModificationDateCriteria modificationDateCriteria = new ModificationDateCriteria(ParameterSearchType.NOT_BETWEEN,
                List.of(LocalDate.of(2017,1,1), LocalDate.of(2018,1,1)));
        CreationDateCriteria creationDateCriteria = new CreationDateCriteria(ParameterSearchType.NOT_IN, List.of());
        PriceCriteria priceCriteria = new PriceCriteria(ParameterSearchType.BETWEEN, List.of(new BigDecimal(0), new BigDecimal(1000)));
        TagCriteria tagCriteria = new TagCriteria(ParameterSearchType.NOT_IN, List.of(150L));

        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .withNameCriteria(nameCriteriaIn)
                .withIdCriteria(idCriteria)
                .withDescriptionCriteria(descriptionCriteria)
                .withExpirationDateCriteria(expirationDateCriteria)
                .withCreationDateCriteria(creationDateCriteria)
                .withModificationDateCriteria(modificationDateCriteria)
                .withPriceCriteria(priceCriteria)
                .withTagCriteria(tagCriteria)
                .build();

        LimitOffsetCriteria limitOffsetCriteria = new LimitOffsetCriteria.Builder()
                .withLimit(20)
                .withOffset(0)
                .build();

        SortCriteria sortCriteria = new SortCriteria.Builder()
                .withIsAscending(true)
                .withCriterias(List.of("id"))
                .build();

        SqlSpecification sqlSpecification = new FindCertificatesByCriteriaSpecification(searchCriteria, sortCriteria, limitOffsetCriteria);
        List<GiftCertificate> actual = certificateRepository.query(sqlSpecification);
        log.debug(sqlSpecification.sql());
        log.debug(actual);
        assertEquals(3, actual.size());
    }

    @Test
    public void testCreationDateCriteria() {
        CreationDateCriteria creationDateCriteria = new CreationDateCriteria(ParameterSearchType.BETWEEN,
                List.of(LocalDate.of(2019,1,1), LocalDate.of(2021,1,1)));
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .withCreationDateCriteria(creationDateCriteria)
                .build();
        SqlSpecification sqlSpecification = new FindCertificatesByCriteriaSpecification(searchCriteria, null, null);
        log.debug(sqlSpecification.sql());
        assertEquals(3, certificateRepository.query(sqlSpecification).size());

    }

}
