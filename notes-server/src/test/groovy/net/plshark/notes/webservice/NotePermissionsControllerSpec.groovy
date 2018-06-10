package net.plshark.notes.webservice

import org.springframework.security.core.Authentication

import net.plshark.notes.NotePermission
import net.plshark.notes.service.NotePermissionsService
import net.plshark.users.service.UserAuthenticationService
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.publisher.PublisherProbe
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
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(12L)
        PublisherProbe probe = PublisherProbe.empty()
        notePermissionService.setPermissionForUser(1, 2, 12, NotePermission.create(true, true)) >> probe.mono()

        expect:
        StepVerifier.create(controller.setPermissionForUser(1, 2, NotePermission.create(true, true), Mock(Authentication)))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }

    def "removing permissions looks up the user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(12L)
        PublisherProbe probe = PublisherProbe.empty()
        notePermissionService.removePermissionForUser(1, 2, 12) >> probe.mono()

        expect:
        StepVerifier.create(controller.removePermissionForUser(1, 2, Mock(Authentication)))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }
}
