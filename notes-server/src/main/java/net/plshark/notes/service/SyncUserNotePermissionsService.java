package net.plshark.notes.service;

/**
 * Service for working with a user's permissions for notes
 */
public interface SyncUserNotePermissionsService {

    /**
     * Check if a user has read permission for a note
     * @param noteId the note ID
     * @param userId the user ID
     * @return if the user has read permission
     */
    boolean userHasReadPermission(long noteId, long userId);

    /**
     * Check if a user has write permission for a note
     * @param noteId the note ID
     * @param userId the user ID
     * @return if the user has write permission
     */
    boolean userHasWritePermission(long noteId, long userId);

    /**
     * Check if a user owns a note
     * @param noteId the note ID
     * @param userId the user ID
     * @return if the user owns the note
     */
    boolean userIsOwner(long noteId, long userId);

    /**
     * Grant a user read and write permission for a new note
     * @param noteId the ID of the new note
     * @param userId the user ID of the owner
     */
    void grantOwnerPermissions(long noteId, long userId);

    /**
     * Delete all permissions for a note
     * @param noteId the note ID
     */
    void deletePermissionsForNote(long noteId);
}
