package net.plshark.notes

import spock.lang.Specification

class RoleSpec extends Specification {

    def "default constructor sets id to empty and sets name"() {
        when:
        Role role = new Role("name")

        then:
        role.id != null
        !role.id.isPresent
        role.name == "name"
    }

    def "two-arg constructor sets id and name"() {
        when:
        Role role = new Role(12, "name")

        then:
        role.id.isPresent
        role.id.asLong == 12
        role.name == "name"
    }

    def "name cannot be set to null"() {
        when:
        new Role(null)

        then:
        thrown(NullPointerException)

        when:
        Role role = new Role("name")
        role.name = null

        then:
        thrown(NullPointerException)
    }
}
