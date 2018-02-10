package net.plshark.users.repo;

import java.util.List;

import org.springframework.dao.DataAccessException;

import net.plshark.users.Role;

/**
 * Repository for adding and removing roles for users
 */
public interface UserRolesRepository {

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
