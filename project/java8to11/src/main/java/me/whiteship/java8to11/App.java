package me.whiteship.java8to11;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class App {
    public static void main(String[] args) {
        // 스태틱 메소드 사용하는 방법
//        UnaryOperator<String> hi = (s) -> "hi " + s;
        UnaryOperator<String> hi = Greeting::hi;
        System.out.println(hi.apply("myomyo"));

        // 인스턴스 메소드 사용할 때
        Greeting greeting = new Greeting();
        UnaryOperator<String> hello = greeting::hello; // 스태틱 메소드를 참조하는 unaryoperator 생성된 것
        System.out.println(hello.apply("myomyo"));

        // 입력값이 없는 생성자
        Supplier<Greeting> newGreeting = Greeting::new; // 객체 생성이 아니라 생성시킬 수 있는 기본생성자를 참조한 것
        Greeting greeting1 = newGreeting.get(); // get을 해야지 인스턴스가 만들어짐
        System.out.println(greeting1.getName());

        // 입력값을 받는 생성자
        Function<String, Greeting> myomyoGreeting = Greeting::new; // newGreeting과 같은 메소드 레퍼런스 코드이지만 참조하는 코드는 실제로 다르다.
        Greeting greeting2 = myomyoGreeting.apply("myomyo");
        System.out.println(greeting2.getName());

        // 특정 타입의 불특정 다수의 인스턴스 메소드를 참조하는 방법
        String[] names = {"chk", "abc", "ret"};
        Arrays.sort(names, (o1, o2) -> 0);
        System.out.println(Arrays.toString(names));
    }
}