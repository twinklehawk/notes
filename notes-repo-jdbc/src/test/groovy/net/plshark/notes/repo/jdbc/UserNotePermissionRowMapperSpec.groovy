package net.plshark.notes.repo.jdbc

import java.sql.ResultSet

import net.plshark.notes.UserNotePermission
import spock.lang.Specification

class UserNotePermissionRowMapperSpec extends Specification {

    UserNotePermissionRowMapper mapper = new UserNotePermissionRowMapper()

    def "row mapped to permission"() {
        ResultSet rs = Mock()
        rs.getLong("user_id") >> 5L
        rs.getLong("note_id") >> 6L
        rs.getBoolean("readable") >> true
        rs.getBoolean("writable") >> false
        rs.getBoolean("owner") >> false

        when:
        UserNotePermission perm = mapper.mapRow(rs, 1)

        then:
        perm.userId == 5
        perm.noteId == 6
        perm.readable == true
        perm.writable == false
        perm.owner == false
    }
}
