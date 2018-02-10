package net.plshark.notes.repo.jdbc

import javax.inject.Inject

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.ActiveProfiles

import net.plshark.notes.repo.RolesRepository
import net.plshark.notes.webservice.Application
import net.plshark.users.Role
import net.plshark.users.User
import spock.lang.Specification

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class JdbcUserRolesRepositoryITSpec extends Specification {

    @Inject
    JdbcUserRolesRepository repo
    Role testRole1
    Role testRole2
    @Inject
    RolesRepository rolesRepo

    def setup() {
        testRole1 = rolesRepo.insert(new Role("testRole1"))
        testRole2 = rolesRepo.insert(new Role("testRole2"))
    }

    def cleanup() {
        if (testRole1 != null)
            rolesRepo.delete(testRole1.id.asLong)
        if (testRole2 != null)
            rolesRepo.delete(testRole2.id.asLong)
    }

    def "can add a role to a user"() {
        when:
        repo.insertUserRole(100, testRole1.id.asLong)

        then:
        repo.getRolesForUser(100).stream().anyMatch{role -> role.id.asLong == testRole1.id.asLong}

        cleanup:
        repo.deleteUserRolesForUser(100)
    }

    def "can retrieve all roles for a user"() {
        repo.insertUserRole(100, testRole1.id.asLong)
        repo.insertUserRole(100, testRole2.id.asLong)

        when:
        List<Role> roles = repo.getRolesForUser(100)

        then:
        roles.size() == 2
        roles.stream().anyMatch{role -> role.id.asLong == testRole1.id.asLong}
        roles.stream().anyMatch{role -> role.id.asLong == testRole2.id.asLong}

        cleanup:
        repo.deleteUserRolesForUser(100)
    }

    def "retrieving roles for a user does not return roles for other users"() {
        repo.insertUserRole(100, testRole1.id.asLong)
        repo.insertUserRole(200, testRole2.id.asLong)

        when:
        List<Role> roles = repo.getRolesForUser(100)

        then:
        roles.size() == 1
        roles.stream().anyMatch{role -> role.id.asLong == testRole1.id.asLong}

        cleanup:
        repo.deleteUserRolesForUser(100)
        repo.deleteUserRolesForUser(200)
    }

    def "can delete an existing user role"() {
        repo.insertUserRole(100, testRole1.id.asLong)

        when:
        repo.deleteUserRole(100, testRole1.id.asLong)

        then:
        repo.getRolesForUser(100).size() == 0
    }

    def "deleting a user role that does not exist does not throw an exception"() {
        when:
        repo.deleteUserRole(100, 200)

        then:
        notThrown(Exception)
    }

    def "can delete all roles for a user"() {
        repo.insertUserRole(100, testRole1.id.asLong)
        repo.insertUserRole(100, testRole2.id.asLong)

        when:
        repo.deleteUserRolesForUser(100)

        then:
        repo.getRolesForUser(100).size() == 0
    }

    def "deleting all roles for a user does not affect other users"() {
        repo.insertUserRole(100, testRole1.id.asLong)
        repo.insertUserRole(200, testRole2.id.asLong)

        when:
        repo.deleteUserRolesForUser(100)

        then:
        repo.getRolesForUser(100).size() == 0
        repo.getRolesForUser(200).size() == 1

        cleanup:
        repo.deleteUserRolesForUser(100)
        repo.deleteUserRolesForUser(200)
    }

    def "can remove a role from all users"() {
        repo.insertUserRole(100, testRole1.id.asLong)
        repo.insertUserRole(200, testRole1.id.asLong)

        when:
        repo.deleteUserRolesForRole(testRole1.id.asLong)

        then:
        repo.getRolesForUser(100).size() == 0
        repo.getRolesForUser(200).size() == 0
    }

    def "removing a role from all users does not affect other roles"() {
        repo.insertUserRole(100, testRole1.id.asLong)
        repo.insertUserRole(200, testRole2.id.asLong)

        when:
        repo.deleteUserRolesForRole(testRole1.id.asLong)

        then:
        repo.getRolesForUser(100).size() == 0
        repo.getRolesForUser(200).size() == 1

        cleanup:
        repo.deleteUserRolesForUser(200)
    }
}
