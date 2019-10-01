package com.epam.esm.integration;

import com.epam.esm.entity.Tag;
import com.epam.esm.DatabaseSetupExtension;
import com.epam.esm.repository.Repository;
import com.epam.esm.specification.FindTagByTitlePartSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class FindTagByTitlePartTest extends DatabaseSetupExtension {

    @Autowired
    private Repository<Tag> tagRepository;

    @Test
    public void testSpecification() {
        int actual = tagRepository.query(new FindTagByTitlePartSpecification("a")).size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }
}
