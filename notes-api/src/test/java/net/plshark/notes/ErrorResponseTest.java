package net.plshark.notes;

import java.time.OffsetDateTime;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link ErrorResponse}
 */
public class ErrorResponseTest {

    /**
     * Verify the correct fields are set by the constructors
     */
    @Test
    public void fieldOrderTest() {
        ErrorResponse response = ErrorResponse.create(400, "status", "something happened", "1/2/3");
        Assert.assertNotNull(response.getTimestamp());
        Assert.assertEquals(400, response.getStatus());
        Assert.assertEquals("status", response.getStatusDetail());
        Assert.assertEquals("something happened", response.getMessage());
        Assert.assertEquals("1/2/3", response.getPath());

        response = ErrorResponse.create(OffsetDateTime.now(), 400, "status", "something happened", "1/2/3");
        Assert.assertNotNull(response.getTimestamp());
        Assert.assertEquals(400, response.getStatus());
        Assert.assertEquals("status", response.getStatusDetail());
        Assert.assertEquals("something happened", response.getMessage());
        Assert.assertEquals("1/2/3", response.getPath());
    }
}
