package net.plshark.notes.repo;

import net.plshark.notes.Note;
import reactor.core.publisher.Mono;

/**
 * Repository for saving, retrieving, and deleting notes
 */
public interface NotesRepository {

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note
     */
    Mono<Note> get(long id);

    /**
     * Insert a new note
     * @param note the note to insert
     * @return the inserted note
     */
    Mono<Note> insert(Note note);

    /**
     * Update an existing note
     * @param note the note to update
     * @return the updated note
     */
    Mono<Note> update(Note note);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @return an empty Mono
     */
    Mono<Void> delete(long id);
}
