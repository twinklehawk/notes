package net.plshark.notes.service;

import net.plshark.notes.ObjectNotFoundException;
import net.plshark.notes.Role;
import net.plshark.notes.User;

/**
 * Service for modifying users and roles
 */
public interface UserManagementService {

    /**
     * Save a new user
     * @param user the user
     * @return the saved user
     */
    User saveUser(User user);

    /**
     * Delete a user by ID
     * @param userId the user ID
     */
    void deleteUser(long userId);

    /**
     * Update a user's password
     * @param userId the ID of the user
     * @param currentPassword the current password, used for verification
     * @param newPassword the new password
     * @throws ObjectNotFoundException if the user was not found
     */
    void updateUserPassword(long userId, String currentPassword, String newPassword) throws ObjectNotFoundException;

    /**
     * Save a new role
     * @param role the role
     * @return the saved role
     */
    Role saveRole(Role role);

    /**
     * Delete a role
     * @param roleId the role ID
     */
    void deleteRole(long roleId);

    /**
     * Grant a role to a user
     * @param userId the ID of the user to grant the role to
     * @param roleId the ID of the role to grant
     * @throws ObjectNotFoundException if the user or role does not exist
     */
    void grantRoleToUser(long userId, long roleId) throws ObjectNotFoundException;

    /**
     * Remove a role from a user
     * @param userId the ID of the user to remove the role from
     * @param roleId the ID of the role to remove
     * @throws ObjectNotFoundException if the user does not exist
     */
    void removeRoleFromUser(long userId, long roleId) throws ObjectNotFoundException;
}
