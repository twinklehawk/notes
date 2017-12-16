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
}
