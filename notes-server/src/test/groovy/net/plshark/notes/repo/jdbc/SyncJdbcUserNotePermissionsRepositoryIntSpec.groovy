package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException

import net.plshark.jdbc.RepoTestConfig
import net.plshark.notes.UserNotePermission
import spock.lang.Specification

@SpringBootTest(classes = RepoTestConfig.class)
class SyncJdbcUserNotePermissionsRepositoryIntSpec extends Specification {

    @Inject
    SyncJdbcUserNotePermissionsRepository repo

    def "can insert and retrieve a permission"() {
        when:
        repo.insert(new UserNotePermission(1, 2, true, true))
        UserNotePermission permission = repo.getByUserAndNote(1, 2).get()

        then:
        permission.userId == 1
        permission.noteId == 2
        permission.readable == true
        permission.writable == true
        permission.owner == false

        cleanup:
        repo.deleteByUserAndNote(1, 2)
    }

    def "no matching permission returns an empty optional"() {
        when:
        Optional<UserNotePermission> permission = repo.getByUserAndNote(1, 2)

        then:
        permission.present == false
    }

    def "can delete a permission"() {
        repo.insert(new UserNotePermission(1, 2, true, true))

        when:
        repo.deleteByUserAndNote(1, 2)

        then:
        repo.getByUserAndNote(1, 2).present == false
    }

    def "delete does not fail if no permission matches"() {
        when:
        repo.deleteByUserAndNote(1, 2)

        then:
        repo.getByUserAndNote(1, 2).present == false
    }

    def "deleting by note deletes all permissions for a note"() {
        repo.insert(new UserNotePermission(1, 2, true, true))
        repo.insert(new UserNotePermission(2, 2, true, true))

        when:
        repo.deleteByNote(2)

        then:
        !repo.getByUserAndNote(1, 2).present
        !repo.getByUserAndNote(2, 2).present
    }

    def "deleting by note does not touch permissions for other notes"() {
        repo.insert(new UserNotePermission(1, 1, true, true))

        when:
        repo.deleteByNote(2)

        then:
        repo.getByUserAndNote(1, 1).present

        cleanup:
        repo.deleteByUserAndNote(1, 1)
    }

    def "can update an existing permission"() {
        repo.insert(new UserNotePermission(1, 1, true, true))

        when:
        repo.update(new UserNotePermission(1, 1, true, false))
        UserNotePermission permission = repo.getByUserAndNote(1, 1).get()

        then:
        permission.readable == true
        permission.writable == false
    }

    def "a JdbcUpdateAffectedIncorrectNumberOfRowsException is thrown if no rows are updated"() {
        when:
        repo.update(new UserNotePermission(100, 200, true, true))

        then:
        thrown(JdbcUpdateAffectedIncorrectNumberOfRowsException)
    }
}
