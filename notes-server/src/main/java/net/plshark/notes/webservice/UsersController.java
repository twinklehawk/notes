package net.plshark.notes.webservice;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.plshark.notes.ObjectNotFoundException;
import net.plshark.notes.PasswordChangeRequest;
import net.plshark.notes.User;
import net.plshark.notes.service.UserManagementService;

/**
 * Controller providing web service methods for users
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserManagementService userMgmtService;

    /**
     * Create a new instance
     * @param userManagementService the service to use to modify users
     */
    public UsersController(UserManagementService userManagementService) {
        this.userMgmtService = userManagementService;
    }

    /**
     * Insert a new user
     * @param user the user to insert
     * @return the inserted user
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User insert(@RequestBody User user) {
        User savedUser = userMgmtService.saveUser(user);
        // don't send back the password
        savedUser.setPassword(null);
        return savedUser;
    }

    /**
     * Delete a user by ID
     * @param id the user ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        userMgmtService.deleteUser(id);
    }

    /**
     * Change a user's password
     * @param userId the ID of the user
     * @param request the password change request
     * @throws ObjectNotFoundException if the user was not found
     */
    @PostMapping(path = "/{id}/password")
    public void changePassword(@PathVariable("id") long userId, @RequestBody PasswordChangeRequest request)
            throws ObjectNotFoundException {
        userMgmtService.updateUserPassword(userId, request.getCurrentPassword(), request.getNewPassword());
    }

    /**
     * Grant a role to a user
     * @param userId the ID of the user to grant to
     * @param roleId the ID of the role to grant
     */
    @PostMapping(path = "/{userId}/roles/{roleId}")
    public void grantRole(@PathVariable("userId") long userId, @PathVariable("roleId") long roleId) {
        userMgmtService.grantRoleToUser(userId, roleId);
    }

    /**
     * Remove a role from a user
     * @param userId the ID of the user to remove the role from
     * @param roleId the ID of the role to remove
     */
    @DeleteMapping(path = "/{userId}/roles/{roleId}")
    public void removeRole(@PathVariable("userId") long userId, @PathVariable("roleId") long roleId) {
        userMgmtService.removeRoleFromUser(userId, roleId);
    }
}
