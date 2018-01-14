package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException
import org.springframework.test.context.ActiveProfiles

import net.plshark.notes.Note
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
        Note insertedNote = repo.insert(new Note("title", "content"))

        when:
        Note note = repo.get(insertedNote.id.asLong).get()

        then:
        note == insertedNote
    }

    def "inserting a note returns the inserted note with the ID set"() {
        when:
        Note note = repo.insert(new Note("title", "content"))

        then:
        note.id.isPresent
        note.correlationId == 0
        note.title == "title"
        note.content == "content"
    }

    def "inserting a note with an ID set throws an exception"() {
        when:
        repo.insert(new Note(1, 0, "title", "content"))

        then:
        thrown(IllegalArgumentException)
    }

    def "a previously inserted note can be updated"() {
        Note inserted = repo.insert(new Note("title", "content"))

        when:
        inserted.setContent("new content")
        Note note = repo.update(inserted)

        then:
        note == inserted
        repo.get(note.id.asLong).get().content == "new content"
    }

    def "updating a note with no ID throws an exception"() {
        when:
        repo.update(new Note("title", "content"))

        then:
        thrown(IllegalArgumentException)
    }

    def "when an update affects 0 rows an exception is thrown"() {
        when:
        repo.update(new Note(100, 0, "title", "content"))

        then:
        thrown(JdbcUpdateAffectedIncorrectNumberOfRowsException)
    }

    def "a previously inserted note can be deleted"() {
        Note inserted = repo.insert(new Note("title", "content"))

        when:
        repo.delete(inserted.id.asLong)

        then:
        !repo.get(inserted.id.asLong).isPresent()
    }

    def "no exception is thrown when a delete does not affect any rows"() {
        when:
        repo.delete(100)

        then:
        notThrown(Exception)
    }
}
