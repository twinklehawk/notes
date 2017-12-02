package net.plshark.notes.repo;

import net.plshark.notes.Note;

/**
 * Repository for saving, retrieving, and deleting notes
 */
public interface NotesRepository {

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note
     */
    Note get(long id);

    /**
     * Insert a new note
     * @param note the note to insert
     * @return the inserted note
     */
    Note insert(Note note);

    /**
     * Update an existing note
     * @param note the note to update
     * @return the updated note
     */
    Note update(Note note);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     */
    void delete(long id);
}
