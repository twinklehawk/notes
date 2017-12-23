package net.plshark.notes

import spock.lang.Specification

class NoteSpec extends Specification {

    def "constructors set id to empty"() {
        Note note

        when:
        note = new Note()

        then:
        note.id != null
        !note.id.isPresent

        when:
        note = new Note(1, 2, "title", "content")

        then:
        note.id != null
        !note.id.isPresent
    }
}
