package net.plshark.auth.throttle.impl

import java.nio.charset.StandardCharsets

import org.springframework.http.server.reactive.ServerHttpRequest

import spock.lang.Specification

class BasicAuthenticationUsernameExtractorSpec extends Specification {

    ServerHttpRequest request = Mock()
    BasicAuthenticationUsernameExtractor extractor = new BasicAuthenticationUsernameExtractor()

    def "valid basic auth returns the username"() {
        request.getHeaders.getFirst("Authorization") >> "Basic dGVzdC11c2VyOnBhc3N3b3Jk"

        expect:
        extractor.extractUsername(request).get() == "test-user"
    }

    def "no Authorization header returns an empty optional"() {
        request.getHeaders.getFirst("Authorization") >> null

        expect:
        !extractor.extractUsername(request).isPresent()

    }

    def "Authorization header not starting with Basic returns an empty optional"() {
        request.getHeaders.getFirst("Authorization") >> "dGVzdC11c2VyOnBhc3N3b3Jk"

        expect:
        !extractor.extractUsername(request).isPresent()

    }

    def "invalid base64 encoding in auth header value returns an empty optional"() {
        request.getHeaders.getFirst("Authorization") >> "Basic 1234"

        expect:
        !extractor.extractUsername(request).isPresent()

    }

    def "no colon in auth header value returns an empty optional"() {
        String result = new String(Base64.getEncoder().encode("test-user".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        request.getHeaders.getFirst("Authorization") >> result

        expect:
        !extractor.extractUsername(request).isPresent()
    }
}
