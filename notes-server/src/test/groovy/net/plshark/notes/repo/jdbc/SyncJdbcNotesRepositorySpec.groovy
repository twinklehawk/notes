package net.plshark.notes.repo.jdbc

import com.opentable.db.postgres.junit.EmbeddedPostgresRules
import com.opentable.db.postgres.junit.PreparedDbRule
import net.plshark.notes.Note
import net.plshark.testutils.PlsharkFlywayPreparer
import org.junit.Rule
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

class SyncJdbcNotesRepositorySpec extends Specification {

    @Rule
    PreparedDbRule dbRule = EmbeddedPostgresRules.preparedDatabase(PlsharkFlywayPreparer.defaultPreparer())
    SyncJdbcNotesRepository repo

    def setup() {
        repo = new SyncJdbcNotesRepository(new JdbcTemplate(dbRule.testDatabase))
    }

    def "get retrieves a previously inserted note by ID"() {
        Note insertedNote = repo.insert(new Note("title", "content"))

        when:
        Note note = repo.get(insertedNote.id).get()

        then:
        note == insertedNote
    }

    def "inserting a note returns the inserted note with the ID set"() {
        when:
        Note note = repo.insert(new Note("title", "content"))

        then:
        note.id != null
        note.correlationId == 0
        note.title == "title"
        note.content == "content"
    }

    def "a previously inserted note can be updated"() {
        Note inserted = repo.insert(new Note("title", "content"))

        when:
        inserted.setContent("new content")
        Note note = repo.update(inserted)

        then:
        note == inserted
        repo.get(note.id).get().content == "new content"
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
        repo.delete(inserted.id)

        then:
        !repo.get(inserted.id).isPresent()
    }

    def "no exception is thrown when a delete does not affect any rows"() {
        when:
        repo.delete(100)

        then:
        notThrown(Exception)
    }
}
