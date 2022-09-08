# 자바 Concurrent 프로그래밍 소개

- Concurrent 소프트웨어
  - 동시에 여러 작업을 할 수 있는 소프트웨어
  - e.g. 웹 브라우저로 유튜브 보면서 문서 작업하기
- 자바에서 지원하는 Concurrent 프로그래밍
  - 멀티프로세싱 (ProcessBuilder)
  - 멀티쓰레드
- 자바 멀티쓰레드 프로그래밍
  - Thread/Runnable
  - 쓰레드의 순서는 보장되지 않음

Thread 상속

```java
public class App {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        // 쓰레드의 순서는 보장되지 않음, 코드상 쓰레드가 먼저지만 실행이 먼저 안되는 경우도 있음
        System.out.println("Hello: " + Thread.currentThread().getName()); 
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread: " + Thread.currentThread().getName());
        }
    }
}
```

Runnable 구현 또는 람다

```java
public class App {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> System.out.println("Thread: " + Thread.currentThread().getName()));
        thread.start();
        System.out.println("Hello: " + Thread.currentThread().getName());
    }
}
```

- 쓰레드 주요 기능
  - 현재 쓰레드 멈춰두기 (sleep) : 다른 쓰레드가 처리할 수 있도록 기회를 주지만 그렇다고 락을 놔주진 않음. (잘못하면 데드락 걸릴 수 있음)

  ```java
  public class App {
      public static void main(String[] args) {
         Thread thread = new Thread(() -> {
              try {
                  Thread.sleep(1000L); // 쓰레드 대기시킴으로써 다른 쓰레드에게 우선권이 감. 즉, main 쓰레드가 먼저 실행됨
             } catch (InterruptedException e) { // 자는동안(대기중) 깨우면 예외 발생
                  e.printStackTrace();
             }
             System.out.println("Thread: " + Thread.currentThread().getName());
          });
          thread.start();
          System.out.println("Hello: " + Thread.currentThread().getName());
      }
  }
  ```
  
  - 다른 쓰레드 깨우기 (interupt) : 다른 쓰레드를 깨워서 interruptedException을 발생시킴. 그 에러 발생시 할 일은 코딩하기 나름. 종료 or 하던일 계속 시키기 할 수 있음
    
  ```java
  public class App {
      public static void main(String[] args) throws InterruptedException {
          Thread thread = new Thread(() -> {
              while (true) {
                  System.out.println("Thread: " + Thread.currentThread().getName());
                  try {
                      Thread.sleep(1000L);
                  } catch (InterruptedException e) {
                      System.out.println("interrupted!");
                      return; // 쓰레드 종료됨, return 안하면 "interrupted!" 출력 후 하던일 계속함
                  }
              }
          });
          thread.start();
          System.out.println("Hello: " + Thread.currentThread().getName());
          Thread.sleep(3000L);
          thread.interrupt();
      }
  }
  ```
  
  - 다른 쓰레드 기다리기 (join) : 다른 쓰레드가 끝날 때까지 기다림
  
  ```java
    public class App {
      public static void main(String[] args) {
          Thread thread = new Thread(() -> {
              System.out.println("Thread: " + Thread.currentThread().getName());
              try {
                  Thread.sleep(3000L);
              } catch (InterruptedException e) {
                  // 현재 thread를 깨워서 예외처리
              }
          });
          thread.start(); // thread 실행
          System.out.println("Hello: " + Thread.currentThread().getName());
          try {
              thread.join(); // thread가 끝날때까지 main쓰레드는 기다림
          } catch (InterruptedException e) { // 멀티 쓰레드 처리방법으로 쓰레드 한두개만해도 예외처리가 복잡해짐.
              // thread가 끝나는걸 기다리는중인데 main 쓰레드를 깨워서 예외처리
          }
          System.out.println(thread + " is finished");
      }
  }
  ```

join 예제를 보면 쓰레드가 2개만 되어도 예외처리를 하는데 복잡해진다.
-> 그래서 Executors가 나왔다.
