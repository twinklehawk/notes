package net.plshark.notes.service;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import net.plshark.notes.NotePermission;
import net.plshark.notes.ObjectNotFoundException;
import net.plshark.notes.UserNotePermission;
import net.plshark.notes.repo.UserNotePermissionsRepository;

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
    public boolean userHasReadPermission(long noteId, long userId) {
        return permissionRepo.getByUserAndNote(userId, noteId).map(permission -> permission.isReadable()).orElse(false);
    }

    @Override
    public void grantOwnerPermissions(long noteId, long userId) {
        permissionRepo.insert(new UserNotePermission(userId, noteId, true, true, true));
    }

    @Override
    public boolean userHasWritePermission(long noteId, long userId) {
        return permissionRepo.getByUserAndNote(userId, noteId).map(permission -> permission.isWritable()).orElse(false);
    }

    @Override
    public boolean userIsOwner(long noteId, long userId) {
        return permissionRepo.getByUserAndNote(userId, noteId).map(permission -> permission.isOwner()).orElse(false);
    }

    @Override
    public void deletePermissionsForNote(long noteId) {
        permissionRepo.deleteByNote(noteId);
    }

    @Override
    public void setPermissionForUser(long id, long userId, long currentUserId, NotePermission permission)
            throws ObjectNotFoundException {
        if (!userIsOwner(id, currentUserId))
            throw new ObjectNotFoundException("No note found for ID " + id);

        Optional<UserNotePermission> currentPermission = permissionRepo.getByUserAndNote(userId, id);
        if (currentPermission.isPresent()) {
            UserNotePermission p = currentPermission.get();
            p.setReadable(permission.isReadable());
            p.setWritable(permission.isWritable());
            permissionRepo.update(p);
        } else {
            permissionRepo.insert(new UserNotePermission(userId, id, permission.isReadable(), permission.isWritable()));
        }
    }

    @Override
    public void removePermissionForUser(long id, long userId, long currentUserId) throws ObjectNotFoundException {
        if (!userIsOwner(id, currentUserId))
            throw new ObjectNotFoundException("No note found for ID " + id);

        permissionRepo.deleteByUserAndNote(userId, id);
    }
}
