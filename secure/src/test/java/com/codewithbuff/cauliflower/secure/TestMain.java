package com.codewithbuff.cauliflower.secure;

import reactor.core.publisher.Mono;

public class TestMain {

    public static void main(String[] args) {
        Mono.just("aaa")
                .map(p1 -> {
                    System.out.println(Thread.currentThread().getName());
                    return "bbb";
                })
                .map(p2 -> {
                    System.out.println(Thread.currentThread().getName());
                    return "ccc";
                })
                .flatMap(p3 -> {
                    System.out.println(Thread.currentThread().getName());
                    return Mono.just("ddd");
                })
                .flatMap(p4 -> {
                    System.out.println(Thread.currentThread().getName());
                    return Mono.just("eee");
                })
                .subscribe(System.out::println);
    }
}
