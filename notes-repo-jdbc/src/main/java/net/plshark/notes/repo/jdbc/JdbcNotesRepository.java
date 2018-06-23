package net.plshark.notes.repo.jdbc;

import java.util.Objects;
import javax.inject.Named;
import javax.inject.Singleton;

import net.plshark.jdbc.ReactiveUtils;
import net.plshark.notes.Note;
import net.plshark.notes.repo.NotesRepository;
import reactor.core.publisher.Mono;

/**
 * Repository that saves notes in and retrieves notes from a DB using JDBC
 */
@Named
@Singleton
public class JdbcNotesRepository implements NotesRepository {

    private final SyncJdbcNotesRepository syncRepo;

    /**
     * Create a new instance
     * @param syncRepo the synchronous repository to wrap
     */
    public JdbcNotesRepository(SyncJdbcNotesRepository syncRepo) {
        this.syncRepo = Objects.requireNonNull(syncRepo);
    }

    @Override
    public Mono<Note> get(long id) {
        return ReactiveUtils.wrapWithMono(() -> syncRepo.get(id).orElse(null));
    }

    @Override
    public Mono<Note> insert(Note note) {
        return ReactiveUtils.wrapWithMono(() -> syncRepo.insert(note));
    }

    @Override
    public Mono<Note> update(Note note) {
        return ReactiveUtils.wrapWithMono(() -> syncRepo.update(note));
    }

    @Override
    public Mono<Void> delete(long id) {
        return ReactiveUtils.wrapWithMono(() -> {
            syncRepo.delete(id);
            return null;
        });
    }
}
