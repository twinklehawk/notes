package net.plshark.notes.service

import org.springframework.dao.EmptyResultDataAccessException

import net.plshark.notes.Note
import net.plshark.notes.ObjectNotFoundException
import net.plshark.notes.repo.NotesRepository
import spock.lang.Specification

class NotesServiceImplSpec extends Specification {

    NotesRepository repo = Mock()
    NotesServiceImpl service = new NotesServiceImpl(repo)

    def "constructor does not accept null"() {
        when:
        new NotesServiceImpl(null)

        then:
        thrown(NullPointerException)
    }

    def "notes with no ID should be inserted"() {
        when:
        service.save(new Note(1, 2, "", ""))

        then: "should insert since the note had no ID"
        1 * repo.insert(_)
    }

    def "notes with ID should be updated"() {
        when:
        service.save(new Note(4, 1, 2, "", ""))

        then: "should update since the ID was set"
        1 * repo.update(_)
    }

    def "retrieving a note by ID should pass the ID through"() {
        repo.get(100) >> new Note(100, 2, 3, "title", "content")

        when:
        Note note = service.get(100)

        then:
        note == new Note(100, 2, 3, "title", "content")
    }

    def "attempting to retrieve a note that does not exist should throw an ObjectNotFoundException"() {
        repo.get(100) >> { throw new EmptyResultDataAccessException(1) }

        when:
        service.get(100)

        then:
        thrown(ObjectNotFoundException)
    }

    def "deleting a note should pass the ID through"() {
        when:
        service.delete(100)

        then:
        1 * repo.delete(100)
    }
}
