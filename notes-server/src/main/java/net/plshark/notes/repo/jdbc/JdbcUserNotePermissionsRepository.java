package net.plshark.notes.repo.jdbc;

import javax.inject.Named;
import javax.inject.Singleton;

import net.plshark.jdbc.ReactiveUtils;
import net.plshark.notes.UserNotePermission;
import net.plshark.notes.repo.UserNotePermissionsRepository;
import reactor.core.publisher.Mono;

/**
 * User note permission repository that uses JDBC
 */
@Named
@Singleton
public class JdbcUserNotePermissionsRepository implements UserNotePermissionsRepository {

    private final SyncJdbcUserNotePermissionsRepository syncRepo;

    /**
     * Create a new instance
     * @param syncRepo the synchronous repository
     */
    public JdbcUserNotePermissionsRepository(SyncJdbcUserNotePermissionsRepository syncRepo) {
        this.syncRepo = syncRepo;
    }

    @Override
    public Mono<UserNotePermission> getByUserAndNote(long userId, long noteId) {
        return ReactiveUtils.wrapWithMono(() -> syncRepo.getByUserAndNote(userId, noteId).orElse(null));
    }

    @Override
    public Mono<UserNotePermission> insert(UserNotePermission permission) {
        return ReactiveUtils.wrapWithMono(() -> syncRepo.insert(permission));
    }

    @Override
    public Mono<Void> deleteByUserAndNote(long userId, long noteId) {
        return ReactiveUtils.wrapWithMono(() -> {
            syncRepo.deleteByUserAndNote(userId, noteId);
            return null;
        });
    }

    @Override
    public Mono<Void> deleteByNote(long noteId) {
        return ReactiveUtils.wrapWithMono(() -> {
            syncRepo.deleteByNote(noteId);
            return null;
        });
    }

    @Override
    public Mono<UserNotePermission> update(UserNotePermission permission) {
        return ReactiveUtils.wrapWithMono(() -> syncRepo.update(permission));
    }
}
