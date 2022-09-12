# Callable과 Future

- Callable
  - Runnable과 유사하지만 작업의 결과를 받을 수 있음
- Future
  - 비동기적인 작업의 현재 상태를 조회하거나 결과를 가져올 수 있음

<br>

결과 가져오기 - `get()`
```java
ExecutorService executorService = Executors.newSingleThreadExecutor();

Callable<String> hello = () -> {
    Thread.sleep(2000L);
    return "Hello";
};

Future<String> submit = executorService.submit(hello);
System.out.println("Started!");

submit.get(); // 결과값을 가져올때까지 기다림 -> 블록킹 콜

System.out.println("End!!");
executorService.shutdown(); // 처리중인 작업 기다렸다가 종료
```
- 블록킹 콜
- 타임아웃(최대한으로 기다릴 시간)을 설정 가능
  - 타임아웃을 설정하지않으면 결과를 끝까지 기다리지만, 설정하면 일정 시간 내에 응답이 없으면 다른 작업을 처리하도록 만들 수 있음
  - `get(long timeout, TimeUnit unit)` 으로 사용함
  - 일정시간 내에 결과값을 못받으면 `TimeoutException` 발생함

  ```java
  submit.get(10, TimeUnit.SECONDS); // 10초동안 기다림
  ```

<br>

작업 상태 확인하기 - `isDone()`
```java
ExecutorService executorService = Executors.newSingleThreadExecutor();

Callable<String> hello = () -> {
    Thread.sleep(2000L);
    return "Hello";
};

Future<String> helloFuture = executorService.submit(hello);
System.out.println(helloFuture.isDone()); // 작업이 완료되지 않았으므로 false
System.out.println("Started!");

helloFuture.get();

System.out.println(helloFuture.isDone()); // 작업이 완료됐으므로 true
System.out.println("End!!");
executorService.shutdown();
```
- 완료했으면 true 아니면 false 리턴

<br>

작업 취소하기 - `cancel()`
```java
ExecutorService executorService = Executors.newSingleThreadExecutor();

Callable<String> hello = () -> {
    Thread.sleep(2000L);
    return "Hello";
};

Future<String> helloFuture = executorService.submit(hello);
System.out.println(helloFuture.isDone()); // false
System.out.println("Started!");

helloFuture.cancel(false);

System.out.println(helloFuture.isDone()); // true -> 작업을 취소했으니 무조건 true가 뜨게됨

helloFuture.get(); // CancellationException 발생 -> 취소된 작업에 결과를 받겠다고 해서

System.out.println("End!!");
executorService.shutdown();
```
- 취소했으면 true, 못했으면 false 리턴
- parameter로 true를 전달하면 현재 진행중인 쓰레드를 interrupt하고 그러지 않으면 현재 진행중인 작업이 끝날때까지 기다림
- `cancel()` 하면 `get()` 으로 못가져옴. -> 가져오면 CancellationException 발생!
- `cancel()` 하면 `isDone()` 은 무조건 true

<br>

여러 작업 동시에 실행하기 - `invokeAll()`
```java
ExecutorService executorService = Executors.newSingleThreadExecutor();

Callable<String> hello = () -> {
    Thread.sleep(2000L);
    return "Hello";
};

Callable<String> java = () -> {
    Thread.sleep(3000L);
    return "Java";
};

Callable<String> myomyo = () -> {
    Thread.sleep(1000L);
    return "Myomyo";
};

List<Future<String>> futures = executorService.invokeAll(Arrays.asList(hello, java, myomyo));
for (Future<String> f : futures) {
    System.out.println(f.get());
}

executorService.shutdown();
```
- 동시에 실행한 작업들 전부 기다림
  - 위 코드에서는 hello 2초 + java 3초 + myomyo 1초 = 총 6초를 기다려야함
- 먼저 끝난 작업이 있더라도 모든 작업이 끝날때까지 기다려야하므로, 가장 오래 걸리는 작업만큼 시간이 더 걸림

<br>

여러 작업 중 하나라도 먼저 응답이 오면 끝내기 - `invokeAny()`
```java
// 싱글 쓰레드면 hello가 끝나야 다른 작업이 들어가므로 invokeAny()를 확인하기 어려움.
ExecutorService executorService = Executors.newFixedThreadPool(3); // 따라서 쓰레드 3개 생성

Callable<String> hello = () -> {
    Thread.sleep(2000L);
    return "Hello";
};

Callable<String> java = () -> {
    Thread.sleep(3000L);
    return "Java";
};

Callable<String> myomyo = () -> {
    Thread.sleep(1000L);
    return "Myomyo";
};

String s = executorService.invokeAny(Arrays.asList(hello, java, myomyo));
System.out.println(s); // Myomyo -> 1초라서 다른 작업중에서 제일 빠르므로

executorService.shutdown();
```
- 동시에 실행한 작업 중에 제일 짧게 걸리는 작업만큼 시간이 걸림
- 블록킹 콜
