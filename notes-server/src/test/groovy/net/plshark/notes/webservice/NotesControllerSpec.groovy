package net.plshark.notes.webservice

import net.plshark.notes.BadRequestException
import net.plshark.notes.Note
import net.plshark.notes.service.NotesService
import spock.lang.Specification

class NotesControllerSpec extends Specification {

    def "nulls not allowed in constructor args"() {
        when:
        new NotesController(null)

        then:
        thrown(NullPointerException)
    }

    def "update with different IDs in URL and object treated as bad request"() {
        NotesController controller = new NotesController(Mock(NotesService))

        when:
        controller.update(100, new Note(200, 1, 2, "", ""))

        then:
        thrown(BadRequestException)
    }

    def "update with no ID in object uses ID from URL"() {
        NotesService service = Mock()
        NotesController controller = new NotesController(service)

        when:
        controller.update(100, new Note(1, 2, "", ""))

        then:
        1 * service.save({ Note n -> n.id.asLong == 100 })
    }
}
