package net.plshark.notes.webservice

import net.plshark.notes.User
import net.plshark.notes.service.UserManagementService
import spock.lang.Specification

class UsersControllerSpec extends Specification {

    UserManagementService service = Mock()
    UsersController controller = new UsersController(service)

    def "password is not returned when creating a user"() {
        service.saveUser(_) >> new User(1, "user", "pass-encoded")

        when:
        User user = controller.insert(new User("name", "pass"))

        then:
        user.password == null
    }
}
