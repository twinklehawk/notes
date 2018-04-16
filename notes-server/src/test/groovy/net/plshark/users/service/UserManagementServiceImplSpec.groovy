package net.plshark.users.service

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.crypto.password.PasswordEncoder

import net.plshark.ObjectNotFoundException
import net.plshark.users.Role
import net.plshark.users.User
import net.plshark.users.repo.RolesRepository
import net.plshark.users.repo.UserRolesRepository
import net.plshark.users.repo.UsersRepository
import net.plshark.users.service.UserManagementServiceImpl
import spock.lang.Specification

class UserManagementServiceImplSpec extends Specification {

    UsersRepository userRepo = Mock()
    RolesRepository roleRepo = Mock()
    UserRolesRepository userRolesRepo = Mock()
    PasswordEncoder encoder = Mock()
    UserManagementServiceImpl service = new UserManagementServiceImpl(userRepo, roleRepo, userRolesRepo, encoder)

    def "constructor does not accept null arguments"() {
        when: "user repository is null"
        new UserManagementServiceImpl(null, roleRepo, userRolesRepo, encoder)

        then:
        thrown(NullPointerException)

        when: "role repository is null"
        new UserManagementServiceImpl(userRepo, null, userRolesRepo, encoder)

        then:
        thrown(NullPointerException)

        when: "user roles repository is null"
        new UserManagementServiceImpl(userRepo, roleRepo, null, encoder)

        then:
        thrown(NullPointerException)

        when: "password encoder is null"
        new UserManagementServiceImpl(userRepo, roleRepo, userRolesRepo, null)

        then:
        thrown(NullPointerException)
    }

    def "cannot save user with ID set"() {
        when:
        service.saveUser(new User(12, "name", "pass"))

        then:
        thrown(IllegalArgumentException)
    }

    def "cannot insert user with null password"() {
        when:
        service.saveUser(new User("name", null))

        then:
        thrown(NoSuchElementException)
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
        1 * userRolesRepo.deleteUserRolesForUser(100)
        1 * userRepo.delete(100)
    }

    def "role is removed from all users when the role is deleted"() {
        when:
        service.deleteRole(200)

        then:
        1 * userRolesRepo.deleteUserRolesForRole(200)
        1 * roleRepo.delete(200)
    }

    def "granting a role to a user should add the role to the user's role"() {
        when:
        service.grantRoleToUser(12, 34)

        then:
        1 * userRolesRepo.insertUserRole(12, 34)
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
        1 * userRolesRepo.deleteUserRole(100, 200)
    }

    def "removing a role from a user that does not exist should throw an ObjectNotFoundException"() {
        userRepo.getForId(100) >> { throw new EmptyResultDataAccessException(1) }

        when:
        service.removeRoleFromUser(100, 200)

        then:
        thrown(ObjectNotFoundException)
    }

    def "retrieving a role by name passes the name through"() {
        roleRepo.getForName("name") >> Optional.of(new Role(1, "name"))

        when:
        Role role = service.getRoleByName("name").get()

        then:
        role == new Role(1, "name")
    }

    def "an empty optional is returned when no role matches the name"() {
        roleRepo.getForName("name") >> Optional.empty()

        when:
        Optional<Role> role = service.getRoleByName("name")

        then:
        !role.isPresent()
    }
}
