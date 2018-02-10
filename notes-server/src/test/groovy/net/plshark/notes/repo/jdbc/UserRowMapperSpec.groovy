package net.plshark.notes.repo.jdbc

import java.sql.ResultSet

import net.plshark.users.User
import spock.lang.Specification

class UserRowMapperSpec extends Specification {

    UserRowMapper mapper = new UserRowMapper()

    def "row mapped to user"() {
        ResultSet rs = Mock()
        rs.getLong("id") >> 5L
        rs.getString("username") >> "admin"
        rs.getString("password") >> "54321"

        when:
        User user = mapper.mapRow(rs, 1)

        then:
        user.id.asLong == 5
        user.username == "admin"
        user.password == "54321"
    }
}
