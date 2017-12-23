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
        when:
        new UserManagementServiceImpl(null, roleRepo, encoder)

        then:
        thrown(NullPointerException)

        when:
        new UserManagementServiceImpl(userRepo, null, encoder)

        then:
        thrown(NullPointerException)

        when:
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
        1 * userRepo.insert({ User user -> user.password == "pass-encoded" })
    }

    def "cannot save role with ID set"() {
        when:
        service.saveRole(new Role(1, "name"))

        then:
        thrown(IllegalArgumentException)
    }

    def "cannot update a user to have null password"() {
        when:
        service.updateUserPassword(100, "current", null)

        then:
        thrown(NullPointerException)
    }

    def "new password is encoded when updating password"() {
        userRepo.getForId(100) >> new User(100, "user", "current-encoded")
        encoder.matches("current", "current-encoded") >> true
        encoder.encode("new-pass") >> "new-pass-encoded"

        when:
        service.updateUserPassword(100, "current", "new-pass")

        then:
        1 * userRepo.update({ User user -> user.password == "new-pass-encoded" })
    }

    def "no matching user when updating password throws exception"() {
        userRepo.getForId(100) >> { throw new EmptyResultDataAccessException(1) }

        when:
        service.updateUserPassword(100, "current", "new")

        then:
        thrown(ObjectNotFoundException)
    }

    def "updating password with incorrect current password throws exception"() {
        userRepo.getForId(100) >> new User(100, "user", "current-encoded")
        encoder.matches("current", "current-encoded") >> false

        when:
        service.updateUserPassword(100, "current", "new-pass")

        then:
        thrown(IllegalArgumentException)
    }
}
