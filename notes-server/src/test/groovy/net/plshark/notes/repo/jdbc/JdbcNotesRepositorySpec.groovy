package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest

import net.plshark.notes.Note
import net.plshark.notes.webservice.Application
import spock.lang.Specification

class JdbcNotesRepositorySpec extends Specification {

    def "constructor does not accept null args"() {
        when:
        new JdbcNotesRepository(null)

        then:
        thrown(NullPointerException)
    }
}
