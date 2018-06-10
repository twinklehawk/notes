package net.plshark.notes.repo;

import java.util.Optional;

import org.springframework.dao.DataAccessException;

import net.plshark.notes.UserNotePermission;

/**
 * Repository for saving and retrieving a users' permissions for notes
 */
public interface SyncUserNotePermissionsRepository {

    /**
     * Get a user's permissions for a note
     * @param userId the user ID
     * @param noteId the note ID
     * @return the permissions, may be empty if the user has no permissions for the note
     * @throws DataAccessException if the query fails
     */
    Optional<UserNotePermission> getByUserAndNote(long userId, long noteId);

    /**
     * Save a new permission
     * @param permission the permission
     * @return the saved permission
     * @throws DataAccessException if the insert fails
     */
    UserNotePermission insert(UserNotePermission permission);

    /**
     * Update an existing permission
     * @param permission the permission to update
     * @return the updated permission
     * @throws DataAccessException if the update fails
     */
    UserNotePermission update(UserNotePermission permission);

    /**
     * Delete a user's permission for a note
     * @param userId the user ID
     * @param noteId the note ID
     */
    void deleteByUserAndNote(long userId, long noteId);

    /**
     * Delete all permissions for a note
     * @param noteId the note ID
     */
    void deleteByNote(long noteId);
}
