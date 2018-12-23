package net.plshark.notes.webservice;

import java.util.Objects;
import net.plshark.notes.NotePermission;
import net.plshark.notes.service.NotePermissionsService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller for modifying a user's permissions for a note
 */
@RestController
@RequestMapping("/notes/{id}/user-permissions")
public class NotePermissionsController {

    private final NotePermissionsService notePermissionsService;

    /**
     * Create a new instance
     * @param notePermissionsService the service to use for note permissions
     */
    public NotePermissionsController(NotePermissionsService notePermissionsService) {
        this.notePermissionsService = Objects.requireNonNull(notePermissionsService,
                "notePermissionsService cannot be null");
    }

    /**
     * Set a user's permissions for a note
     * @param id the note ID
     * @param username the username of the user to set permissions for
     * @param permission the permissions to set
     * @param auth the currently authenticated user
     * @return an empty result or ObjectNotFoundException if the note is not found for the user
     */
    @PostMapping(path = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> setPermissionForUser(@PathVariable("id") long id, @PathVariable("username") String username,
            @RequestBody NotePermission permission, Authentication auth) {
        return notePermissionsService.setPermissionForUser(id, username, auth.getName(), permission);
    }

    /**
     * Remove all permissions for a note from a user
     * @param id the note ID
     * @param username the username of the user to remove permissions from
     * @param auth the currently authenticated user
     * @return an empty result or ObjectNotFoundException if the note is not found for the user
     */
    @DeleteMapping(path = "/{username}")
    public Mono<Void> removePermissionForUser(@PathVariable("id") long id, @PathVariable("username") String username,
            Authentication auth) {
        return notePermissionsService.removePermissionForUser(id, username, auth.getName());
    }
}
