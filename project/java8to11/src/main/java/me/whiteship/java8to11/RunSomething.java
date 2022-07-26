package me.whiteship.java8to11;

/*
함수형 인터페이스 정의
- 추상 메소드를 1개만 가지고 있는 인터페이스
- SAM(Single Abstract Method) 인터페이스
- @FunctionalInterface 어노테이션을 가진 인터페이스
    이 어노테이션을 붙이면 컴파일러가 함수형 인터페이스를 올바르게 정의했는지 확인해줌
- 순수함수, 고차함수, 익명함수 조건을 만족해야 함

람다식
- 함수형 인터페이스의 인스턴스를 만드는 방법으로 쓰일 수 있음
- 코드를 줄임
- 메소드 매개변수, 리턴 타입, 변수로 만들어서 사용 가능
- 람다식은 하나의 식(expression)을 표현한 것
- 메소드를 람다식으로 표현하면 메소드 이름과 반환값이 없어지므로 람다식을 익명 함수라고도 함
- 람다식은 익명 클래스의 객체와 동등

순수 함수 정의
- 사이드 이펙트가 없음(함수 밖에 있는 값 변경 x)
- 상태가 없음(함수 밖에 잇는 값 사용 x)

고차 함수 정의
- 함수가 함수를 매개변수로 받을 수 있고, 함수를 리턴할 수도 있음

1급 객체 정의
- 변수에 할당할 수 있음
- 객체의 인자로 넘길 수 있음
- 객체의 리턴값으로 리턴 할 수 있음
 */

@FunctionalInterface
public interface RunSomething {
    int doIt(int number);
}
