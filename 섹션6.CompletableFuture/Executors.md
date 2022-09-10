# Executors

- 고수준 (High-Level) Concurrency 프로그래밍
  - 쓰레드를 만들고 관리하는 작업을 애플리케이션에서 분리
  - 그런 기능을 Executors에게 위임
- Executors가 하는 일
  - 쓰레드 만들기 : 애플리케이션이 사용할 쓰레드 풀을 만들어 관리
  - 쓰레드 관리 : 쓰레드 생명 주기를 관리
  - 작업 처리 및 실행 : 쓰레드로 실행할 작업을 제공할 수 있는 API를 제공
- 주요 인터페이스
  - Executor: execute(Runnable)
  - ExecutorService: Executor 상속 받은 인터페이스로, Callable도 실행할 수 있으며, Executor를 종료 시키거나, 여러 Callable을 동시에 실행하는 등의 기능을 제공함
  - ScheduledExecutorService: ExecutorService를 상속 받은 인터페이스로 특정 시간 이후에 또는 주기적으로 작업을 실행할 수 있음
- Fork/Join 프레임워크
  - ExecutorService의 구현체로 손쉽게 멀티 프로세서를 활용할 수 있게끔 도와줌

<br>

ExecutorService로 작업 실행하기
```java
ExecutorService executorService = Executors.newSingleThreadExecutor();
executorService.submit(() -> {
    System.out.println("Hello :" + Thread.currentThread().getName());
});
```

<br>

ExecutorService로 멈추기
```java
// 명시적인 종료를 안한다면 애플리케이션이 계속 실행되므로 종료시키기
executorService.shutdown(); // 처리중인 작업 기다렸다가 종료
executorService.shutdownNow(); // 당장 종료
```

<br>

예) 2개의 쓰레드로 5개의 작업을 처리하기<br>
쓰레드풀을 사용하는 이유 -> 쓰레드 생성 비용을 줄임
```java
public class App {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2); // 쓰레드풀에 쓰레드 2개 생성
        executorService.submit(getRunnable("Hello")); // 쓰레드 1 또는 2가 작업을 수행함 (쓰레드가 번갈아가며 수행하는건 아님)
        executorService.submit(getRunnable("myomyo"));
        executorService.submit(getRunnable("The"));
        executorService.submit(getRunnable("Java"));
        executorService.submit(getRunnable("Thread"));

        executorService.shutdown(); // 처리중인 작업 기다렸다가 종료
    }

    private static Runnable getRunnable(String message) {
        return () -> System.out.println(message + Thread.currentThread().getName());
    }
}
/*
Hellopool-1-thread-1
myomyopool-1-thread-2
Thepool-1-thread-2
Javapool-1-thread-1
Threadpool-1-thread-2
*/
```
submit은 execute와 동일하게 일처리를 시키지만, 추가적으로 결과(future)도 보고(리턴)받음

<br>

예) 작업을 1초 후 실행하고 그 뒤로는 2초 간격으로 작업 반복(스프링에서는 @Scheduled 로 사용가능)
```java
public class App {
    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(); // 하나의 쓰레드만 사용하는 ScheduledExecutorService 생성
        executorService.scheduleAtFixedRate(getRunnable("Hello"), 1, 2, TimeUnit.SECONDS); // 1초 후 작업 실행하고, 그 뒤로는 2초 간격으로 반복함
    }

    private static Runnable getRunnable(String message) {
        return () -> System.out.println(message + Thread.currentThread().getName());
    }
}
```
schedule() : 지정한 딜레이(delay) 후에 작업 1회 실행<br>
scheduleAtFixedRate() : 시작딜레이(initialDelay) 이후 첫번째 실행을 시작으로 지정한 시간(period)만큼 차이로 정확하게 반복 실행함

---

쓰레드의 문제점
- 운영체제에 직접 접근
- 생성비용이 비쌈
- 운영체제 쓰레드의 숫자는 제한적이므로, 숫자를 넘기면 자바 애플리케이션이 예상치 못한 방식으로 종료될 수 있음

따라서 이런 단점 때문에 쓰레드 풀(Thread Pool)을 생성하여 사용한다.
- 쓰레드를 제한된 개수만큼 생성해놓고 작업 큐(Queue)에 들어오는 작업을 하나씩 쓰레드가 처리 (쓰레드 생성 방지)
- 작업이 많이 증가하여도 쓰레드의 전체 개수가 늘어나지 않아 애플리케이션 성능이 급격하게 저하되지 않음 (프로그램 성능 저하 방지)

쓰레드 풀의 장점
- 쓰레드 생성/수거 비용 X
- 쓰레드 생성될 때 OS가 메모리 공간을 확보해주고 메모리를 쓰레드게에 할당해줌
- 쓰레드 풀을 미리 만들어 두기 때문에 처음에 생성 비용은 들지만, 쓰레드를 재사용할 수 있으므로 시스템 자원을 줄이고, 작업 요청 시 쓰레드가 대기중이라 실행하는데 딜레이가 발생 X

쓰레드 풀의 단점
- 쓰레드를 너무 많이 생성해놓고 쓰지않으면 메모리 낭비 발생
