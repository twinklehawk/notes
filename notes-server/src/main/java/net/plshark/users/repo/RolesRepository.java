package net.plshark.users.repo;

import org.springframework.dao.DataAccessException;
import net.plshark.users.Role;
import reactor.core.publisher.Mono;

/**
 * Repository for saving, deleting, and retrieving roles
 */
public interface RolesRepository {

    /**
     * Get a role by ID
     * @param id the ID
     * @return the matching role
     * @throws DataAccessException if the query fails
     */
    Mono<Role> getForId(long id);

    /**
     * Get a role by name
     * @param name the role name
     * @return the matching role
     * @throws DataAccessException if the query fails
     */
    Mono<Role> getForName(String name);

    /**
     * Insert a new role
     * @param role the role to insert
     * @return the inserted role, will have the ID set
     * @throws DataAccessException if the insert fails
     */
    Mono<Role> insert(Role role);

    /**
     * Delete a role by ID
     * @param roleId the role ID
     * @return an empty result
     * @throws DataAccessException if the delete fails
     */
    Mono<Void> delete(long roleId);
}
