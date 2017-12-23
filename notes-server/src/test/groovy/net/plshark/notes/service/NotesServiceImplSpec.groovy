package net.plshark.notes.service

import net.plshark.notes.Note
import net.plshark.notes.repo.NotesRepository
import spock.lang.Specification

class NotesServiceImplSpec extends Specification {

    def "constructor does not accept null"() {
        when:
        new NotesServiceImpl(null)

        then:
        thrown(NullPointerException)
    }

    def "notes with no ID should be inserted"() {
        NotesRepository repo = Mock()
        NotesServiceImpl service = new NotesServiceImpl(repo)

        when:
        service.save(new Note(1, 2, "", ""))

        then: "should insert since the note had no ID"
        1 * repo.insert(_)
    }

    def "notes with ID should be updated"() {
        NotesRepository repo = Mock()
        NotesServiceImpl service = new NotesServiceImpl(repo)

        when:
        service.save(new Note(4, 1, 2, "", ""))

        then: "should update since the ID was set"
        1 * repo.update(_)
    }
}
