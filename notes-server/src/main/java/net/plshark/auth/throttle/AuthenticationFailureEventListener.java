package net.plshark.auth.throttle;

import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Listener that passes information on failed login attempts to a LoginAttemptService
 */
@Named
@Singleton
public class AuthenticationFailureEventListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService service;

    /**
     * Create a new instance
     * @param service the service to inform of failed logins
     */
    public AuthenticationFailureEventListener(LoginAttemptService service) {
        this.service = Objects.requireNonNull(service, "service cannot be null");
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Authentication auth = event.getAuthentication();
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        service.onLoginFailed(auth.getName(), details.getRemoteAddress());
    }
}
