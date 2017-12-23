package net.plshark.notes

import spock.lang.*
import java.time.OffsetDateTime

class ErrorResponseSpec extends Specification {

    def "constructor sets correct fields"() {
        when:
        ErrorResponse response = ErrorResponse.create(400, "status", "something happened", "1/2/3")

        then:
        response.status == 400
        response.statusDetail == "status"
        response.message == "something happened"
        response.path == "1/2/3"
        response.timestamp != null
    }

    def "full constructor sets correct fields"() {
        when:
        ErrorResponse response = ErrorResponse.create(OffsetDateTime.now(), 400, "status", "something happened", "1/2/3")

        then:
        response.status == 400
        response.statusDetail == "status"
        response.message == "something happened"
        response.path == "1/2/3"
        response.timestamp != null
    }
}
