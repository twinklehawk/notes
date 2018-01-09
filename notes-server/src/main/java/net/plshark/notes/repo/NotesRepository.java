package net.plshark.notes.repo;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import net.plshark.notes.entity.NoteEntity;

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
    NoteEntity get(long id);

    /**
     * Get a note owned by a user
     * @param id the note ID
     * @param userId the owning user ID
     * @return the matching note if found
     * @throws DataAccessException if the query fails
     */
    Optional<NoteEntity> getByIdForUser(long id, long userId);

    /**
     * Insert a new note
     * @param note the note to insert
     * @return the inserted note
     * @throws DataAccessException if the insert fails
     */
    NoteEntity insert(NoteEntity note);

    /**
     * Update an existing note
     * @param note the note to update
     * @return the updated note
     * @throws DataAccessException if the update fails
     */
    NoteEntity update(NoteEntity note);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @throws DataAccessException if the delete fails
     */
    void delete(long id);
}
