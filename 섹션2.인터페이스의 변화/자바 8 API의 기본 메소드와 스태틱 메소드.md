# 자바 8 API의 기본 메소드와 스태틱 메소드

자바 8에서 추가한 기본 메소드로 인한 API 변화를 살펴보자.

Iterable을 상속받은 Collection 인터페이스라서 둘 다 같다고 보면 되고, <br>
그 타입들의 메소드를 사용하기 보다는 좀 더 낮은 수준인 List, Map, Set으로 자주 사용된다.

<br>

### 📚 Iterable의 기본 메소드

- forEach()

  : 파라미터로는 매개변수는 있고 반환값이 없는 `Consumer`를 받는다.<br>
  따라서 이 메소드도 반환값이 없으며 손쉽게 엘리먼트를 순회할 수 있음
  
  ```java
  List<String> name = new ArrayList<>();
  name.add("myomyo");
  name.add("eee");
  name.add("aaa");
  name.add("ddd");
  
  name.forEach(System.out::println));
  ```
  
- spliterator()

  : 쪼갤수 있는 기능이 있는 Iterator이다. 병렬적으로 처리가 가능하며 이터레이터이기에 순회도 가능하다.
  
  ```java
  Spliterator<String> spliterator = name.spliterator();
  while (spliterator.tryAdvance(System.out::println)); // 다음값 없으면 false
  /*
  myomyo
  eee
  aaa
  ddd
  */
  ```
  
  여기서 이터레이터를 쪼개는 `trySplit()`을 쓰면 분리가 된다.
  ```java
  Spliterator<String> spliterator = name.spliterator();
  Spliterator<String> spliterator1 = spliterator.trySplit(); // 반틈 쪼갠다
  while (spliterator.tryAdvance(System.out::println));
  System.out.println("=====");
  while (spliterator1.tryAdvance(System.out::println));
  /*
  aaa
  ddd
  =====
  myomyo
  eee
  */
  ```
  
<br>

### 📚 Collection의 기본 메소드

- stream() / parallelStream()

  : 모든 컬렉션의 하위 인터페이스들은 `stream`을 가진다. 이것을 보면 `spliterator`를 사용하고 있다.<br>
  또한 엘리먼트들을 스트림으로 만들어서 메소드처럼 처리할 수 있다.
  
  ```java
  long m = name.stream().map(String::toUpperCase) // 전부 대문자로 변경
          .filter(s -> s.startsWith("M")) // "M"이 있는것만 걸러내기
          .count(); // 몇개 있는지 세기
  System.out.println(m); // 1
  ```
  
  마지막에 `count()`뿐만 아니라 `collect(Collectors.toSet())`처럼 Map이나 Set으로도 만들 수 있다.
  
- removeIf

  : 매개변수는 있고 반환값이 boolean인 `Predicate`를 매개변수로 받는다.<br>
  따라서 true인 것은 제외를 시킨다.
  
  ```java
  name.removeIf(s -> s.startWith("m")); // "m"으로 시작되는거 제외
  name.forEach(System.out::println);
  /*
  eee
  aaa
  ddd
  */
  ```
  
- spliterator()

<br>

### 📚 Comparator의 기본 메소드 및 스태틱 메소드

정렬에 사용되는 인터페이스

- reversed()

  : 정렬을 역순으로 할 때 사용
  
  ```java
  Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
  name.sort(compareToIgnoreCase.reversed()); // 문자순으로 역순정렬
  name.forEach(System.out::println);
  /*
  myomyo
  eee
  ddd
  aaa
  */
  ```
  
- thenComparing()

  : 추가적으로 조건을 걸어 정렬하고 싶을 때

- static reverseOrder() / naturalOrder()
- static nullsFirst() / nullsLast()
- static comparing()

<br>

### 🎓 이 외에도 Java에서는 많은 기본 메소드와 스태틱 메소드를 제공하고 있음

자바 8 이전에는 
- 인터페이스에 a(), b(), c()라는 추상 메소드가 있으면
- 인터페이스를 구현하는 추상 클래스에 비어있는 구현체를 넣어 놓았음
- 왜냐면 실제로 구현하는 클래스들이 추상 클래스를  필요한 기능만 구현하기 위함(편의성 제공)

자바 8 이후로는 이런 편의성을 인터페이스가 제공한다.
- 기본 메소드 (default method)
  - a(), b(), c()를 구현 해놓음
  - 따라서 중간에 추상 클래스가 필요없고 바로 인터페이스를 implements해서 필요한 부분만 재정의하면 됨.
- 스태틱 메소드 (static method)
 
 <br>
 
결국 자바 8 이전에는 extends로 구현했지만 지금은 implements로 인터페이스를 구현하게되어서 코드는 간결해지고 상속이 자유로워 진다.
