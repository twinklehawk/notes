package net.plshark.notes.repo.jdbc

import spock.lang.Specification

class JdbcUserRolesRepositorySpec extends Specification {

    def "constructor does not accept null args"() {
        when:
        new JdbcUserRolesRepository(null)

        then:
        thrown(NullPointerException)
    }
}
