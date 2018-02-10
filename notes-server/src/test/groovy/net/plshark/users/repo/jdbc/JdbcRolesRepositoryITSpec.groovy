package net.plshark.users.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.ActiveProfiles

import net.plshark.notes.webservice.Application
import net.plshark.users.Role
import net.plshark.users.repo.jdbc.JdbcRolesRepository
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
        inserted.id.isPresent()
        inserted.name == "test-role"

        cleanup:
        repo.delete(inserted.id.get())
    }

    def "can retrieve a previously inserted role by ID"() {
        Role inserted = repo.insert(new Role("test-role"))

        when:
        Role role = repo.getForId(inserted.id.get())

        then:
        role == inserted

        cleanup:
        repo.delete(inserted.id.get())
    }

    def "retrieving a role by ID when no role matches throws EmptyResultDataAccessException"() {
        when:
        Role role = repo.getForId(1000)

        then:
        thrown(EmptyResultDataAccessException)
    }

    def "can retrieve a previously inserted role by name"() {
        Role inserted = repo.insert(new Role("test-role"))

        when:
        Role role = repo.getForName("test-role")

        then:
        role == inserted

        cleanup:
        repo.delete(inserted.id.get())
    }

    def "retrieving a role by name when no role matches throws EmptyResultDataAccessException"() {
        when:
        Role role = repo.getForName("test-role")

        then:
        thrown(EmptyResultDataAccessException)
    }

    def "can delete a previously inserted role by ID"() {
        Role inserted = repo.insert(new Role("test-role"))

        when:
        repo.delete(inserted.id.get())
        repo.getForId(inserted.id.get())

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
