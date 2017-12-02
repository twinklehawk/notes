package net.plshark.notes.webservice;

/**
 * Exception indicating a request was invalid
 */
public class BadRequestException extends Exception {

    private static final long serialVersionUID = 5359694168645391199L;

    /**
     * Create a new instance
     * @param message the detail message
     */
    public BadRequestException(String message) {
        super(message);
    }
}
