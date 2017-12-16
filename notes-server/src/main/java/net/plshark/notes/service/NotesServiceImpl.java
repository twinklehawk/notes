package net.plshark.notes.service;

import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.EmptyResultDataAccessException;

import net.plshark.notes.Note;
import net.plshark.notes.ObjectNotFoundException;
import net.plshark.notes.repo.NotesRepository;

/**
 * Implementation for NotesService
 */
@Named
@Singleton
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepo;

    /**
     * Create a new instance
     * @param notesRepo the repository to use to store notes
     */
    public NotesServiceImpl(NotesRepository notesRepo) {
        this.notesRepo = Objects.requireNonNull(notesRepo, "notesRepo cannot be null");
    }

    @Override
    public Note get(long id) throws ObjectNotFoundException {
        try {
            return notesRepo.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("No note found for id " + id, e);
        }
    }

    @Override
    public Note save(Note note) {
        Note savedNote;
        if (note.getId().isPresent())
            savedNote = notesRepo.update(note);
        else
            savedNote = notesRepo.insert(note);
        return savedNote;
    }

    @Override
    public void delete(long id) {
        notesRepo.delete(id);
    }
}
