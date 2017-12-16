package net.plshark.notes.webservice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.plshark.notes.BadRequestException;
import net.plshark.notes.ErrorResponse;
import net.plshark.notes.ObjectNotFoundException;

/**
 * Controller advice for handling exceptions
 */
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    /**
     * Handle a BadRequestException
     * @param e the exception
     * @param request the request that caused the exception
     * @return the response to return to the client
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e, HttpServletRequest request) {
        log.debug("Bad request", e);
        return ResponseEntity.badRequest().body(buildResponse(HttpStatus.BAD_REQUEST, e, request.getRequestURI()));
    }

    /**
     * Handle an ObjectNotFoundException
     * @param e the exception
     * @param request the request that caused the exception
     * @return the response to return to the client
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        log.debug("Object not found", e);
        return ResponseEntity.notFound().build();
    }

    /**
     * Handle a Throwable
     * @param t the throwable
     * @param request the request that caused the throwable
     * @return the response to return to the client
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleThrowable(Throwable t, HttpServletRequest request) {
        log.error("Internal error", t);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, t, request.getRequestURI()));
    }

    private ErrorResponse buildResponse(HttpStatus status, Throwable e, String path) {
        return ErrorResponse.create(status.value(), status.getReasonPhrase(), e.getMessage(), path);
    }
}
