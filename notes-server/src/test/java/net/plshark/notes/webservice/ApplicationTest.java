package net.plshark.notes.webservice;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests for {@link Application}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTest {

    @Inject
    private Application application;

    // TODO make this an integration test instead of unit test
    /**
     * Verify the application context can be built
     */
    @Test
    public void contextTest() {
        Assert.assertNotNull(application);
    }
}
