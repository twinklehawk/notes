package net.plshark.notes

import spock.lang.Specification

class UserSpec extends Specification {

    def "constructors set id to empty"() {
        User user

        when:
        user = new User()

        then:
        user.id != null
        !user.id.isPresent

        when:
        user = new User("name", "pass")

        then:
        user.id != null
        !user.id.isPresent
    }
}
