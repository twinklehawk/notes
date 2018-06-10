package net.plshark.notes.service;

import net.plshark.notes.Note;
import reactor.core.publisher.Mono;

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
    Mono<Note> getForUser(long id, long userId);

    /**
     * Save a note
     * @param note the note to save. If the note's ID is not empty, the existing
     *            note for this ID will be updated
     * @param userId the ID of the user saving the note
     * @return the saved note
     */
    Mono<Note> save(Note note, long userId);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @param userId the ID of the current user
     * @return an empty result
     */
    Mono<Void> deleteForUser(long id, long userId);
}
