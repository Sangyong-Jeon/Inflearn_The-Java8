package me.whiteship.java8to11;

// 다른 형태의 메소드가 있더라도 중요하지않고 중요한건 추상 메소드가 몇개이냐이다.
// 1개면 함수 인터페이스
// 함수 인터페이스이면 java가 제공하는 @FunctionalInterface를 붙여서 만약 2개의 추상 메소드를 만들었다면 컴파일할 때 에러가 난다.
//

@FunctionalInterface
public interface RunSomething {
    int doIt(int number);
}
