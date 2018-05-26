package net.plshark.jdbc;

import java.util.List;
import java.util.concurrent.Callable;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ReactiveUtils {

    public static <T> Mono<T> wrapWithMono(Callable<T> callable) {
        Mono<T> blockingWrapper = Mono.fromCallable(callable);
        return blockingWrapper.subscribeOn(Schedulers.elastic());
    }

    public static <T> Flux<T> wrapWithFlux(Callable<List<T>> callable) {
        Mono<List<T>> mono = wrapWithMono(callable);
        return mono.flatMapMany(Flux::fromIterable);
    }
}
