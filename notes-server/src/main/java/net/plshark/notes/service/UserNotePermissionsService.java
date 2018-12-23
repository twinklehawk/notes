package net.plshark.notes.service;

import reactor.core.publisher.Mono;

/**
 * Service for working with a user's permissions for notes
 */
public interface UserNotePermissionsService {

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
