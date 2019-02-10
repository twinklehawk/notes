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
     * @param permission the permissions to set
     * @return an empty result
     */
    Mono<Void> setPermissionForUser(long id, String username, NotePermission permission);

    /**
     * Remove all permissions for a note from a user
     * @param id the note ID
     * @param username the username of the user to remove permissions from
     * @return an empty result
     */
    Mono<Void> removePermissionForUser(long id, String username);
}
