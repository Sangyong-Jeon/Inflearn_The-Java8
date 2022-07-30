package me.whiteship.java8to11;

import java.util.function.*;

public class Foo {
    public static void main(String[] args) {
        Foo foo = new Foo();
        foo.run();
    }
    int baseNumber = 1;

    private void run() {
        // final int baseNumber = 10; // 자바8부터 final 생략 가능
        int baseNumber = 10; // 로컬 변수

        // 로컬 클래스에서 로컬 변수 참조
        class LocalClass {
            int baseNumber = 30;
            void printBaseNumber() {
                System.out.println(baseNumber);
            }
        }
        LocalClass localClass = new LocalClass();
        localClass.printBaseNumber();

        // 익명 클래스에서 로컬 변수 참조
        Consumer<Integer> integerConsumer = new Consumer<Integer>() {
            int baseNumber = 20;
            @Override
            public void accept(Integer integer) {
                System.out.println(baseNumber);
            }
        };
        integerConsumer.accept(10);

        // 람다에서 로컬 변수 참조
        IntConsumer printInt = (i) -> {
            System.out.println(i + baseNumber);
        };

        printInt.accept(10);
    }
}
