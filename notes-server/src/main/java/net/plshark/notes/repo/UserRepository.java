package net.plshark.notes.repo;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import net.plshark.notes.User;

/**
 * Repository for saving, deleting, and retrieving users
 */
public interface UserRepository {

    /**
     * Get a user by the username
     * @param username the username
     * @return the matching user
     * @throws EmptyResultDataAccessException if there is no matching user
     * @throws DataAccessException if the query fails
     */
    User getForUsername(String username);

    /**
     * Insert a new user
     * @param user the user to insert
     * @return the inserted user, will have the ID set
     */
    User insert(User user);

    /**
     * Delete a user by ID
     * @param userId the user ID
     */
    void delete(long userId);
}
