package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.ActiveProfiles

import net.plshark.notes.Role
import net.plshark.notes.webservice.Application
import spock.lang.Specification

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class JdbcRolesRepositoryITSpec extends Specification {

    @Inject
    JdbcRolesRepository repo

    def "inserting a role returns the inserted role with the ID set"() {
        when:
        Role inserted = repo.insert(new Role("test-role"))

        then:
        inserted.id.isPresent
        inserted.name == "test-role"

        cleanup:
        repo.delete(inserted.id.asLong)
    }

    def "can retrieve a previously inserted role by ID"() {
        Role inserted = repo.insert(new Role("test-role"))

        when:
        Role role = repo.getForId(inserted.id.asLong)

        then:
        role == inserted

        cleanup:
        repo.delete(inserted.id.asLong)
    }

    def "can delete a previously inserted role by ID"() {
        Role inserted = repo.insert(new Role("test-role"))

        when:
        repo.delete(inserted.id.asLong)
        repo.getForId(inserted.id.asLong)

        then: "get should throw an exception since the row should be gone"
        thrown(EmptyResultDataAccessException)
    }

    def "no exception is thrown when attempting to delete a role that does not exist"() {
        when:
        repo.delete(10000)

        then:
        notThrown(Exception)
    }
}
