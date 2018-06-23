package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException

import net.plshark.jdbc.RepoTestConfig
import net.plshark.notes.Note
import spock.lang.Specification

@SpringBootTest(classes = RepoTestConfig.class)
class SyncJdbcNotesRepositoryIntSpec extends Specification {

    @Inject
    SyncJdbcNotesRepository repo

    def cleanup() {
        repo.deleteAll()
    }

    def "get retrieves a previously inserted note by ID"() {
        Note insertedNote = repo.insert(new Note("title", "content"))

        when:
        Note note = repo.get(insertedNote.id.get()).get()

        then:
        note == insertedNote
    }

    def "inserting a note returns the inserted note with the ID set"() {
        when:
        Note note = repo.insert(new Note("title", "content"))

        then:
        note.id.isPresent()
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
        repo.get(note.id.get()).get().content == "new content"
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
        repo.delete(inserted.id.get())

        then:
        !repo.get(inserted.id.get()).isPresent()
    }

    def "no exception is thrown when a delete does not affect any rows"() {
        when:
        repo.delete(100)

        then:
        notThrown(Exception)
    }
}
