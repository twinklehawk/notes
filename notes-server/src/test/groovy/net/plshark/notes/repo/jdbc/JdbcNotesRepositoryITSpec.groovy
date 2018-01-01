package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException
import org.springframework.test.context.ActiveProfiles

import net.plshark.notes.entity.NoteEntity
import net.plshark.notes.webservice.Application
import spock.lang.Specification

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class JdbcNotesRepositoryITSpec extends Specification {

    @Inject
    JdbcNotesRepository repo

    def cleanup() {
        repo.deleteAll()
    }

    def "get retrieves a previously inserted note by ID"() {
        NoteEntity insertedNote = repo.insert(new NoteEntity(1, "title", "content"))

        when:
        NoteEntity note = repo.get(insertedNote.id.asLong)

        then:
        note == insertedNote
    }

    def "inserting a note returns the inserted note with the ID set"() {
        when:
        NoteEntity note = repo.insert(new NoteEntity(1, "title", "content"))

        then:
        note.id.isPresent
        note.ownerId == 1
        note.correlationId == 0
        note.title == "title"
        note.content == "content"
    }

    def "inserting a note with an ID set throws an exception"() {
        when:
        repo.insert(new NoteEntity(1, 1, 0, "title", "content"))

        then:
        thrown(IllegalArgumentException)
    }

    def "a previously inserted note can be updated"() {
        NoteEntity inserted = repo.insert(new NoteEntity(1, "title", "content"))

        when:
        inserted.setContent("new content")
        NoteEntity note = repo.update(inserted)

        then:
        note == inserted
        repo.get(note.id.asLong).content == "new content"
    }

    def "updating a note with no ID throws an exception"() {
        when:
        repo.update(new NoteEntity(1, "title", "content"))

        then:
        thrown(IllegalArgumentException)
    }

    def "when an update affects 0 rows an exception is thrown"() {
        when:
        repo.update(new NoteEntity(100, 1, 0, "title", "content"))

        then:
        thrown(JdbcUpdateAffectedIncorrectNumberOfRowsException)
    }

    def "a previously inserted note can be deleted"() {
        NoteEntity inserted = repo.insert(new NoteEntity(1, "title", "content"))

        when:
        repo.delete(inserted.id.asLong)
        repo.get(inserted.id.asLong)

        then: "get should throw an exception since the row should be gone"
        thrown(EmptyResultDataAccessException)
    }

    def "no exception is thrown when a delete does not affect any rows"() {
        when:
        repo.delete(100)

        then:
        notThrown(Exception)
    }
}
