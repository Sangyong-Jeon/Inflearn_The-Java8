# Stream API

- 걸러내기
  - filter(predicate)
  - 예) 이름이 3글자 이상이 데이터마 새로운 스트림으로
- 변경하기
  - map(function) 또는 flatMap(function)
  - 예) 각각의 Post 인스턴스에서 String title만 새로우 스트림으로
  - 예) List<Stream<String>>을 String의 스트림으로
- 생성하기
  - generate(supplier) 또는 iterate(T seed, UnaryOperator)
  - 예) 10부터 1씩 증가하는 무제한 숫자 스트림
  - 예) 랜덤 int 무제한 스트림
- 제한하기
  - limit(long) 또는 skip(long)
  - 예) 최대 5개의 요소가 담긴 스트림을 리턴함
  - 예) 앞에서 3개를 빼 나머지 스트림을 리턴함
- 스트림에 있는 데이터가 특정 조건으 만족하는지 확인
  - anyMatch(), allMatch(), nonMatch()
  - 예) k로 시작하는 문자열이 있는지 확인한다. (true 또느 false 리턴)
  - 예) 스트림에 있는 모든 값이 10보다 작은지 확인함
- 개수 세기
  - count()
  - 예) 10보다 큰 수으 개수르 센다
- 스트림으 데이터 하날 뭉치기
  - reduce(identity, BiFucntion), collect(), sum(), max()
  - 예) 모든 숫자 합 구하기
  - 예) 모든 데이터르 하나의 List 또는 Set에 옮겨 담기
  
<br>
  
### 예시
  
<br>
  
OnlineClass 클래스
```java
public class OnlineClass {

    private Integer id;
    private String title;
    private boolean closed;

    public OnlineClass(Integer id, String title, boolean closed) {
        this.id = id;
        this.title = title;
        this.closed = closed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
```

<br>
  
```java
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

        System.out.println("spring으로 시작하는 수업");
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
                .skip(10) // 처음 10개를 건너뜀
                .limit(10) // 최대 10개까지만 가져오기
                .forEach(System.out::println);

        System.out.println("자바 수업 중에 Test가 들어있는 수업이 있는지 확인");
        boolean test = javaClasses.stream().anyMatch(oc -> oc.getTitle().contains("Test"));
        System.out.println(test);
        // filter는 스트림에 있는 모든 인스턴스를 한번씩 다 연산하지만, anyMatch는 대응하는 것 중에 아무거나 하나만 찾으면 되는것.
        // 스트림에 들어있는 인스턴스 중에서 처음으로 매치되는걸 찾는다면 그 다음 객체들에 대응해볼 필요가 없어서 성능적으로 장점이 있음

        System.out.println("스프링 수업 중에 제목에 spring이 들어간 것만 제목만 모아서 List로 만들기");
        List<String> spring = springClasses.stream()
                .filter(oc -> oc.getTitle().contains("spring")) // OnlineClass타입
                .map(OnlineClass::getTitle)
                .collect(Collectors.toList());

        // 위 코드에서 filter와 map의 순서를 바꾸면 스트림 요소 타입도 달라진다.
        List<String> spring2 = springClasses.stream()
                .map(OnlineClass::getTitle)
                .filter(title -> title.contains("spring")) // String타입
                .collect(Collectors.toList());
        spring2.forEach(System.out::println);
    }
}
```
