package com.codewithbuff.cauliflower.secure;

import reactor.core.publisher.Mono;

public class TestMain {

    public static void main(String[] args) {
        Mono.error(new Throwable())
                .flatMap(p -> {
                    return Mono.just("bbb");
                })
                .onErrorResume(throwable -> {
                    return Mono.just("aaa");
                })
                .subscribe(System.out::println);
    }
}
