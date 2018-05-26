package net.plshark.notes.service;

import reactor.core.publisher.Mono;

/**
 * Service for working with a user's permissions for notes
 */
public interface UserNotePermissionsService {

    /**
     * Check if a user has read permission for a note
     * @param noteId the note ID
     * @param userId the user ID
     * @return if the user has read permission
     */
    Mono<Boolean> userHasReadPermission(long noteId, long userId);

    /**
     * Check if a user has write permission for a note
     * @param noteId the note ID
     * @param userId the user ID
     * @return if the user has write permission
     */
    Mono<Boolean> userHasWritePermission(long noteId, long userId);

    /**
     * Check if a user owns a note
     * @param noteId the note ID
     * @param userId the user ID
     * @return if the user owns the note
     */
    Mono<Boolean> userIsOwner(long noteId, long userId);

    /**
     * Grant a user read and write permission for a new note
     * @param noteId the ID of the new note
     * @param userId the user ID of the owner
     * @return an empty result
     */
    Mono<Void> grantOwnerPermissions(long noteId, long userId);

    /**
     * Delete all permissions for a note
     * @param noteId the note ID
     * @return an empty result
     */
    Mono<Void> deletePermissionsForNote(long noteId);
}
