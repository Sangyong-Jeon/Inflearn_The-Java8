# CompletableFuture 1

자바에서 비동기(Asynchronous) 프로그래밍을 가능하게하는 인터페이스
-> Future를 사용해도 어느정도 가능했지만 하기 힘든 것이 많음

- Future 단점 (Java5부터 제공)
  - Future를 외부에서 완료시킬 수 없지만, 취소하거나 `get()`에 타임아웃을 설정할 수는 있음
  - 블로킹 코드 `get()`를 사용하지 않고서는 작업이 끝났을 때 콜백을 실행할 수 없음
  - 여러 Future를 조합할 수 없음
    - e.g. Event 정보 가져온 다음 Event에 참석하는 회원 목록 가져오기
  - 예외 처리용 API를 제공하지 않음

- CompletableFuture (Java8부터 제공)
  - Future 구현
  - CompletionStage 구현
  ```java
  public class CompletableFuture<T> implements Future<T>, CompletionStage<T> { ... }
  ```
  
  - Future이지만 직접 쓰레드를 생성하지 않고 async로 작업 처리 가능
  - 여러 CompletableFuture를 병렬로 처리하거나, 병합하여 처리 가능
  - Cancel 또는 Error를 처리할 수 있는 방법 제공
- 비동기로 작업 실행
  - 리턴값이 없는 경우 : `runAsync()`

    ```java
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        System.out.println("myomyo " + Thread.currentThread().getName()); // myomyo ForkJoinPool.commonPool-worker-19
    });
    future.get();
    /*
    myomyo ForkJoinPool.commonPool-worker-19
    */
    ```
  - 리턴값이 있는 경우 : `supplyAsync()`

    ```java
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("myomyo " + Thread.currentThread().getName()); // myomyo ForkJoinPool.commonPool-worker-19
        return "myomyo";
    });
    System.out.println(future.get()); // myomyo
    /*
    myomyo ForkJoinPool.commonPool-worker-19
    myomyo
    */
    ```
  - 원하는 Executor(쓰레드풀)를 사용해서 실행 가능 (기본은 `ForkJoinPool.commonPool()`)
- 콜백 제공하기 : Future는 get() 호출하기전에 콜백 지정 불가능했지만, CompletableFuture는 가능함
  - `thenApply(Function)` : 리턴값을 받아서 다른 값으로 바꾸는 콜백

    ```java
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("myomyo " + Thread.currentThread().getName());
        return "myomyo";
    }).thenApply(s -> { // 콜백을 get()호출하기 전에 정의하는것이 가능해짐
        System.out.println(Thread.currentThread().getName());
        return s.toUpperCase();
    });
    System.out.println(future.get()); // MYOMYO
    /*
    myomyo ForkJoinPool.commonPool-worker-19
    ForkJoinPool.commonPool-worker-19
    MYOMYO
    */
    ```
  - `thenAccept(Consumer)` : 리턴값을 또 다른 작업을 처리하는 콜백 (리턴X)

    ```java
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("myomyo " + Thread.currentThread().getName());
        return "myomyo";
    }).thenAccept(s -> {
        System.out.println(Thread.currentThread().getName());
        System.out.println(s.toUpperCase());
    });
    future.get();
    /*
    myomyo ForkJoinPool.commonPool-worker-19
    ForkJoinPool.commonPool-worker-19
    MYOMYO
    */
    ```
  - `thenRun(Runnable)` : 리턴값 받지 않고 다른 작업을 처리하는 콜백

    ```java
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("myomyo " + Thread.currentThread().getName());
        return "myomyo";
    }).thenRun(() -> {
        System.out.println(Thread.currentThread().getName());
    });
    future.get();
    /*
    myomyo ForkJoinPool.commonPool-worker-19
    ForkJoinPool.commonPool-worker-19
    */
    ```
  - 콜백 자체를 또 다른 쓰레드에서 실행 가능

    ```java
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("myomyo " + Thread.currentThread().getName());
        return "myomyo";
    }, executorService).thenRunAsync(() -> {
        System.out.println(Thread.currentThread().getName());
    }, executorService);
    future.get();
    executorService.shutdown();
    /*
    myomyo pool-1-thread-1
    pool-1-thread-2
    */
    ```
    - 기본은 Fork / Join Thread Pool 이지만, 원한다면 Executor에서 쓰레드풀 사용 가능
