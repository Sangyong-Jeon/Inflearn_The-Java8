package me.whiteship.java8to11;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(2, "spring data jpa", true));
        springClasses.add(new OnlineClass(3, "spring mvc", false));
        springClasses.add(new OnlineClass(4, "spring core", false));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        List<OnlineClass> javaClasses = new ArrayList<>();
        javaClasses.add(new OnlineClass(6, "The Java, Test", true));
        javaClasses.add(new OnlineClass(7, "The Java, Code manipulation", true));
        javaClasses.add(new OnlineClass(8, "The Java, 8 to 11", false));

        List<List<OnlineClass>> myomyoEvents = new ArrayList<>();
        myomyoEvents.add(springClasses);
        myomyoEvents.add(javaClasses);

        System.out.println("spring 으로 시작하는 수업");
        springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("spring"))
                .forEach(oc -> System.out.println(oc.getId()));

        System.out.println("close 되지 않은 수업");
        springClasses.stream()
                .filter(Predicate.not(OnlineClass::isClosed)) // 메소드 레퍼런스로는 !OnlineClass:isClosed를 표현 못하므로 Predicated의 not()을 이용함.
                .forEach(oc -> System.out.println(oc.getId()));

        System.out.println("수업 이름만 모아서 스트림 만들기");
        springClasses.stream()
                .map(OnlineClass::getTitle)
                .forEach(System.out::println);


        System.out.println("두 수업 목록에 들어있는 모든 수업 아이디 출력");
        myomyoEvents.stream() // [[List<oc>][List<oc>]]
//                .flatMap(list -> list.stream()) // [oc, oc, ...]
                .flatMap(Collection::stream)
                .forEach(oc -> System.out.println(oc.getId()));


        System.out.println("10부터 1씩 증가하는 무제한 스트림 중에서 앞에 10개 빼고 최대 10개 까지만");
        Stream.iterate(10, i -> i + 1) // 10부터 1씩 증가하는 무제한 스트림
                .skip(10) // 처음 10개 스팁
                .limit(10) // 최대 10개까지만 가져오기
                .forEach(System.out::println);

        System.out.println("자바 수업 중에 Test가 들어있는 수업이 있는지 확인");
        boolean test = javaClasses.stream().anyMatch(oc -> oc.getTitle().contains("Test"));
        System.out.println(test);
        // filter는 스트림에 있는 모든 인스턴스를 한번씩 다 연산하지만, anyMatch는 대응하는 것 중에 아무거나 하나만 찾으면 되는거라
        // 스트림에 들어있는 인스턴스 중에서 처음으로 매치되는걸 찾는다면 그 다음 객체들에 대응해볼 필요가 없어서 성능적으로 조금 더 장점이 있음

        System.out.println("스프링 수업 중에 제목에 spring이 들어간 것만 제목만 모아서 List로 만들기");
        List<String> spring = springClasses.stream()
                .filter(oc -> oc.getTitle().contains("spring"))
                .map(OnlineClass::getTitle)
                .collect(Collectors.toList());

        // filter와 map의 순서를 바꾸면 스트림 요소 타입도 달라진다.
        List<String> spring2 = springClasses.stream()
                .map(OnlineClass::getTitle)
                .filter(title -> title.contains("spring"))
                .collect(Collectors.toList());
        spring2.forEach(System.out::println);


    }
}