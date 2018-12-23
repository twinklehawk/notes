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
        repo.insert(new UserNotePermission('user', 2, true, true))
        UserNotePermission permission = repo.getByUserAndNote('user', 2).get()

        then:
        permission.username == 'user'
        permission.noteId == 2
        permission.readable
        permission.writable
        !permission.owner

        cleanup:
        repo.deleteByUserAndNote('user', 2)
    }

    def "no matching permission returns an empty optional"() {
        when:
        Optional<UserNotePermission> permission = repo.getByUserAndNote('user', 2)

        then:
        !permission.present
    }

    def "can delete a permission"() {
        repo.insert(new UserNotePermission('user', 2, true, true))

        when:
        repo.deleteByUserAndNote('user', 2)

        then:
        !repo.getByUserAndNote('user', 2).present
    }

    def "delete does not fail if no permission matches"() {
        when:
        repo.deleteByUserAndNote('user', 2)

        then:
        !repo.getByUserAndNote('user', 2).present
    }

    def "deleting by note deletes all permissions for a note"() {
        repo.insert(new UserNotePermission('user', 2, true, true))
        repo.insert(new UserNotePermission('user2', 2, true, true))

        when:
        repo.deleteByNote(2)

        then:
        !repo.getByUserAndNote('user', 2).present
        !repo.getByUserAndNote('user2', 2).present
    }

    def "deleting by note does not touch permissions for other notes"() {
        repo.insert(new UserNotePermission('user', 1, true, true))

        when:
        repo.deleteByNote(2)

        then:
        repo.getByUserAndNote('user', 1).present

        cleanup:
        repo.deleteByUserAndNote('user', 1)
    }

    def "can update an existing permission"() {
        repo.insert(new UserNotePermission('user', 1, true, true))

        when:
        repo.update(new UserNotePermission('user', 1, true, false))
        UserNotePermission permission = repo.getByUserAndNote('user', 1).get()

        then:
        permission.readable
        !permission.writable
    }

    def "a JdbcUpdateAffectedIncorrectNumberOfRowsException is thrown if no rows are updated"() {
        when:
        repo.update(new UserNotePermission('user', 200, true, true))

        then:
        thrown(JdbcUpdateAffectedIncorrectNumberOfRowsException)
    }
}
