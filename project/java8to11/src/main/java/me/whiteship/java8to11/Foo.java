package me.whiteship.java8to11;

import java.util.function.*;

public class Foo {
    public static void main(String[] args) {
        // 람다식 예시
        RunSomething runSomething = number -> number + 10;

        // 클래스를 이용하여 만든 Function<T,R> 함수형 인터페이스
        Plus10 plus10 = new Plus10();
        System.out.println(plus10.apply(1)); // 11

        // 람다식으로 이용한 Function<T,R> 함수형 인터페이스
        Function<Integer, Integer> plus11 = number -> number + 11;
        System.out.println(plus11.apply(1)); // 12

        // 함수 조합용 메소드
        Function<Integer, Integer> multiply2 = number -> number * 2;
        Function<Integer, Integer> multiply2AndPlus10 = plus10.compose(multiply2);
        System.out.println(multiply2AndPlus10.apply(2)); // 14
        System.out.println(plus10.andThen(multiply2).apply(2)); // 24

        // 매개변수 2개인 BiFunction<T,U,R> 함수형 인터페이스
        BiFunction<Integer, Integer, Integer> timesTable = (first, last) -> first * last;
        System.out.println(timesTable.apply(2, 9)); // 18

        // 매개변수만 있고 반환값 없는 Consumer<T> 함수형 인터페이스
        Consumer<Integer> printT = number -> System.out.println(number);
        printT.accept(10); // 10

        // 매개변수는 없고 반환값만 있는 Supplier<T> 함수형 인터페이스
        Supplier<Integer> get10 = () -> 10;
        System.out.println(get10.get()); // 10

        // 매개변수는 하나이고 boolean을 반환하는 Predicate<T> 함수형 인터페이스
        Predicate<String> startsWithMyomyo = s -> s.startsWith("myomyo");
        Predicate<Integer> isEven = i -> i % 2 == 0;
        Predicate<Integer> isOdd = i -> i % 2 == 1;

        System.out.println(startsWithMyomyo.test("myomyoJava")); // true
        System.out.println(isEven.test(1)); // false
        System.out.println(isEven.and(isOdd).test(2)); // false
        System.out.println(isEven.or(isOdd).test(2)); // true
        System.out.println(isEven.negate().test(2)); // false

        // Function의 자손으로, Function과 달리 매개변수와 결과의 타입이 같은 UnaryOperator<T>
        UnaryOperator<Integer> plus12 = i -> i + 12;
        UnaryOperator<Integer> multiply3 = i -> i * 3;
        System.out.println(plus12.andThen(multiply3).apply(2)); // 42

        // BiFunction의 자손으로, 매개변수와 결과의 타입이 같은 BinaryOperator<T>
        BinaryOperator<Integer> plus10Andplus = (i, j) -> i + j + 10;
        System.out.println(plus10Andplus.apply(2, 3)); // 15
    }
}
