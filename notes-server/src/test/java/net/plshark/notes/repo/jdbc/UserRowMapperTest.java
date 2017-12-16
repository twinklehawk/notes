package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import net.plshark.notes.User;

/**
 * Tests for {@link UserRowMapper}
 */
public class UserRowMapperTest {

    /**
     * Verify a row is mapped to a user correctly
     * @throws SQLException unexpected
     */
    @Test
    public void mapRowTest() throws SQLException {
        UserRowMapper mapper = new UserRowMapper();
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id")).thenReturn(5L);
        Mockito.when(rs.getString("username")).thenReturn("admin");
        Mockito.when(rs.getString("password")).thenReturn("54321");

        User user = mapper.mapRow(rs, 2);
        Assert.assertEquals(5L, user.getId().getAsLong());
        Assert.assertEquals("admin", user.getUsername());
        Assert.assertEquals("54321", user.getPassword());
    }
}
