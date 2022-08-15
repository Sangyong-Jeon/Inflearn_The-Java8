package me.whiteship.java8to11;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("myomyo");
        names.add("eee");
        names.add("aaa");
        names.add("ddd");

        // 일반 for문은 병렬로 처리하기 힘듬
        for (String name : names) {
            if (name.startsWith("m")) {
                System.out.println(name.toUpperCase());
            }
        }

        // 스트림은 JVM이 알아서 병렬적으로 처리해줌
        // 데이터가 정말 방대하게 큰 경우에는 병렬처리가 좋지만 일반적인 경우 그냥 스트림 쓰면 된다.
        names.parallelStream()
                .filter(s -> s.startsWith("m"))
                .forEach(s -> System.out.println(s.toUpperCase()));


    }
}