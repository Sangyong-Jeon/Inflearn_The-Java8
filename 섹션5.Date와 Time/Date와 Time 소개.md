# Date와 Time 소개

- `Date`는 날짜 + 시간을 다룰 목적으로 JDK1.0부터 제공됨.
- `Calendar`는 `Date` 클래스의 기능이 부족했기에 나온 클래스로 JDK1.1부터 제공됨.
- 위 클래스들의 단점을 개선한 새로운 클래스들이 추가된 `java.time`패키지가 JDK1.8부터 제공됨.
> `java.time`패키지만 배우면 좋겠지만 `Date`와 `Calendar`는 20년 넘게 써왔기에 어느 정도는 알아야 한다.<br>
> jdk1.0은 1996년, jdk1.8은 2014년에 발표됨.

<br>

### 🧐 Java 8에서 새로운 날짜와 시간 API가 생긴 이유?
- `java.util.Date` 클래스는 mutable 하기 때문에 thread-safe 하지 않음
  - mutable(가변) : 값 변경 가능
  - immutable(불변) : 값 변경 불가능
  - thread-safe : 멀티 스레드 환경에서 여러 스레드로부터 동시 접근이 되어도 정확하게 동작함(문제가 없음)
- 클래스명이 Date인데 시간까지 다룸
- 버그 발생할 여지가 많음
  - 타입 안정성(type-safety) X
    - 아래 GregorianCalendar 생성자에서 int 타입이니까 마이너스값도 들어올 수 있어서 타입 안정성 X
    - Enum 타입으로 변경하면 해당 타입만 받을 수 있으므로 타입 안정성 O
  ```java
  public class GregorianCalendar extends Calendar {
    public GregorianCalendar(int year, int month, int dayOfMonth) {
      this(year, month, dayOfMonth, 0, 0, 0, 0);
    }
  }
  ```
  - 월이 0부터 시작
  ```java
  Calendar myomyoBirthDay = new GregorianCalendar(1997, 1, 1); // 두번째 파라미터를 조심
  Calendar myomyoBirthDay = new GregorianCalendar(1997, Calendar.JANUARY, 1); // 0부터 시작이기에 미리 정해진 상수로 대체
  ...
  public abstract class Calendar implements Serializable, Cloneable, Comparable<Calendar> {
    public static final int JANUARY = 0;
    ...
  }
  ```
- 날짜 시간 처리가 복잡한 애플리케이션에서는 보통 [Joda Time](https://www.joda.org/joda-time/)을 썼음
  - Joda Time : JDK1.8 이전에 `Date`와 `Calendar`클래스가 불편해서 만든 오픈소스 라이브러리

> EPHOCH 시간은 1970-01-01 00:00:00 UTC 를 의미함


```java
public class App {
  public static void main(String[] args) throws InterruptedException {
    Date date = new Date(); // Date인데 시간도 나타내고, 타임스탬프까지 표현 가능함
    long time = date.getTiem(); // 날짜에서 시간을 가져오는게 이상함
    System.out.println(time); // 가져온 시간도 시,분,초 단위가 아니라 ephoch 기준으로 경과한 밀리초를 가져옴
    
    Thread.sleep(1000 * 3); // 3초 딜레이
    Date after3Seconds = new Date();
    System.out.println(after3Seconds); // 3초후 시간
    after3Seconds.setTime(time); // mutable해서 값 변경함
    System.out.println(after3Seconds); // 원래 시간(3초 딜레이 전), 스레드 안전X
  }
}
```

<br>

### Java 8에서 제공하는 Date-Time API
- 기계용 시간(machine time)과 인류용 시간(human time)으로 나눌 수 있음
- 기계용 시간은 EPOCK부터 현재까지의 타임스탬프를 표현
- 인류용 시간은 연, 월, 일, 시, 분, 초 등을 표현
- 타임스탬프는 Instant를 사용
- 특정 날짜(LocalDate), 시간(LocalTime), 일시(LocalDateTime) 사용
- 기간을 표현할 때는 Duration(시간 기반)과 Period(날짜 기반) 사용
- DateTimeFormatter를 사용해서 일시를 특정한 문자열로 포매팅 가능
