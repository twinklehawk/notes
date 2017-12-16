package net.plshark.notes.repo;

import java.util.List;

import org.springframework.dao.DataAccessException;

import net.plshark.notes.Role;

/**
 * Repository for saving, deleting, and retrieving roles
 */
public interface RoleRepository {

    /**
     * Get all the roles for a user
     * @param userId the user ID
     * @return the roles for that user
     * @throws DataAccessException if the query fails
     */
    List<Role> getRolesForUser(long userId);

    /**
     * Insert a new role
     * @param role the role to insert
     * @return the inserted role, will have the ID set
     * @throws DataAccessException if the insert fails
     */
    Role insert(Role role);

    /**
     * Delete a role by ID
     * @param roleId the role ID
     * @throws DataAccessException if the delete fails
     */
    void delete(long roleId);
}
