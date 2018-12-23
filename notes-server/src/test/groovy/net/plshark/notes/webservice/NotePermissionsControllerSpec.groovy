package net.plshark.notes.webservice

import net.plshark.notes.NotePermission
import net.plshark.notes.service.NotePermissionsService
import org.springframework.security.core.Authentication
import reactor.test.StepVerifier
import reactor.test.publisher.PublisherProbe
import spock.lang.Specification

class NotePermissionsControllerSpec extends Specification {

    NotePermissionsService notePermissionService = Mock()
    NotePermissionsController controller = new NotePermissionsController(notePermissionService)
    Authentication auth = Mock(Authentication)

    def "nulls not allowed in constructor args"() {
        when:
        new NotePermissionsController(null)

        then:
        thrown(NullPointerException)
    }

    def "setting permissions looks up the user ID"() {
        auth.getName() >> 'user'
        PublisherProbe probe = PublisherProbe.empty()
        notePermissionService.setPermissionForUser(1, 'user2', 'user', NotePermission.create(true, true)) >> probe.mono()

        expect:
        StepVerifier.create(controller.setPermissionForUser(1, 'user2', NotePermission.create(true, true), auth))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }

    def "removing permissions looks up the user ID"() {
        auth.getName() >> 'user'
        PublisherProbe probe = PublisherProbe.empty()
        notePermissionService.removePermissionForUser(1, 'user2', 'user') >> probe.mono()

        expect:
        StepVerifier.create(controller.removePermissionForUser(1, 'user2', auth))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }
}
