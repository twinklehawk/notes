package net.plshark.notes.webservice

import org.springframework.security.core.Authentication

import net.plshark.notes.BadRequestException
import net.plshark.notes.Note
import net.plshark.notes.ObjectNotFoundException
import net.plshark.notes.service.NotesService
import net.plshark.notes.service.UserAuthenticationService
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
        when:
        controller.update(100, new Note(200, 2, "", ""), Mock(Authentication))

        then:
        thrown(BadRequestException)
    }

    def "update with no ID in object uses ID from URL"() {
        when:
        controller.update(100, new Note("", ""), Mock(Authentication))

        then:
        1 * notesService.save({ Note n -> n.id.asLong == 100 }, 0)
    }

    def "update passes the note through and retrieves the current user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> 15

        when:
        controller.update(100, new Note(100, 2, "", ""), Mock(Authentication))

        then:
        1 * notesService.save(new Note(100, 2, "", ""), 15)
    }

    def "get passes the ID through and retrieves the current user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> 15
        notesService.getForUser(2, 15) >> Optional.of(new Note("title", "cont"))

        when:
        Note note = controller.get(2, Mock(Authentication))

        then:
        note == new Note("title", "cont")
    }

    def "an ObjectNotFoundException is thrown when no note matches a get request"() {
        notesService.getForUser(2, 0) >> Optional.empty()

        when:
        controller.get(2, Mock(Authentication))

        then:
        thrown(ObjectNotFoundException)
    }

    def "insert passes the note through and retrieves the current user ID"() {
        userAuthService.getUserIdForAuthentication(_) >> 15

        when:
        controller.insert(new Note(100, 200, "title", "content"), Mock(Authentication))

        then:
        1 * notesService.save(new Note(100, 200, "title", "content"), 15)
    }

    def "delete passes the ID through"() {
        userAuthService.getUserIdForAuthentication(_) >> 15

        when:
        controller.delete(12, Mock(Authentication))

        then:
        1 * notesService.deleteForUser(12, 15)
    }
}
