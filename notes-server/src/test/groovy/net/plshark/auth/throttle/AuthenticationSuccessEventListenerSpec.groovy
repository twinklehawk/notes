package net.plshark.auth.throttle

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.WebAuthenticationDetails

import spock.lang.Specification

class AuthenticationSuccessEventListenerSpec extends Specification {

    LoginAttemptService service = Mock()
    AuthenticationSuccessEventListener listener = new AuthenticationSuccessEventListener(service)

    def "constructor does not accept nulls"() {
        when:
        new AuthenticationSuccessEventListener(null)

        then:
        thrown(NullPointerException)
    }

    def "successful login attempts should notify the service"() {
        Authentication auth = Mock()
        WebAuthenticationDetails details = Mock()
        auth.getDetails() >> details
        auth.getName() >> "test-user"
        details.getRemoteAddress() >> "192.168.1.2"

        when:
        listener.onApplicationEvent(new AuthenticationSuccessEvent(auth))

        then:
        1 * service.onLoginSucceeded("test-user", "192.168.1.2")
    }
}
