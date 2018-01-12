package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

import net.plshark.notes.UserNotePermission
import net.plshark.notes.webservice.Application
import spock.lang.Specification

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class JdbcUserNotePermissionsRepositoryITSpec extends Specification {

    @Inject
    JdbcUserNotePermissionsRepository repo

    def "can insert and retrieve a permission"() {
        when:
        repo.insert(new UserNotePermission(1, 2, true, true))
        UserNotePermission permission = repo.getByUserAndNote(1, 2).get()

        then:
        permission.userId == 1
        permission.noteId == 2
        permission.readable == true
        permission.writable == true

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
}
