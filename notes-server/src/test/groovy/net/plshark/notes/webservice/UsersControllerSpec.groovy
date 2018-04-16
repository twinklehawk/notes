package net.plshark.notes.webservice

import net.plshark.BadRequestException
import net.plshark.users.PasswordChangeRequest
import net.plshark.users.User
import net.plshark.users.service.UserManagementService
import spock.lang.Specification

class UsersControllerSpec extends Specification {

    UserManagementService service = Mock()
    UsersController controller = new UsersController(service)

    def "constructor does not accept null args"() {
        when:
        new UsersController(null)

        then:
        thrown(NullPointerException)
    }

    def "inserting a user with an ID throws BadRequestException"() {
        when:
        controller.insert(new User(1, "name", "pass"))

        then:
        thrown(BadRequestException)
    }

    def "password is not returned when creating a user"() {
        service.saveUser(_) >> new User(1, "user", "pass-encoded")

        when:
        User user = controller.insert(new User("name", "pass"))

        then:
        user.password.present == false
    }

    def "delete passes the user ID through to be deleted"() {
        when:
        controller.delete(100)

        then:
        1 * service.deleteUser(100)
    }

    def "change password passes the user ID, current password, and new passwords through"() {
        when:
        controller.changePassword(100, PasswordChangeRequest.create("current", "new"))

        then:
        1 * service.updateUserPassword(100, "current", "new")
    }

    def "granting a role passes the user and role IDs through"() {
        when:
        controller.grantRole(100, 200)

        then:
        1 * service.grantRoleToUser(100, 200)
    }

    def "removing a role passes the user and role IDs through"() {
        when:
        controller.removeRole(200, 300)

        then:
        1 * service.removeRoleFromUser(200, 300)
    }
}
