package net.plshark.notes.repo;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import net.plshark.notes.Role;
import net.plshark.notes.User;

/**
 * Repository for saving, deleting, and retrieving users
 */
public interface UserRepository {

    /**
     * Get a user by user ID
     * @param id the user ID
     * @return the matching user
     * @throws EmptyResultDataAccessException if there is no matching user
     * @throws DataAccessException if the query fails
     */
    User getForId(long id);

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
     * Update an existing user. The username cannot be changed after a user is
     * created
     * @param user the user to update
     * @return the updated user
     */
    User update(User user);

    /**
     * Delete a user by ID
     * @param userId the user ID
     */
    void delete(long userId);

    /**
     * Get all the roles for a user
     * @param userId the user ID
     * @return the roles for that user
     * @throws DataAccessException if the query fails
     */
    List<Role> getRolesForUser(long userId);

    /**
     * Grant a role to a user
     * @param userId the ID of the user to grant the role to
     * @param roleId the ID of the role to grant
     */
    void insertUserRole(long userId, long roleId);

    /**
     * Remove a role to a user
     * @param userId the ID of the user to remove the role from
     * @param roleId the ID of the role to remove
     */
    void deleteUserRole(long userId, long roleId);
}
