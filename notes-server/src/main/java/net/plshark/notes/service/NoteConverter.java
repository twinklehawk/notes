package net.plshark.notes.service;

import net.plshark.notes.Note;
import net.plshark.notes.entity.NoteEntity;

/**
 * Converts between {@link Note} and {@link NoteEntity}
 */
public class NoteConverter {

    /**
     * Convert a NoteEntity to a Note
     * @param entity the entity
     * @return the note
     */
    public Note from(NoteEntity entity) {
        Note note = new Note(entity.getTitle(), entity.getContent());
        entity.getId().ifPresent(id -> note.setId(id));
        note.setCorrelationId(entity.getCorrelationId());
        return note;
    }

    /**
     * Convert a Note to a NoteEntity
     * @param note the note
     * @param ownerId the owner ID
     * @return the entity
     */
    public NoteEntity from(Note note, long ownerId) {
        NoteEntity entity = new NoteEntity(ownerId, note.getTitle(), note.getContent());
        note.getId().ifPresent(id -> entity.setId(id));
        entity.setCorrelationId(note.getCorrelationId());
        return entity;
    }
}
