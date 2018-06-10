package net.plshark.notes.repo;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

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
     * @throws EmptyResultDataAccessException if there is no matching note
     * @throws DataAccessException if the query fails
     */
    Mono<Note> get(long id);

    /**
     * Insert a new note
     * @param note the note to insert
     * @return the inserted note
     * @throws DataAccessException if the insert fails
     */
    Mono<Note> insert(Note note);

    /**
     * Update an existing note
     * @param note the note to update
     * @return the updated note
     * @throws DataAccessException if the update fails
     */
    Mono<Note> update(Note note);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @return an empty Mono
     * @throws DataAccessException if the delete fails
     */
    Mono<Void> delete(long id);
}
