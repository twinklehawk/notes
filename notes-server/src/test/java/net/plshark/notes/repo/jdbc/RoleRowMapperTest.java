package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import net.plshark.notes.Role;

/**
 * Tests for {@link RoleRowMapper}
 */
public class RoleRowMapperTest {

    /**
     * Verify a row is mapped to a role correctly
     * @throws SQLException unexpected
     */
    @Test
    public void mapRowTest() throws SQLException {
        RoleRowMapper mapper = new RoleRowMapper();
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id")).thenReturn(5L);
        Mockito.when(rs.getString("name")).thenReturn("admin");

        Role role = mapper.mapRow(rs, 2);
        Assert.assertEquals(5L, role.getId());
        Assert.assertEquals("admin", role.getName());
    }
}
