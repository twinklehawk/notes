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
     * @throws DataAccessException if the insert fails
     */
    User insert(User user);

    /**
     * Update an existing user's password
     * @param id the ID of the user to update
     * @param currentPassword the current password
     * @param newPassword the new password
     * @throws EmptyResultDataAccessException if no user exists with the ID or currentPassword
     * @throws DataAccessException if the update fails
     */
    void updatePassword(long id, String currentPassword, String newPassword);

    /**
     * Delete a user by ID
     * @param userId the user ID
     * @throws DataAccessException if the delete fails
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
     * @throws DataAccessException if the insert fails
     */
    void insertUserRole(long userId, long roleId);

    /**
     * Remove a role to a user
     * @param userId the ID of the user to remove the role from
     * @param roleId the ID of the role to remove
     * @throws DataAccessException if the delete fails
     */
    void deleteUserRole(long userId, long roleId);

    /**
     * Delete all user roles for a user
     * @param userId the user ID
     * @throws DataAccessException if the delete fails
     */
    void deleteUserRolesForUser(long userId);

    /**
     * Delete all user roles for a role
     * @param roleId the role ID
     * @throws DataAccessException if the delete fails
     */
    void deleteUserRolesForRole(long roleId);
}
