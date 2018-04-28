package net.plshark.notes.repo.jdbc;

import java.util.Objects;
import java.util.concurrent.Callable;

import javax.inject.Named;
import javax.inject.Singleton;

import net.plshark.notes.Note;
import net.plshark.notes.repo.NotesRepository;
import net.plshark.notes.repo.SyncNotesRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Repository that saves notes in and retrieves notes from a DB using JDBC
 */
@Named
@Singleton
public class AsyncJdbcNotesRepository implements NotesRepository {

    private final SyncNotesRepository syncRepo;

    /**
     * Create a new instance
     * @param syncRepo the synchronous repository to wrap
     */
    public AsyncJdbcNotesRepository(SyncNotesRepository syncRepo) {
        this.syncRepo = Objects.requireNonNull(syncRepo);
    }

    private <T> Mono<T> wrapWithMono(Callable<T> callable) {
        Mono<T> blockingWrapper = Mono.fromCallable(callable);
        return blockingWrapper.subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<Note> get(long id) {
        return wrapWithMono(() -> syncRepo.get(id).orElse(null));
    }

    @Override
    public Mono<Note> insert(Note note) {
        return wrapWithMono(() -> syncRepo.insert(note));
    }

    @Override
    public Mono<Note> update(Note note) {
        return wrapWithMono(() -> syncRepo.update(note));
    }

    @Override
    public Mono<Void> delete(long id) {
        return wrapWithMono(() -> {
            syncRepo.delete(id);
            return null;
        });
    }
}
