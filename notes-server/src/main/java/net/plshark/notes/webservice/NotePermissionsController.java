package net.plshark.notes.webservice;

import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.plshark.ObjectNotFoundException;
import net.plshark.notes.NotePermission;
import net.plshark.notes.service.NotePermissionsService;
import net.plshark.users.service.UserAuthenticationService;

/**
 * Controller for modifying a user's permissions for a note
 */
@RestController
@RequestMapping("/notes/{id}/user-permissions")
public class NotePermissionsController {

    private final NotePermissionsService notePermissionsService;
    private final UserAuthenticationService userAuthService;

    /**
     * Create a new instance
     * @param notePermissionsService the service to use for note permissions
     * @param userAuthService the service to use to retrieve authenticated user information
     */
    public NotePermissionsController(NotePermissionsService notePermissionsService,
            UserAuthenticationService userAuthService) {
        this.notePermissionsService = Objects.requireNonNull(notePermissionsService,
                "notePermissionsService cannot be null");
        this.userAuthService = Objects.requireNonNull(userAuthService, "userAuthService cannot be null");
    }

    /**
     * Set a user's permissions for a note
     * @param id the note ID
     * @param userId the user ID of the user to set permissions for
     * @param permission the permissions to set
     * @param auth the currently authenticated user
     * @throws ObjectNotFoundException if the note is not found for the user
     */
    @PostMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setPermissionForUser(@PathVariable("id") long id, @PathVariable("userId") long userId,
            @RequestBody NotePermission permission, Authentication auth) throws ObjectNotFoundException {
        long currentUserId = userAuthService.getUserIdForAuthentication(auth);
        notePermissionsService.setPermissionForUser(id, userId, currentUserId, permission);
    }

    /**
     * Remove all permissions for a note from a user
     * @param id the note ID
     * @param userId the user ID of the user to remove permissions from
     * @param auth the currently authenticated user
     * @throws ObjectNotFoundException if the note is not found for the user
     */
    @DeleteMapping(path = "/{userId}")
    public void removePermissionForUser(@PathVariable("id") long id, @PathVariable("userId") long userId,
            Authentication auth) throws ObjectNotFoundException {
        long currentUserId = userAuthService.getUserIdForAuthentication(auth);
        notePermissionsService.removePermissionForUser(id, userId, currentUserId);
    }
}
