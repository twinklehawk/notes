package net.plshark.notes.service;

import net.plshark.notes.NotePermission;
import reactor.core.publisher.Mono;

/**
 * Service for working with a user's permissions for notes
 */
public interface UserNotePermissionsService {

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

    /**
     * Check if a user has read permission for a note
     * @param noteId the note ID
     * @param username the username
     * @return if the user has read permission
     */
    Mono<Boolean> userHasReadPermission(long noteId, String username);

    /**
     * Check if a user has write permission for a note
     * @param noteId the note ID
     * @param username the username
     * @return if the user has write permission
     */
    Mono<Boolean> userHasWritePermission(long noteId, String username);

    /**
     * Check if a user owns a note
     * @param noteId the note ID
     * @param username the username
     * @return if the user owns the note
     */
    Mono<Boolean> userIsOwner(long noteId, String username);

    /**
     * Grant a user read and write permission for a new note
     * @param noteId the ID of the new note
     * @param username the username of the owner
     * @return an empty result
     */
    Mono<Void> grantOwnerPermissions(long noteId, String username);

    /**
     * Delete all permissions for a note
     * @param noteId the note ID
     * @return an empty result
     */
    Mono<Void> deletePermissionsForNote(long noteId);
}
