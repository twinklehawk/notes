package net.plshark.users.service;

import net.plshark.users.model.Role;
import net.plshark.users.model.User;
import reactor.core.publisher.Mono;

/**
 * Service for modifying users and roles
 */
public interface UserManagementService {

    /**
     * Save a new user
     * @param user the user
     * @return the saved user
     */
    Mono<User> saveUser(User user);

    /**
     * Delete a user by ID
     * @param userId the user ID
     * @return an empty result
     */
    Mono<Void> deleteUser(long userId);

    /**
     * Delete a user
     * @param user the user
     * @return an empty result
     */
    Mono<Void> deleteUser(User user);

    /**
     * Update a user's password
     * @param userId the ID of the user
     * @param currentPassword the current password, used for verification
     * @param newPassword the new password
     * @return an empty result or ObjectNotFoundException if the user was not found
     */
    Mono<Void> updateUserPassword(long userId, String currentPassword, String newPassword);

    /**
     * Retrieve a role by name
     * @param name the role name
     * @return the matching role
     */
    Mono<Role> getRoleByName(String name);

    /**
     * Save a new role
     * @param role the role
     * @return the saved role
     */
    Mono<Role> saveRole(Role role);

    /**
     * Delete a role
     * @param roleId the role ID
     * @return an empty result
     */
    Mono<Void> deleteRole(long roleId);

    /**
     * Grant a role to a user
     * @param userId the ID of the user to grant the role to
     * @param roleId the ID of the role to grant
     * @return an empty result or ObjectNotFoundException if the user or role does not exist
     */
    Mono<Void> grantRoleToUser(long userId, long roleId);

    /**
     * Grant a role to a user
     * @param user the user to grant the role to
     * @param role the role to grant
     * @return an empty result or ObjectNotFoundException if the user or role does not exist
     */
    Mono<Void> grantRoleToUser(User user, Role role);

    /**
     * Remove a role from a user
     * @param userId the ID of the user to remove the role from
     * @param roleId the ID of the role to remove
     * @return an empty result or ObjectNotFoundException if the user does not exist
     */
    Mono<Void> removeRoleFromUser(long userId, long roleId);
}
