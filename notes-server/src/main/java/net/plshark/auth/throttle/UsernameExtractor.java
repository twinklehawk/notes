package net.plshark.auth.throttle;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Extracts usernames from requests
 */
public interface UsernameExtractor {

    /**
     * Extract the username from a request
     * @param request the request
     * @return the username or empty if not found
     */
    Optional<String> extractUsername(HttpServletRequest request);
}
