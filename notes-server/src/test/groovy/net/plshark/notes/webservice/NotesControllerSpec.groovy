package net.plshark.notes.webservice

import net.plshark.notes.BadRequestException
import net.plshark.notes.Note
import net.plshark.notes.service.NotesService
import spock.lang.Specification

class NotesControllerSpec extends Specification {

    NotesService service = Mock()
    NotesController controller = new NotesController(service)

    def "nulls not allowed in constructor args"() {
        when:
        new NotesController(null)

        then:
        thrown(NullPointerException)
    }

    def "update with different IDs in URL and object treated as bad request"() {
        when:
        controller.update(100, new Note(200, 2, "", ""))

        then:
        thrown(BadRequestException)
    }

    def "update with no ID in object uses ID from URL"() {
        when:
        controller.update(100, new Note("", ""))

        then:
        1 * service.save({ Note n -> n.id.asLong == 100 })
    }

    def "update passes the note through"() {
        when:
        controller.update(100, new Note(100, 2, "", ""))

        then:
        1 * service.save(new Note(100, 2, "", ""))
    }

    def "get passes the ID through"() {
        when:
        controller.get(2)

        then:
        1 * service.get(2)
    }

    def "insert passes the note through"() {
        when:
        controller.insert(new Note(100, 200, "title", "content"))

        then:
        1 * service.save(new Note(100, 200, "title", "content"))
    }

    def "delete passes the ID through"() {
        when:
        controller.delete(12)

        then:
        1 * service.delete(12)
    }
}
