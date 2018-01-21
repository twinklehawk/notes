package net.plshark.auth.throttle;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Filter that blocks requests from an IP address or for a user if too many requests have been made
 * in a time frame
 */
public class LoginAttemptThrottlingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoginAttemptThrottlingFilter.class);

    private final LoginAttemptService service;
    private final UsernameExtractor usernameExtractor;

    /**
     * Create a new instance
     * @param service the service to track what IPs and usernames are blocked
     * @param usernameExtractor the extractor to retrieve the username from a request
     */
    public LoginAttemptThrottlingFilter(LoginAttemptService service, UsernameExtractor usernameExtractor) {
        this.service = Objects.requireNonNull(service);
        this.usernameExtractor = Objects.requireNonNull(usernameExtractor);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean blocked = false;

        String clientIp = getClientIp(httpRequest);
        if (service.isIpBlocked(getClientIp(httpRequest))) {
            blocked = true;
            log.debug("blocked request from {}", clientIp);
        } else {
            Optional<String> username = getUsername(httpRequest);
            if (username.isPresent() && service.isUsernameBlocked(username.get())) {
                blocked = true;
                log.debug("blocked request for username {}", username.get());
            }
        }

        if (blocked) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Get the IP address that sent a request
     * @param request the request
     * @return the IP address
     */
    private String getClientIp(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Forwarded-For")).map(header -> header.split(",")[0])
                .orElse(request.getRemoteAddr());
    }

    /**
     * Get the username for the authentication in a request
     * @param request the request
     * @return the username or empty if not found
     */
    private Optional<String> getUsername(HttpServletRequest request) {
        return usernameExtractor.extractUsername(request);
    }
}
