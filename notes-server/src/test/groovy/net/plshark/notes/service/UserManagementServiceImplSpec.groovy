package net.plshark.notes.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.crypto.password.PasswordEncoder

import net.plshark.notes.ObjectNotFoundException
import net.plshark.notes.Role
import net.plshark.notes.User
import net.plshark.notes.repo.RoleRepository
import net.plshark.notes.repo.UserRepository
import spock.lang.Specification

class UserManagementServiceImplSpec extends Specification {

    UserRepository userRepo = Mock()
    RoleRepository roleRepo = Mock()
    PasswordEncoder encoder = Mock()
    UserManagementServiceImpl service = new UserManagementServiceImpl(userRepo, roleRepo, encoder)

    def "constructor does not accept null arguments"() {
        when: "user repository is null"
        new UserManagementServiceImpl(null, roleRepo, encoder)

        then:
        thrown(NullPointerException)

        when: "role repository is null"
        new UserManagementServiceImpl(userRepo, null, encoder)

        then:
        thrown(NullPointerException)

        when: "password encoder is null"
        new UserManagementServiceImpl(userRepo, roleRepo, null)

        then:
        thrown(NullPointerException)
    }

    def "cannot save user with ID set"() {
        when:
        service.saveUser(new User(12, "name", "pass"))

        then:
        thrown(IllegalArgumentException)
    }

    def "cannot insert user with null username or password"() {
        when:
        service.saveUser(new User(null, "pass"))

        then:
        thrown (NullPointerException)

        when:
        service.saveUser(new User("name", null))

        then:
        thrown (NullPointerException)
    }

    def "new users have password encoded"() {
        encoder.encode("pass") >> "pass-encoded"

        when:
        service.saveUser(new User("user", "pass"))

        then:
        1 * userRepo.insert(new User("user", "pass-encoded"))
    }

    def "cannot save role with ID set"() {
        when:
        service.saveRole(new Role(1, "name"))

        then:
        thrown(IllegalArgumentException)
    }

    def "saving a role passes role through"() {
        when:
        service.saveRole(new Role("name"))

        then:
        1 * roleRepo.insert(new Role("name"))
    }

    def "cannot update a user to have null password"() {
        when:
        service.updateUserPassword(100, "current", null)

        then:
        thrown(NullPointerException)
    }

    def "new password is encoded when updating password"() {
        encoder.encode("current") >> "current-encoded"
        encoder.encode("new-pass") >> "new-pass-encoded"

        when:
        service.updateUserPassword(100, "current", "new-pass")

        then:
        1 * userRepo.updatePassword(100, "current-encoded", "new-pass-encoded")
    }

    def "no matching user when updating password throws exception"() {
        encoder.encode("current") >> "current-encoded"
        encoder.encode("new") >> "new-encoded"
        userRepo.updatePassword(100, "current-encoded", "new-encoded") >> { throw new EmptyResultDataAccessException(1) }

        when:
        service.updateUserPassword(100, "current", "new")

        then:
        thrown(ObjectNotFoundException)
    }

    def "all roles for a user are removed when the user is deleted"() {
        when:
        service.deleteUser(100)

        then:
        1 * userRepo.deleteUserRolesForUser(100)
        1 * userRepo.delete(100)
    }

    def "role is removed from all users when the role is deleted"() {
        when:
        service.deleteRole(200)

        then:
        1 * userRepo.deleteUserRolesForRole(200)
        1 * roleRepo.delete(200)
    }

    def "granting a role to a user should add the role to the user's role"() {
        when:
        service.grantRoleToUser(12, 34)

        then:
        1 * userRepo.insertUserRole(12, 34)
    }

    def "granting a role to a user that does not exist should throw an ObjectNotFoundException"() {
        userRepo.getForId(100) >> { throw new EmptyResultDataAccessException(1) }

        when:
        service.grantRoleToUser(100, 200)

        then:
        thrown(ObjectNotFoundException)
    }

    def "granting a role that does not exist should throw an ObjectNotFoundException"() {
        roleRepo.getForId(200) >> { throw new EmptyResultDataAccessException(1) }

        when:
        service.grantRoleToUser(100, 200)

        then:
        thrown(ObjectNotFoundException)
    }

    def "removing a role from a user should remove the role from the user's roles"() {
        when:
        service.removeRoleFromUser(100, 200)

        then:
        1 * userRepo.deleteUserRole(100, 200)
    }

    def "removing a role from a user that does not exist should throw an ObjectNotFoundException"() {
        userRepo.getForId(100) >> { throw new EmptyResultDataAccessException(1) }

        when:
        service.removeRoleFromUser(100, 200)

        then:
        thrown(ObjectNotFoundException)
    }
}
