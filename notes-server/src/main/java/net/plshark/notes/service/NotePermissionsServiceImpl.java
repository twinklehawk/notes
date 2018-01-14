package net.plshark.notes.service;

import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import net.plshark.notes.UserNotePermission;
import net.plshark.notes.repo.UserNotePermissionsRepository;

/**
 * Default implementation of NotePermissionsService
 */
@Named
@Singleton
public class NotePermissionsServiceImpl implements NotePermissionsService {

    private final UserNotePermissionsRepository permissionRepo;

    /**
     * Create a new instance
     * @param permissionRepo the repository for retrieving and storing user permissions on notes
     */
    public NotePermissionsServiceImpl(UserNotePermissionsRepository permissionRepo) {
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
}
