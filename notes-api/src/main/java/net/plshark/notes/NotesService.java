package net.plshark.notes;

/**
 * Service for notes
 */
public interface NotesService {

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note
     * @throws ObjectNotFoundException if the matching note was not found
     */
    Note get(long id) throws ObjectNotFoundException;

    /**
     * Save a note
     * @param note the note to save. If the note's ID is not empty, the existing
     *            note for this ID will be updated
     * @return the saved note
     */
    Note save(Note note);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     */
    void delete(long id);
}
