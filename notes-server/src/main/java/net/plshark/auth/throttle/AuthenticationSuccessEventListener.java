package net.plshark.auth.throttle;

import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Listener that passes information from successful logins to a LoginAttemptService
 */
@Named
@Singleton
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAttemptService service;

    /**
     * Create new instance
     * @param service the service to inform of successful logins
     */
    public AuthenticationSuccessEventListener(LoginAttemptService service) {
        this.service = Objects.requireNonNull(service, "service cannot be null");
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        service.onLoginSucceeded(auth.getName(), details.getRemoteAddress());
    }
}
