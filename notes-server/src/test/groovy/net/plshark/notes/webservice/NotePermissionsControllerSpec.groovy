package net.plshark.notes.webservice

import org.springframework.security.core.Authentication

import net.plshark.notes.NotePermission
import net.plshark.notes.service.NotePermissionsService
import net.plshark.users.service.UserAuthenticationService
import spock.lang.Specification

class NotePermissionsControllerSpec extends Specification {

    NotePermissionsService notePermissionService = Mock()
    UserAuthenticationService userAuthService = Mock()
    NotePermissionsController controller = new NotePermissionsController(notePermissionService, userAuthService)

    def "nulls not allowed in constructor args"() {
        when:
        new NotePermissionsController(null, userAuthService)

        then:
        thrown(NullPointerException)

        when:
        new NotePermissionsController(notePermissionService, null)

        then:
        thrown(NullPointerException)
    }

    def "setting permissions looks up the user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> 12

        when:
        controller.setPermissionForUser(1, 2, NotePermission.create(true, true), Mock(Authentication))

        then:
        1 * notePermissionService.setPermissionForUser(1, 2, 12, NotePermission.create(true, true))
    }

    def "removing permissions looks up the user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> 12

        when:
        controller.removePermissionForUser(1, 2, Mock(Authentication))

        then:
        1 * notePermissionService.removePermissionForUser(1, 2, 12)
    }
}
