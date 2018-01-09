package net.plshark.notes.service

import net.plshark.notes.Note
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

    def "notes with no ID should be inserted and owner ID set to current user"() {
        repo.insert({ NoteEntity entity -> entity.ownerId == 2 }) >> new NoteEntity(1, 2, 3, "", "")

        when:
        Note note = service.save(new Note("", ""), 2)

        then: "should insert since the note had no ID"
        note == new Note(1, 3, "", "")
    }

    def "notes with ID should be updated"() {
        repo.get(4) >> new NoteEntity(4, 1, 3, "title", "content")
        repo.update(new NoteEntity(4, 1, 2, "", "")) >> new NoteEntity(4, 1, 2, "", "")

        when:
        Note note = service.save(new Note(4, 2, "", ""), 1)

        then: "should update since the ID was set"
        note == new Note(4, 2, "", "")
    }

    def "retrieving a note by ID for a user should pass the IDs through"() {
        repo.getByIdForUser(100, 200) >> Optional.of(new NoteEntity(100, 2, 3, "title", "content"))

        when:
        Optional<Note> note = service.getForUser(100, 200)

        then:
        note.get() == new Note(100, 3, "title", "content")
    }

    def "attempting to retrieve a note that does not exist should return an empty optional"() {
        repo.getByIdForUser(100, 200) >> { Optional.empty() }

        when:
        Optional<Note> note = service.getForUser(100, 200)

        then:
        !note.isPresent()
    }

    def "deleting a note should pass the ID through"() {
        when:
        service.deleteForUser(100, 200)

        then:
        1 * repo.deleteByIdForUser(100, 200)
    }
}
