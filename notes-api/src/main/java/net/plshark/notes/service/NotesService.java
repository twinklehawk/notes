package net.plshark.notes.service;

import java.util.Optional;

import net.plshark.notes.Note;
import net.plshark.notes.ObjectNotFoundException;

/**
 * Service for notes
 */
public interface NotesService {

    /**
     * Get a note by ID for a user
     * @param id the note ID
     * @param userId the ID of the current user. The user should own or have read privileges on the note
     * @return the matching note, or an empty optional if the note was not found for the user
     */
    Optional<Note> getForUser(long id, long userId);

    /**
     * Save a note
     * @param note the note to save. If the note's ID is not empty, the existing
     *            note for this ID will be updated
     * @param userId the ID of the user saving the note
     * @return the saved note
     * @throws ObjectNotFoundException if the note has an ID but cannot be found
     */
    Note save(Note note, long userId) throws ObjectNotFoundException;

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @param userId the ID of the current user
     * @throws ObjectNotFoundException if the note could not be found
     */
    void deleteForUser(long id, long userId) throws ObjectNotFoundException;
}
