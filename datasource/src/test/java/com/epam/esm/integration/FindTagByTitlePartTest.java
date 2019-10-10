package com.epam.esm.integration;

import com.epam.esm.DatabaseSetupExtension;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.specification.FindTagByTitlePartSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FindTagByTitlePartTest extends DatabaseSetupExtension {

    @Autowired
    private AbstractTagRepository tagRepository;

    @Test
    public void testSpecification() {
        int actual = tagRepository.query(new FindTagByTitlePartSpecification("a")).size();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }
}
