package net.plshark.notes.service;

import net.plshark.notes.NotePermission;
import reactor.core.publisher.Mono;

/**
 * Service for modifying a user's permissions to interact with notes
 */
public interface NotePermissionsService {

    /**
     * Set a user's permission for a note
     * @param id the note ID
     * @param username the username to set permissions for
     * @param currentUsername the username of the current user, must be the note owner to change permissions
     * @param permission the permissions to set
     * @return an empty result or ObjectNotFoundException if the note is not found for the current user
     */
    Mono<Void> setPermissionForUser(long id, String username, String currentUsername, NotePermission permission);

    /**
     * Remove all permissions for a note from a user
     * @param id the note ID
     * @param username the username of the user to remove permissions from
     * @param currentUsername the username of the current user, must be the note owner to change permissions
     * @return an empty result or ObjectNotFoundException if the note is not found for the current user
     */
    Mono<Void> removePermissionForUser(long id, String username, String currentUsername);
}
