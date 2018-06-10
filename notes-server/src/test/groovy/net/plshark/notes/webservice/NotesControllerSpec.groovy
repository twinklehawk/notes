package net.plshark.notes.webservice

import org.springframework.security.core.Authentication

import net.plshark.BadRequestException
import net.plshark.ObjectNotFoundException
import net.plshark.notes.Note
import net.plshark.notes.service.NotesService
import net.plshark.users.service.UserAuthenticationService
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.publisher.PublisherProbe
import spock.lang.Specification

class NotesControllerSpec extends Specification {

    NotesService notesService = Mock()
    UserAuthenticationService userAuthService = Mock()
    NotesController controller = new NotesController(notesService, userAuthService)

    def "nulls not allowed in constructor args"() {
        when:
        new NotesController(null, userAuthService)

        then:
        thrown(NullPointerException)

        when:
        new NotesController(notesService, null)

        then:
        thrown(NullPointerException)
    }

    def "update with different IDs in URL and object treated as bad request"() {
        expect:
        StepVerifier.create(controller.update(100, new Note(200, 2, "", ""), Mock(Authentication)))
            .verifyError(BadRequestException.class)
    }

    def "update with no ID in object uses ID from URL"() {
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(0L)
        PublisherProbe probe = PublisherProbe.of(Mono.just(new Note("", "")))
        notesService.save({ Note n -> n.id.get() == 100L }, 0) >> probe.mono()

        expect:
        StepVerifier.create(controller.update(100, new Note("", ""), Mock(Authentication)))
            .expectNext(new Note("", ""))
            .verifyComplete()
    }

    def "update passes the note through and retrieves the current user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(15L)
        PublisherProbe probe = PublisherProbe.of(Mono.just(new Note(100, 2, "", "")))
        notesService.save(new Note(100, 2, "", ""), 15) >> probe.mono()

        expect:
        StepVerifier.create(controller.update(100, new Note(100, 2, "", ""), Mock(Authentication)))
            .expectNext(new Note(100, 2, "", ""))
            .verifyComplete()
    }

    def "get passes the ID through and retrieves the current user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(15L)
        notesService.getForUser(2, 15) >> Mono.just(new Note("title", "cont"))

        expect:
        StepVerifier.create(controller.get(2, Mock(Authentication)))
            .expectNext(new Note("title", "cont"))
            .verifyComplete()
    }

    def "an ObjectNotFoundException is thrown when no note matches a get request"() {
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(0L)
        notesService.getForUser(2, 0) >> Mono.empty()

        expect:
        StepVerifier.create(controller.get(2, Mock(Authentication)))
            .verifyError(ObjectNotFoundException)
    }

    def "insert passes the note through and retrieves the current user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(15L)
        notesService.save(new Note(100, 200, "title", "content"), 15) >> Mono.just(new Note(100, 200, "title", "content"))

        expect:
        StepVerifier.create(controller.insert(new Note(100, 200, "title", "content"), Mock(Authentication)))
            .expectNext(new Note(100, 200, "title", "content"))
            .verifyComplete()
    }

    def "delete passes the ID through"() {
        userAuthService.getUserIdForAuthentication(_) >> Mono.just(15L)
        PublisherProbe probe = PublisherProbe.empty()
        notesService.deleteForUser(12, 15) >> probe.mono()

        expect:
        StepVerifier.create(controller.delete(12, Mock(Authentication)))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }
}
