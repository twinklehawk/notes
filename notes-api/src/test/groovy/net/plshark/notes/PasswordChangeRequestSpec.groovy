package net.plshark.notes

import spock.lang.*

class PasswordChangeRequestSpec extends Specification {

    def "constructor sets correct fields"() {
        when:
        PasswordChangeRequest request = PasswordChangeRequest.create("current pass", "new pass")

        then:
        request.currentPassword == "current pass"
        request.newPassword == "new pass"
    }
}
