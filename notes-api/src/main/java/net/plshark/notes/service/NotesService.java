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
     * @param username the username of the current user. The user should own or have read privileges on the note
     * @return the matching note, or an empty optional if the note was not found for the user
     */
    Mono<Note> getForUser(long id, String username);

    /**
     * Save a new note
     * @param note the note to save
     * @param username the username of the user saving the note
     * @return the saved note
     */
    Mono<Note> save(Note note, String username);

    /**
     * Update a note
     * @param note the note to update, must have the ID set
     * @param username the username of the user updating the note
     * @return the updated note
     */
    Mono<Note> update(Note note, String username);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @param username the username of the current user
     * @return an empty result
     */
    Mono<Void> deleteForUser(long id, String username);
}
