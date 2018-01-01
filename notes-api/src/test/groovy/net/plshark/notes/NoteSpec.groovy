package net.plshark.notes

import spock.lang.Specification

class NoteSpec extends Specification {

    def "constructors set id to empty"() {
        Note note

        when:
        note = new Note("title", "content")

        then:
        !note.id.isPresent

        when:
        note = new Note(OptionalLong.empty(), 0, "title", "content")

        then:
        !note.id.isPresent
    }

    def "null constructor args are not allowed"() {
        when:
        new Note(null, "cont")

        then:
        thrown(NullPointerException)

        when:
        new Note("title", null)

        then:
        thrown(NullPointerException)

        when:
        new Note(null, 0, "", "")

        then:
        thrown(NullPointerException)

        when:
        new Note(OptionalLong.empty(), 0, null, "")

        then:
        thrown(NullPointerException)

        when:
        new Note(OptionalLong.empty(), 0, "", null)

        then:
        thrown(NullPointerException)
    }

    def "setters do not allow null args"() {
        Note note = new Note("", "")

        when:
        note.title = null

        then:
        thrown(NullPointerException)

        when:
        note.content = null

        then:
        thrown(NullPointerException)
    }
}
