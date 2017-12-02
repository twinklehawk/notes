package net.plshark.notes.webservice;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.plshark.notes.ErrorResponse;

/**
 * Tests for {@link ExceptionHandlerControllerAdvice}
 */
public class ExceptionHandlerControllerAdviceTest {

    /**
     * Verify the correct response is built for a BadRequestException
     */
    @Test
    public void handleBadRequestTest() {
        HttpServletRequest request = Mockito.when(Mockito.mock(HttpServletRequest.class).getRequestURI())
                .thenReturn("http://test/url").getMock();
        ExceptionHandlerControllerAdvice advice = new ExceptionHandlerControllerAdvice();

        ResponseEntity<ErrorResponse> response = advice.handleBadRequest(new BadRequestException("bad request"),
                request);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("bad request", response.getBody().getMessage());
        Assert.assertEquals("http://test/url", response.getBody().getPath());
        Assert.assertEquals(400, response.getBody().getStatus());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), response.getBody().getStatusDetail());
        Assert.assertNotNull(response.getBody().getTimestamp());
    }

    /**
     * Verify the correct response is built for a Throwable
     */
    @Test
    public void handleThrowableTest() {
        HttpServletRequest request = Mockito.when(Mockito.mock(HttpServletRequest.class).getRequestURI())
                .thenReturn("http://test/url").getMock();
        ExceptionHandlerControllerAdvice advice = new ExceptionHandlerControllerAdvice();

        ResponseEntity<ErrorResponse> response = advice.handleThrowable(new Exception("whatever"), request);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assert.assertEquals("whatever", response.getBody().getMessage());
        Assert.assertEquals("http://test/url", response.getBody().getPath());
        Assert.assertEquals(500, response.getBody().getStatus());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), response.getBody().getStatusDetail());
        Assert.assertNotNull(response.getBody().getTimestamp());
    }
}
