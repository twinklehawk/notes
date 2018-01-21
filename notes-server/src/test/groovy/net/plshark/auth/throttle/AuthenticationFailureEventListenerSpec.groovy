package net.plshark.auth.throttle

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.WebAuthenticationDetails

import spock.lang.Specification

class AuthenticationFailureEventListenerSpec extends Specification {

    LoginAttemptService service = Mock()
    AuthenticationFailureEventListener listener = new AuthenticationFailureEventListener(service)

    def "constructor does not accept nulls"() {
        when:
        new AuthenticationFailureEventListener(null)

        then:
        thrown(NullPointerException)
    }

    def "failed login attempts should notify the service"() {
        Authentication auth = Mock()
        WebAuthenticationDetails details = Mock()
        auth.getDetails() >> details
        auth.getName() >> "test-user"
        details.getRemoteAddress() >> "192.168.1.2"

        when:
        listener.onApplicationEvent(new AuthenticationFailureBadCredentialsEvent(auth, new BadCredentialsException("")))

        then:
        1 * service.onLoginFailed("test-user", "192.168.1.2")
    }
}
