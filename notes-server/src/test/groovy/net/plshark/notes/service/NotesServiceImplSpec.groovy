package net.plshark.notes.service

import org.springframework.dao.EmptyResultDataAccessException

import net.plshark.notes.Note
import net.plshark.notes.ObjectNotFoundException
import net.plshark.notes.entity.NoteEntity
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
        repo.insert(_) >> new NoteEntity(1, 2, 3, "", "")

        when:
        Note note = service.save(new Note("", ""))

        then: "should insert since the note had no ID"
        note == new Note(1, 3, "", "")
    }

    def "notes with ID should be updated"() {
        repo.get(4) >> new NoteEntity(4, 1, 3, "title", "content")
        repo.update(new NoteEntity(4, 1, 2, "", "")) >> new NoteEntity(4, 1, 2, "", "")

        when:
        Note note = service.save(new Note(4, 2, "", ""))

        then: "should update since the ID was set"
        note == new Note(4, 2, "", "")
    }

    def "retrieving a note by ID should pass the ID through"() {
        repo.get(100) >> new NoteEntity(100, 2, 3, "title", "content")

        when:
        Note note = service.get(100)

        then:
        note == new Note(100, 3, "title", "content")
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
