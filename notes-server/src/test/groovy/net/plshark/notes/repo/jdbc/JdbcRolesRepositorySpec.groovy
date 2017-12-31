package net.plshark.notes.repo.jdbc

import spock.lang.Specification

class JdbcRolesRepositorySpec extends Specification {

    def "constructor does not accept null args"() {
        when:
        new JdbcRolesRepository(null)

        then:
        thrown(NullPointerException)
    }
}
