package net.plshark.notes.service;

import net.plshark.notes.Note;
import reactor.core.publisher.Mono;

/**
 * Service for notes
 */
public interface NotesService {

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note, or an empty optional if the note was not found
     */
    Mono<Note> get(long id);

    /**
     * Save a new note
     * @param note the note to save
     * @return the saved note
     */
    Mono<Note> save(Note note);

    /**
     * Update a note
     * @param note the note to update, must have the ID set
     * @return the updated note
     */
    Mono<Note> update(Note note);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @return an empty result
     */
    Mono<Void> delete(long id);
}
