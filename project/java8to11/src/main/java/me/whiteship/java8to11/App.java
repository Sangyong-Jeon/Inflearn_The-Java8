package me.whiteship.java8to11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Callable<String> hello = () -> {
            Thread.sleep(2000L);
            return "Hello";
        };

        Callable<String> java = () -> {
            Thread.sleep(3000L);
            return "Java";
        };

        Callable<String> myomyo = () -> {
            Thread.sleep(1000L);
            return "Myomyo";
        };

        long l = System.currentTimeMillis();
        String s = executorService.invokeAny(Arrays.asList(hello, java, myomyo));
        System.out.println(s);
        long l2 = System.currentTimeMillis();
        System.out.println(l2 - l);
        executorService.shutdown();
    }
}