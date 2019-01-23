package net.plshark.notes.webservice

import net.plshark.ObjectNotFoundException
import net.plshark.notes.Note
import net.plshark.notes.service.NotesService
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.publisher.PublisherProbe
import spock.lang.Specification

class NotesControllerSpec extends Specification {

    Authentication auth = Mock()
    NotesService notesService = Mock()
    NotesController controller = new NotesController(notesService)

    def "nulls not allowed in constructor args"() {
        when:
        new NotesController(null)

        then:
        thrown(NullPointerException)
    }

    def "update with no ID in object uses ID from URL"() {
        auth.getName() >> 'user'
        PublisherProbe probe = PublisherProbe.of(Mono.just(new Note("", "")))
        notesService.update({ Note n -> n.id == 100L }, 'user') >> probe.mono()

        expect:
        StepVerifier.create(controller.update(100, new Note("", ""), auth))
            .expectNext(new Note("", ""))
            .verifyComplete()
    }

    def "update passes the note through and retrieves the current user ID"() {
        auth.getName() >> 'user'
        PublisherProbe probe = PublisherProbe.of(Mono.just(new Note(100, 2, "", "")))
        notesService.update(new Note(100, 2, "", ""), 'user') >> probe.mono()

        expect:
        StepVerifier.create(controller.update(100, new Note(100, 2, "", ""), auth))
            .expectNext(new Note(100, 2, "", ""))
            .verifyComplete()
    }

    def "get passes the ID through and retrieves the current user ID"() {
        auth.getName() >> 'user'
        notesService.getForUser(2, 'user') >> Mono.just(new Note("title", "cont"))

        expect:
        StepVerifier.create(controller.get(2, auth))
            .expectNext(new Note("title", "cont"))
            .verifyComplete()
    }

    def "an ObjectNotFoundException is thrown when no note matches a get request"() {
        auth.getName() >> 'user'
        notesService.getForUser(2, 'user') >> Mono.empty()

        expect:
        StepVerifier.create(controller.get(2, auth))
            .verifyError(ObjectNotFoundException)
    }

    def "insert passes the note through and retrieves the current user ID"() {
        auth.getName() >> 'user'
        notesService.save(new Note(100, 200, "title", "content"), 'user') >> Mono.just(new Note(100, 200, "title", "content"))

        expect:
        StepVerifier.create(controller.insert(new Note(100, 200, "title", "content"), auth))
            .expectNext(new Note(100, 200, "title", "content"))
            .verifyComplete()
    }

    def "delete passes the ID through"() {
        auth.getName() >> 'user2'
        PublisherProbe probe = PublisherProbe.empty()
        notesService.deleteForUser(12, 'user2') >> probe.mono()

        expect:
        StepVerifier.create(controller.delete(12, auth))
            .verifyComplete()
        probe.assertWasSubscribed()
        probe.assertWasRequested()
        probe.assertWasNotCancelled()
    }
}
