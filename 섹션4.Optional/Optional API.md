# Optional API

- Optional 만들기
  - Optional.of()
  - Optional.ofNullable()
  - Optional.empty()
- Optional에 값이 있는지 없는지 확인하기
  - isPresent()
  - isEmpty() : Java 11부터 제공

```java
public class App {
  public static void main(String[] args) {
    List<OnlineClass> springClasses = new ArrayList<>();
    springClasses.add(new OnlineClass(1, "spring boot", true));
    springClasses.add(new OnlineClass(5, "rest api development", false));
    
    Optional<OnlineClass> optional = springClasses.stream()
          .filter(oc -> oc.getTitle().startsWith("spring"))
          .findFirst(); // filter 조건에 일치하는 가장 앞에 있는 요소 1개를 Optional로 리턴
    
    // Optional 안에 값이 있는가?
    boolean present = optional.isPresent();
    System.out.println(present); // true
    
    // Optional 안에 값이 없는가?
    boolean empty = optional.isEmpty();
    System.out.println(empty); // false
    }
}
```
- Optional에 있는 값 가져오기
  - get()
  - 만약에 비어있는 Optional에서 꺼낸다면?
    -> RuntimeException을 상속받은 `NoSuchElementException` 발생
  - 따라서 get()을 사용할 때는 있는지 없는지 먼저 확인해야 함
```java
List<OnlineClass> springClasses = new ArrayList<>();
springClasses.add(new OnlineClass(1, "spring boot", true));
springClasses.add(new OnlineClass(5, "rest api development", false));

Optional<OnlineClass> optional = springClasses.stream()
    .filter(oc -> oc.getTitle().startsWith("jpa"))
    .findFirst();
                
// OnlineClass onlineClass = optional.get(); // ERROR!
// System.out.println(onlineClass);
if(optional.isPresent()) {
  OnlineClass onlineClass = optional.get();
  System.out.println(onlineClass.getTitle());
}
```
  
- Optional에 값이 있는 경우에 그 값을 가지고 ~~를 하라
  - ifPresent(Consumer)
  - 값이 있는지 확인하는 방법은 위 if문 보다는 관련 메소드가 있으니 가급적 그 메소드를 사용함(get()은 가급적X)
  - 예) spring으로 시작하는 수업이 있으면 id를 출력하라
```java
Optional<OnlineClass> optional = springClasses.stream()
    .filter(oc -> oc.getTitle().startsWith("spring"))
    .findFirst();
optional.ifPresent(oc -> System.out.println(oc.getId());
```
- Optional에 값이 있으면 가져오고 없는 경우에 ~~를 리턴하라
  - orElse(T)
  - 인스턴스가 들어감
  - 이미 상수로 만들어져있는 인스턴스를 작업할 때는 이것이 적합
  - 해당 Optional안에 값이 있든 없든 파라미터에 정의한 행위는 실행됨
    - 이것이 싫다면 orElseGet(Supplier)
  - 예) jpa로 시작하는 수업이 없다면 새로 만들어서 리턴하라
```java
public class App {
  public static void main(String[] args) {
    List<OnlineClass> springClasses = new ArrayList<>();
    springClasses.add(new OnlineClass(1, "spring boot", true));
    springClasses.add(new OnlineClass(5, "rest api development", false));
    
    Optional<OnlineClass> optional = springClasses.stream()
          .filter(oc -> oc.getTitle().startsWith("jpa"))
          .findFirst();
    
    OnlineClass onlineClass = optional.orElse(createNewClass());
    System.out.println(onlineClass.getTitle()); // jpa class
    }

    private static OnlineClass createNewClass() {
      return new OnlineClass(10, "jpa class", false);
    }
}
```
- Optional에 값이 있으면 가져오고 없는 경우에 ~~를 하라
  - orElseGet(Supplier)
  - orElse(T)와 달리 람다가 들어가기 때문에 Lazy하게 다룰 수 있음
  - 동적으로 작업해서 만들어야 할 때는 이것이 적합
  - Optional 안에 값이 있다면 실행 X
    - Optional 안에 null일 경우에만 함수가 실행되면서 인스턴스화가 된다는 뜻
  - 예) spring으로 시작하는 수업이 없다면 새로 만들어서 리턴하라
```java
public class App {
  public static void main(String[] args) {
    List<OnlineClass> springClasses = new ArrayList<>();
    springClasses.add(new OnlineClass(1, "spring boot", true));
    springClasses.add(new OnlineClass(5, "rest api development", false));
    
    Optional<OnlineClass> optional = springClasses.stream()
          .filter(oc -> oc.getTitle().startsWith("spring"))
          .findFirst();
    
    OnlineClass onlineClass = optional.orElseGet(App::createNewClass);
    System.out.println(onlineClass.getTitle());
    }

    private static OnlineClass createNewClass() {
      System.out.println("creating new online class"); // orElse()였으면 실행됨.
      return new OnlineClass(10, "jpa class", false);
    }
}
```
- Optional에 값이 있으면 가져오고 없는 경우에 에러를 던져라
  - orElseThrow()
  - 기본적으로 NoSuchElementException을 던짐
  - 원하는 에러가 있다면 orElseThrow(Supplier)로 제공하면 됨
```java
OnlineClass onlineClass = optional.orElseThrow(IllegalStateException::new);
```
- Optional에 들어있는 값 걸러내기
  - Optional.filter(Predicate)
  - filter 조건에 충족하지 않으면 Optional.empty() 반환
  - filter 조건에 충족하는 값을 가져옴
```java
Optional<OnlineClass> onlineClass = optional.filter(OnlineClass::isClosed); // 닫혀있는지 확인하는 메소드
System.out.println(onlineClass.isPresent());
```
- Optional에 들어있는 값 변환하기
  - Optional.map(Function)
  - Optional.flatMap(Function) : Optional 안에 들어있는 인스턴스가 Optional인 경우에 사용하면 편리함
  - Function에서 처리할 때 리턴하는 타입에 따라 Optional에 담기는 값 타입이 달라짐
```java
Optional<OnlineClass> optional = springClasses.stream()
    .filter(oc -> oc.getTitle().startsWith("spring"))
    .findFirst();
Optional<Integer> interger = optional.map(Onlineclass::getId); // 해당 클래스의 ID를 가져오므로 타입이 Integer로 변환
```

<br>

# 🙋‍♂️ 추가적으로 궁금한 부분

### flatMap() 사용 예시
OnlineClass 클래스에는 getProgress()를 통해 `Optional<Progress>`를 반환받는다.
```java
Optional<Progress> progress = optional.flatMap(OnlineClass::getProgress);
// flatMap을 쓰지않는다면 아래 과정을 거치게 됨
Optional<Optional<Progress>> progress1 = optional.map(OnlineClass::getProgress);
Optional<Progress> progress2 = progress1.orElse(Optional.empty());
```

- Stream이 제공하는 flatMap과 다르다
  - Stream에서는 input이 하나, output은 여러개일 수 있음
- Optional에서 flatMap은 input이 하나, output도 하나
  - Optional 자체가 어떤 한 값만 가질 수 있는 것이기 때문

<br>

### Optional에서 of는 언제 사용할까?
null이 들어가면 예외가 발생하니 값이 확실히 들어있는 객체가 맞다면 사용

<br>

### OptionalInt는 언제 사용할까?
int 타입의 Stream 결과를 받는 타입으로 사용한다.
결과에 해당하는 int가 있을수도 없을수도 있기 때문
