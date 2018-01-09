package net.plshark.notes.service;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import net.plshark.notes.Note;
import net.plshark.notes.entity.NoteEntity;
import net.plshark.notes.repo.NotesRepository;

/**
 * Implementation for NotesService
 */
@Named
@Singleton
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepo;
    private final NoteConverter converter = new NoteConverter();

    /**
     * Create a new instance
     * @param notesRepo the repository to use to store notes
     */
    public NotesServiceImpl(NotesRepository notesRepo) {
        this.notesRepo = Objects.requireNonNull(notesRepo, "notesRepo cannot be null");
    }

    @Override
    public Optional<Note> getForUser(long id, long userId) {
        return notesRepo.getByIdForUser(id, userId).map(note -> converter.from(note));
    }

    @Override
    public Note save(Note note, long userId) {
        NoteEntity savedNote;
        if (note.getId().isPresent()) {
            NoteEntity currentNote = notesRepo.get(note.getId().getAsLong());
            savedNote = notesRepo.update(converter.from(note, currentNote.getOwnerId()));
        } else {
            NoteEntity entity = converter.from(note, userId);
            savedNote = notesRepo.insert(entity);
        }
        return converter.from(savedNote);
    }

    @Override
    public void deleteForUser(long id, long userId) {
        notesRepo.deleteByIdForUser(id, userId);
    }
}
