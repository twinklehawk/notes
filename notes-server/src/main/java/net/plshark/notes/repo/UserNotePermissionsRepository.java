package net.plshark.notes.repo;

import org.springframework.dao.DataAccessException;

import net.plshark.notes.UserNotePermission;
import reactor.core.publisher.Mono;

/**
 * Repository for saving and retrieving a users' permissions for notes
 */
public interface UserNotePermissionsRepository {

    /**
     * Get a user's permissions for a note
     * @param userId the user ID
     * @param noteId the note ID
     * @return the permissions, may be empty if the user has no permissions for the note
     * @throws DataAccessException if the query fails
     */
    Mono<UserNotePermission> getByUserAndNote(long userId, long noteId);

    /**
     * Save a new permission
     * @param permission the permission
     * @return the saved permission
     * @throws DataAccessException if the insert fails
     */
    Mono<UserNotePermission> insert(UserNotePermission permission);

    /**
     * Update an existing permission
     * @param permission the permission to update
     * @return the updated permission
     * @throws DataAccessException if the update fails
     */
    Mono<UserNotePermission> update(UserNotePermission permission);

    /**
     * Delete a user's permission for a note
     * @param userId the user ID
     * @param noteId the note ID
     * @return an empty result
     */
    Mono<Void> deleteByUserAndNote(long userId, long noteId);

    /**
     * Delete all permissions for a note
     * @param noteId the note ID
     * @return an empty result
     */
    Mono<Void> deleteByNote(long noteId);
}
