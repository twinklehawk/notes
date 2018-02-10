package net.plshark.users

import net.plshark.users.User
import spock.lang.Specification

class UserSpec extends Specification {

    def "constructors set id to empty"() {
        User user

        when:
        user = new User("name", "pass")

        then:
        user.id != null
        !user.id.present
    }
}
