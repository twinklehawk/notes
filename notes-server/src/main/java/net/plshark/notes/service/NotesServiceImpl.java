package net.plshark.notes.service;

import java.util.Objects;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.transaction.annotation.Transactional;

import net.plshark.ObjectNotFoundException;
import net.plshark.notes.Note;
import net.plshark.notes.repo.NotesRepository;
import reactor.core.publisher.Mono;

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
    public Mono<Note> getForUser(long id, String username) {
        return permissionService.userHasReadPermission(id, username)
            .flatMap(canRead -> canRead ? notesRepo.get(id) : Mono.empty());
    }

    @Override
    @Transactional
    public Mono<Note> save(Note note, String username) {
        return notesRepo.insert(note)
                .flatMap(insertedNote ->
                        permissionService.grantOwnerPermissions(insertedNote.getId(), username)
                        .thenReturn(insertedNote));
    }

    @Override
    public Mono<Note> update(Note note, String username) {
        return permissionService.userHasWritePermission(note.getId(), username)
                .flatMap(canWrite -> canWrite ?
                        notesRepo.update(note) :
                        Mono.error(new ObjectNotFoundException("No note found for ID " + note.getId())));
    }

    @Override
    @Transactional
    public Mono<Void> deleteForUser(long id, String username) {
        return permissionService.userIsOwner(id, username)
            .flatMap(isOwner -> isOwner ?
                    notesRepo.delete(id).and(permissionService.deletePermissionsForNote(id)) :
                    Mono.error(new ObjectNotFoundException("No note found for ID " + id)));
    }
}
