package net.plshark.notes.repo.jdbc;

import java.util.concurrent.Callable;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ReactiveUtils {

    public static <T> Mono<T> wrapWithMono(Callable<T> callable) {
        Mono<T> blockingWrapper = Mono.fromCallable(callable);
        return blockingWrapper.subscribeOn(Schedulers.elastic());
    }
}
