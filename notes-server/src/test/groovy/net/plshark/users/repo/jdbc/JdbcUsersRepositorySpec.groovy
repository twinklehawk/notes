package net.plshark.users.repo.jdbc

import net.plshark.users.repo.jdbc.JdbcUsersRepository
import spock.lang.Specification

class JdbcUsersRepositorySpec extends Specification {

    def "constructor does not accept null args"() {
        when:
        new JdbcUsersRepository(null)

        then:
        thrown(NullPointerException)
    }
}
