package net.plshark.notes.service;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import net.plshark.ObjectNotFoundException;
import net.plshark.notes.Note;
import net.plshark.notes.repo.NotesRepository;

/**
 * Implementation for NotesService
 */
@Named
@Singleton
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepo;
    private final UserNotePermissionsService permissionService;

    /**
     * Create a new instance
     * @param notesRepo the repository to use to store notes
     * @param permissionService the service to check user permissions for notes
     */
    public NotesServiceImpl(NotesRepository notesRepo, UserNotePermissionsService permissionService) {
        this.notesRepo = Objects.requireNonNull(notesRepo, "notesRepo cannot be null");
        this.permissionService = Objects.requireNonNull(permissionService, "permissionService cannot be null");
    }

    @Override
    public Optional<Note> getForUser(long id, long userId) {
        return permissionService.userHasReadPermission(id, userId) ? notesRepo.get(id) : Optional.empty();
    }

    @Override
    public Note save(Note note, long userId) throws ObjectNotFoundException {
        Note savedNote;
        if (note.getId().isPresent()) {
            if (!permissionService.userHasWritePermission(note.getId().getAsLong(), userId))
                throw new ObjectNotFoundException("No note found for ID " + note.getId().getAsLong());
            savedNote = notesRepo.update(note);
        } else {
            savedNote = notesRepo.insert(note);
            permissionService.grantOwnerPermissions(savedNote.getId().getAsLong(), userId);
        }
        return savedNote;
    }

    @Override
    @Transactional
    public void deleteForUser(long id, long userId) throws ObjectNotFoundException {
        if (!permissionService.userIsOwner(id, userId))
            throw new ObjectNotFoundException("No note found for ID " + id);
        notesRepo.delete(id);
        permissionService.deletePermissionsForNote(id);
    }
}
