package me.whiteship.java8to11;


import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        int size = 1500;
        int[] numbers = new int[size];
        Random random = new Random();

        // 1500개를 랜덤한 값으로 채움
        IntStream.range(0, size).forEach(i -> numbers[i] = random.nextInt());
        long start = System.nanoTime();
        Arrays.sort(numbers); // 항상 싱글쓰레드 사용하는 퀵소트 사용함.
        System.out.println("serial sorting took " + (System.nanoTime() - start));

        IntStream.range(0, size).forEach(i -> numbers[i] = random.nextInt());
        start = System.nanoTime();
        Arrays.parallelSort(numbers); // 필요에 따라 여러 개의 쓰레드로 작업
        System.out.println("parallel sorting took " + (System.nanoTime() - start));
    }
}