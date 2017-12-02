package net.plshark.notes.repo.jdbc;

import org.junit.Test;

/**
 * Tests for {@link JdbcNotesRepository}
 */
public class JdbcNotesRepositoryTest {

    // TODO integration tests

    /**
     * Verify an exception is thrown if null objects are passed in
     */
    @Test(expected = NullPointerException.class)
    public void badConstructorTest() {
        new JdbcNotesRepository(null);
    }
}
