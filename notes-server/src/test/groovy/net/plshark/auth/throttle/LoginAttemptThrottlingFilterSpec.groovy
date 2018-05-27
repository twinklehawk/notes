package net.plshark.auth.throttle

import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain

import spock.lang.Specification

class LoginAttemptThrottlingFilterSpec extends Specification {

    LoginAttemptService service = Mock()
    UsernameExtractor extractor = Mock()
    LoginAttemptThrottlingFilter filter = new LoginAttemptThrottlingFilter(service, extractor)
    ServerHttpRequest request = Mock()
    ServerHttpResponse response = Mock()
    ServerWebExchange exchange = Mock()
    WebFilterChain chain = Mock()

    def setup() {
        exchange.getRequest() >> request
        exchange.getResponse() >> response
    }

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
        request.getHeaders().getFirst("X-Forwarded-For") >> null
        request.getRemoteAddress() >> InetSocketAddress.createUnresolved("192.168.1.2", 80)
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> false
        service.isUsernameBlocked("test-user") >> false

        when:
        filter.filter(exchange, chain)

        then:
        1 * chain.filter(exchange)
    }

    def "should pull the correct IP and username when the forwarded header is set"() {
        request.getHeaders().getFirst("X-Forwarded-For") >> "192.168.1.2"
        request.getRemoteAddress() >> null
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> false
        service.isUsernameBlocked("test-user") >> false

        when:
        filter.filter(exchange, chain)

        then:
        1 * chain.filter(exchange)
    }

    def "should block the request if the username is blocked"() {
        request.getRemoteAddress() >> InetSocketAddress.createUnresolved("192.168.1.2", 80)
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> false
        service.isUsernameBlocked("test-user") >> true

        when:
        filter.filter(exchange, chain)

        then:
        0 * chain.filter(exchange)
        1 * response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS)
    }

    def "should block the request if the IP is blocked"() {
        request.getRemoteAddress() >> InetSocketAddress.createUnresolved("192.168.1.2", 80)
        extractor.extractUsername(request) >> Optional.of("test-user")
        service.isIpBlocked("192.168.1.2") >> true
        service.isUsernameBlocked("test-user") >> false

        when:
        filter.filter(exchange, chain)

        then:
        0 * chain.filter(exchange)
        1 * response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS)
    }

    def "should not check the username if the request does not include a username"() {
        request.getRemoteAddress() >> InetSocketAddress.createUnresolved("192.168.1.2", 80)
        extractor.extractUsername(request) >> Optional.empty()
        service.isIpBlocked("192.168.1.2") >> false

        when:
        filter.filter(exchange, chain)

        then:
        0 * service.isUsernameBlocked(_)
        1 * chain.filter(exchange)
    }
}
