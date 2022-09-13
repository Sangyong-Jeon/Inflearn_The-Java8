package me.whiteship.java8to11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("myomyo " + Thread.currentThread().getName());
            return "myomyo";
        }, executorService).thenRunAsync(() -> {
            System.out.println(Thread.currentThread().getName());
        }, executorService);
        future.get();
        executorService.shutdown();
    }
}