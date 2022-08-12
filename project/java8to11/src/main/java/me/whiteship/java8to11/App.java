package me.whiteship.java8to11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;

public class App {
    public static void main(String[] args) {
        List<String> name = new ArrayList<>();
        name.add("myomyo");
        name.add("eee");
        name.add("aaa");
        name.add("ddd");

        Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
        name.sort(compareToIgnoreCase.reversed()); // 추가적으로 또 조건을 걸어 정렬할때

        name.forEach(System.out::println);

    }
}