package net.plshark.notes

import spock.lang.*
import java.time.OffsetDateTime

class NotePermissionSpec extends Specification {

    def "creator sets correct fields"() {
        when:
        NotePermission permission = NotePermission.create(true, false)

        then:
        permission.readable == true
        permission.writable == false
    }
}
