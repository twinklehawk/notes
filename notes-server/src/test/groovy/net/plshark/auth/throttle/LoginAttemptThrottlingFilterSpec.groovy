package net.plshark.auth.throttle

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import spock.lang.Specification

class LoginAttemptThrottlingFilterSpec extends Specification {

    LoginAttemptService service = Mock()
    UsernameExtractor extractor = Mock()
    LoginAttemptThrottlingFilter filter = new LoginAttemptThrottlingFilter(service, extractor)
    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    FilterChain chain = Mock()

    def "constructor does not accept nulls"() {
        when:
        new LoginAttemptThrottlingFilter(null, extractor)

        then:
        thrown(NullPointerException)

        when:
        new LoginAttemptThrottlingFilter(service, null)

        then:
        thrown(NullPointerException)
    }

    def "should pull the correct IP and username when the forwarded header is not set"() {
        request.getHeader("X-Forwarded-For") >> null
        request.getRemoteAddr() >> "192.168.1.2"
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> false
        service.isUsernameBlocked("test-user") >> false

        when:
        filter.doFilter(request, response, chain)

        then:
        1 * chain.doFilter(request, response)
    }

    def "should pull the correct IP and username when the forwarded header is set"() {
        request.getHeader("X-Forwarded-For") >> "192.168.1.2"
        request.getRemoteAddr() >> null
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> false
        service.isUsernameBlocked("test-user") >> false

        when:
        filter.doFilter(request, response, chain)

        then:
        1 * chain.doFilter(request, response)
    }

    def "should block the request if the username is blocked"() {
        request.getRemoteAddr() >> "192.168.1.2"
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> false
        service.isUsernameBlocked("test-user") >> true

        when:
        filter.doFilter(request, response, chain)

        then:
        0 * chain.doFilter(request, response)
        1 * response.sendError(429)
    }

    def "should block the request if the IP is blocked"() {
        request.getRemoteAddr() >> "192.168.1.2"
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> true
        service.isUsernameBlocked("test-user") >> false

        when:
        filter.doFilter(request, response, chain)

        then:
        0 * chain.doFilter(request, response)
        1 * response.sendError(429)
    }

    def "should not check the username if the request does not include a username"() {
        request.getRemoteAddr() >> "192.168.1.2"
        extractor.extractUsername(request) >> Optional.empty()
        service.isIpBlocked("192.168.1.2") >> false

        when:
        filter.doFilter(request, response, chain)

        then:
        0 * service.isUsernameBlocked(_)
        1 * chain.doFilter(request, response)
    }
}
