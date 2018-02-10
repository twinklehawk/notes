package net.plshark.notes.webservice

import net.plshark.BadRequestException
import net.plshark.users.Role
import net.plshark.users.service.UserManagementService
import spock.lang.Specification

class RolesControllerSpec extends Specification {

    UserManagementService service = Mock()
    RolesController controller = new RolesController(service)

    def "constructor does not accept null args"() {
        when:
        new RolesController(null)

        then:
        thrown(NullPointerException)
    }

    def "cannot insert a role with ID already set"() {
        when:
        controller.insert(new Role(1, "name"))

        then:
        thrown(BadRequestException)
    }

    def "insert passes not through to service"() {
        when:
        controller.insert(new Role("admin"))

        then:
        1 * service.saveRole({ Role role -> !role.id.isPresent && role.name == "admin" })
    }

    def "delete passes ID through to service"() {
        when:
        controller.delete(100)

        then:
        1 * service.deleteRole(100)
    }
}
