package net.plshark.notes.repo.jdbc

import java.sql.ResultSet

import net.plshark.notes.Note
import spock.lang.Specification

class NoteRowMapperSpec extends Specification {

    NoteRowMapper mapper = new NoteRowMapper()

    def "row mapped to note"() {
        ResultSet rs = Mock()
        rs.getLong("id") >> 5L
        rs.getLong("correlation_id") >> 7L
        rs.getString("title") >> "title"
        rs.getString("content") >> "note"

        when:
        Note note = mapper.mapRow(rs, 1)

        then:
        note.id.asLong == 5
        note.correlationId == 7
        note.title == "title"
        note.content == "note"
    }
}
