package net.plshark.notes.service;

import java.util.Objects;
import javax.inject.Named;
import javax.inject.Singleton;

import net.plshark.ObjectNotFoundException;
import net.plshark.notes.NotePermission;
import net.plshark.notes.UserNotePermission;
import net.plshark.notes.repo.UserNotePermissionsRepository;
import reactor.core.publisher.Mono;

/**
 * Default implementation of NotePermissionsService
 */
@Named
@Singleton
public class UserNotePermissionsServiceImpl implements UserNotePermissionsService, NotePermissionsService {

    private final UserNotePermissionsRepository permissionRepo;

    /**
     * Create a new instance
     * @param permissionRepo the repository for retrieving and storing user permissions on notes
     */
    public UserNotePermissionsServiceImpl(UserNotePermissionsRepository permissionRepo) {
        this.permissionRepo = Objects.requireNonNull(permissionRepo, "permissionRepo cannot be null");
    }

    @Override
    public Mono<Boolean> userHasReadPermission(long noteId, long userId) {
        return permissionRepo.getByUserAndNote(userId, noteId)
                .map(permission -> permission.isReadable())
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Void> grantOwnerPermissions(long noteId, long userId) {
        return permissionRepo.insert(new UserNotePermission(userId, noteId, true, true, true)).then();
    }

    @Override
    public Mono<Boolean> userHasWritePermission(long noteId, long userId) {
        return permissionRepo.getByUserAndNote(userId, noteId)
                .map(permission -> permission.isWritable())
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> userIsOwner(long noteId, long userId) {
        return permissionRepo.getByUserAndNote(userId, noteId)
                .map(permission -> permission.isOwner())
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Void> deletePermissionsForNote(long noteId) {
        return permissionRepo.deleteByNote(noteId);
    }

    @Override
    public Mono<Void> setPermissionForUser(long id, long userId, long currentUserId, NotePermission permission) {
        return userIsOwner(id, currentUserId)
            .flatMap(isOwner -> isOwner ? permissionRepo.getByUserAndNote(userId, id) :
                Mono.error(new ObjectNotFoundException("No note found for ID " + id)))
            .flatMap(p -> {
                p.setReadable(permission.isReadable());
                p.setWritable(permission.isWritable());
                return permissionRepo.update(p);
            })
            .switchIfEmpty(Mono.defer(() ->
                permissionRepo.insert(new UserNotePermission(userId, id, permission.isReadable(), permission.isWritable()))))
            .then();
    }

    @Override
    public Mono<Void> removePermissionForUser(long id, long userId, long currentUserId) {
        return userIsOwner(id, currentUserId)
            .flatMap(isOwner -> isOwner ? permissionRepo.deleteByUserAndNote(userId, id) :
                Mono.error(new ObjectNotFoundException("No note found for ID " + id)));
    }
}
