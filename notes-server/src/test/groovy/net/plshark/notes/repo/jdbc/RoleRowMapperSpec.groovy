package net.plshark.notes.repo.jdbc

import java.sql.ResultSet

import net.plshark.notes.Role
import spock.lang.Specification

class RoleRowMapperSpec extends Specification {

    RoleRowMapper mapper = new RoleRowMapper()

    def "row mapped to note"() {
        ResultSet rs = Mock()
        rs.getLong("id") >> 5L
        rs.getString("name") >> "admin"

        when:
        Role role = mapper.mapRow(rs, 1)

        then:
        role.id.asLong == 5
        role.name == "admin"
    }
}
