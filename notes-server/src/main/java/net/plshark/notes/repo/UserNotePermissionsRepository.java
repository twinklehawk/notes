package net.plshark.notes.repo;

import net.plshark.notes.UserNotePermission;
import reactor.core.publisher.Mono;

/**
 * Repository for saving and retrieving a user's permissions for notes
 */
public interface UserNotePermissionsRepository {

    /**
     * Get a user's permissions for a note
     * @param username the username
     * @param noteId the note ID
     * @return the permissions, may be empty if the user has no permissions for the note
     */
    Mono<UserNotePermission> getByUserAndNote(String username, long noteId);

    /**
     * Save a new permission
     * @param permission the permission
     * @return the saved permission
     */
    Mono<UserNotePermission> insert(UserNotePermission permission);

    /**
     * Update an existing permission
     * @param permission the permission to update
     * @return the updated permission
     */
    Mono<UserNotePermission> update(UserNotePermission permission);

    /**
     * Delete a user's permission for a note
     * @param username the username
     * @param noteId the note ID
     * @return an empty result
     */
    Mono<Void> deleteByUserAndNote(String username, long noteId);

    /**
     * Delete all permissions for a note
     * @param noteId the note ID
     * @return an empty result
     */
    Mono<Void> deleteByNote(long noteId);
}
