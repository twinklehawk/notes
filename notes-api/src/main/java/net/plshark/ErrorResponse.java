package net.plshark;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import net.plshark.AutoValue_ErrorResponse;

/**
 * Response containing information about an exception
 */
@AutoValue
public abstract class ErrorResponse {

    /**
     * Create an instance with a date and time of now
     * @param status the response status code
     * @param statusDetail the response status description
     * @param message the error detail message
     * @param path the path of the request that caused the error
     * @return the ErrorResponse instance
     */
    public static ErrorResponse create(int status, String statusDetail, String message, String path) {
        return create(OffsetDateTime.now(), status, statusDetail, message, path);
    }

    /**
     * Create an instance
     * @param timestamp the date and time when the exception happened
     * @param status the response status code
     * @param statusDetail the response status description
     * @param message the error detail message
     * @param path the path of the request that caused the error
     * @return the ErrorResponse instance
     */
    @JsonCreator
    public static ErrorResponse create(@JsonProperty("date") OffsetDateTime timestamp, @JsonProperty("status") int status,
            @JsonProperty("error") String statusDetail, @JsonProperty("message") String message,
            @JsonProperty("path") String path) {
        return new AutoValue_ErrorResponse(timestamp, status, statusDetail, message, path);
    }

    /**
     * @return the date and time when the exception happened
     */
    public abstract OffsetDateTime getTimestamp();

    /**
     * @return the response status code
     */
    public abstract int getStatus();

    /**
     * @return the response status description
     */
    public abstract String getStatusDetail();

    /**
     * @return the error detail message
     */
    public abstract String getMessage();

    /**
     * @return the path of the request that caused the error
     */
    public abstract String getPath();
}
