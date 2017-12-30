package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.ActiveProfiles

import net.plshark.notes.Role
import net.plshark.notes.User
import net.plshark.notes.webservice.Application
import spock.lang.Specification

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class JdbcUsersRepositoryITSpec extends Specification {

    @Inject
    JdbcUsersRepository repo

    def "inserting a user returns the inserted user with the ID set"() {
        when:
        User inserted = repo.insert(new User("name", "pass"))

        then:
        inserted.id.isPresent
        inserted.username == "name"
        inserted.password == "pass"

        cleanup:
        repo.delete(inserted.id.asLong)
    }

    def "can retrieve a previously inserted user by ID"() {
        User inserted = repo.insert(new User("name", "pass"))

        when:
        User user = repo.getForId(inserted.id.asLong)

        then:
        user == inserted

        cleanup:
        repo.delete(inserted.id.asLong)
    }

    def "can retrieve  previously inserted user by username"() {
        User inserted = repo.insert(new User("name", "pass"))

        when:
        User user = repo.getForUsername("name")

        then:
        user == inserted

        cleanup:
        repo.delete(inserted.id.asLong)
    }

    def "can delete a previously inserted user by ID"() {
        User inserted = repo.insert(new User("name", "pass"))

        when:
        repo.delete(inserted.id.asLong)
        repo.getForId(inserted.id.asLong)

        then: "should throw an exception since the row should be gone"
        thrown(EmptyResultDataAccessException)
    }

    def "no exception is thrown when attempting to delete a user that does not exist"() {
        when:
        repo.delete(10000)

        then:
        notThrown(Exception)
    }

    def "update password should change the password if the current password is correct"() {
        User inserted = repo.insert(new User("name", "pass"))

        when:
        repo.updatePassword(inserted.id.asLong, "pass", "new-pass")
        User user = repo.getForId(inserted.id.asLong)

        then:
        user.password == "new-pass"

        cleanup:
        repo.delete(inserted.id.asLong)
    }

    def "update password should throw an EmptyResultDataAccessException if the current password is wrong"() {
        User inserted = repo.insert(new User("name", "pass"))

        when:
        repo.updatePassword(inserted.id.asLong, "wrong-pass", "new-pass")

        then:
        thrown(EmptyResultDataAccessException)

        when:
        User user = repo.getForId(inserted.id.asLong)

        then:
        user.password == "pass"

        cleanup:
        repo.delete(inserted.id.asLong)
    }

    def "update password should throw an EmptyResultDataAccessException if no user has the ID"() {
        when:
        repo.updatePassword(1000, "pass", "new-pass")

        then:
        thrown(EmptyResultDataAccessException)
    }
}
