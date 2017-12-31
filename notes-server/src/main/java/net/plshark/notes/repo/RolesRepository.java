package net.plshark.notes.repo;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import net.plshark.notes.Role;

/**
 * Repository for saving, deleting, and retrieving roles
 */
public interface RolesRepository {

    /**
     * Get a role by ID
     * @param id the ID
     * @return the matching role
     * @throws EmptyResultDataAccessException if there is no matching role
     * @throws DataAccessException if the query fails
     */
    Role getForId(long id);

    /**
     * Get a role by name
     * @param name the role name
     * @return the matching role
     * @throws EmptyResultDataAccessException if there is no matching role
     * @throws DataAccessException if the query fails
     */
    Role getForName(String name);

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
