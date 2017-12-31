package net.plshark.notes.repo.jdbc

import spock.lang.Specification

class JdbcUsersRepositorySpec extends Specification {

    def "constructor does not accept null args"() {
        when:
        new JdbcUsersRepository(null)

        then:
        thrown(NullPointerException)
    }
}
