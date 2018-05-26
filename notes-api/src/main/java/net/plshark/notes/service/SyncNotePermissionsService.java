package net.plshark.notes.service;

import net.plshark.ObjectNotFoundException;
import net.plshark.notes.NotePermission;

/**
 * Service for modifying a user's permissions to interact with notes
 */
public interface SyncNotePermissionsService {

    /**
     * Set a user's permission for a note
     * @param id the note ID
     * @param userId the user ID to set permissions for
     * @param currentUserId the ID of the current user, must be the note owner to change permissions
     * @param permission the permissions to set
     * @throws ObjectNotFoundException if the note is not found for the user
     */
    void setPermissionForUser(long id, long userId, long currentUserId, NotePermission permission)
            throws ObjectNotFoundException;

    /**
     * Remove all permissions for a note from a user
     * @param id the note ID
     * @param userId the user ID of the user to remove permissions from
     * @param currentUserId the ID of the current user, must be the note owner to change permissions
     * @throws ObjectNotFoundException if the note is not found for the user
     */
    void removePermissionForUser(long id, long userId, long currentUserId) throws ObjectNotFoundException;
}
