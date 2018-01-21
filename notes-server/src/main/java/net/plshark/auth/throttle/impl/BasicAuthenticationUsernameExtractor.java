package net.plshark.auth.throttle.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.plshark.auth.throttle.UsernameExtractor;

/**
 * Username extractor for requests using basic authentication
 */
public class BasicAuthenticationUsernameExtractor implements UsernameExtractor {

    private final Logger log = LoggerFactory.getLogger(BasicAuthenticationUsernameExtractor.class);

    @Override
    public Optional<String> extractUsername(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization")).flatMap(header -> extractUsername(header));
    }

    /**
     * Extract the username from the Authorization header value
     * @param header the header value
     * @return the username or empty if not found
     */
    private Optional<String> extractUsername(String header) {
        Optional<String> username = Optional.empty();

        if (header.startsWith("Basic ")) {
            try {
                byte[] base64Auth = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decoded = Base64.getDecoder().decode(base64Auth);
                String auth = new String(decoded, StandardCharsets.UTF_8);
                int colonIndex = auth.indexOf(':');
                if (colonIndex != -1)
                    username = Optional.of(auth.substring(0, colonIndex));
            } catch (IllegalArgumentException e) {
                log.debug("invalid base64 encoding in Authorization header", e);
                username = Optional.empty();
            }
        }

        return username;
    }
}
