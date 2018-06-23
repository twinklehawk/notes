package net.plshark.notes.repo.jdbc

import spock.lang.Specification

class SyncJdbcNotesRepositorySpec extends Specification {

    def "constructor does not accept null args"() {
        when:
        new SyncJdbcNotesRepository(null)

        then:
        thrown(NullPointerException)
    }
}
