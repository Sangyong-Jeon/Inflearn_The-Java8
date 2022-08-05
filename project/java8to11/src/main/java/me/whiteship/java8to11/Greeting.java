package me.whiteship.java8to11;

public class Greeting {

    private String name;

    // 기본생성자
    public Greeting() {
    }

    // 매개변수가 있는 생성자
    public Greeting(String name) {
        this.name = name;
    }

    // 인스턴스 메소드
    public String hello(String name) {
        return "hello " + name;
    }

    // 스태틱 메소드
    public static String hi(String name) {
        return "hi " + name;
    }

    public String getName() {
        return name;
    }
}
