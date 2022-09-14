# CompletableFuture 2

- 조합하기
  - `thenCompose()` : 두 작업이 서로 이어서 실행하도록 조합

    ```java
    public class App {
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
                System.out.println("Hello " + Thread.currentThread().getName());
                return "Hello";
            });
    
            CompletableFuture<String> future = hello.thenCompose(App::getWorld); //hello가 끝나고 리턴을 getWorld가 받아서 작업
            System.out.println(future.get());
        }
    
        private static CompletableFuture<String> getWorld(String message) {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println("World " + Thread.currentThread().getName());
                return message + " World";
            });
        }
    }
    /*
    Hello ForkJoinPool.commonPool-worker-19
    World ForkJoinPool.commonPool-worker-5
    Hello World
    */
    ```
  - `thenCombine()` : 두 작업을 독립적으로 실행하고 둘 다 종료했을 때 콜백 실행

    ```java
    public class App {
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
                System.out.println("Hello " + Thread.currentThread().getName());
                return "Hello";
            });
            
            CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
                System.out.println("World " + Thread.currentThread().getName());
                return "World";
            });
            // 작업을 이어서하는게 아니라, 비동기적이고 독립적으로 작업을 할 때, 2개의 작업이 전부 끝났을 때 BiFunction을 실행함
            // h는 hello의 결과값, w는 world의 결과값
            CompletableFuture<String> future = hello.thenCombine(world, (h, w) -> h + " " + w);
            System.out.println(future.get());
        }
    }
    /*
    Hello ForkJoinPool.commonPool-worker-19
    World ForkJoinPool.commonPool-worker-23
    Hello World
    */
    ```
  - `allOf()` : 여러 작업을 모두 실행하고 모든 작업 결과에 콜백 실행

    ```java
        public class App {
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
                System.out.println("Hello " + Thread.currentThread().getName());
                return "Hello";
            });
            
            CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
                System.out.println("World " + Thread.currentThread().getName());
                return "World";
            });
            
            // 모든 작업이 끝났을 때 콜백 실행함
            // 하지만 결과값이 동일한 타입인지 보장 X, 어떤 작업은 에러 발생할 수도 있음
            CompletableFuture<Void> future = CompletableFuture.allOf(hello, world).thenAccept(System.out::println);
            System.out.println(future.get());
        }
    }
    /*
    Hello ForkJoinPool.commonPool-worker-19
    World ForkJoinPool.commonPool-worker-5
    null
    null
    */
    ```
    
    - 모든 작업 결과를 컬렉션으로 반환받기 (강사님 코드)
      - 아무것도 블록킹 되지 않음
      - thenApply(Function)에 넘겨준 Function이 사용되는 시점에 이미 모든 작업이 끝난 상태라서 join()으로 결과값을 가져온 것

      ```java
      List<CompletableFuture<String>> futures = Arrays.asList(hello, world);
      CompletableFuture<String>[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);
      CompletableFuture<List<String>> results = CompletableFuture.allOf(futuresArray)
              .thenApply(v -> futures.stream()
                      .map(CompletableFuture::join) // join()은 Unchecked Exception, get()은 Checked Exception 발생
                      .collect(Collectors.toList()));
      results.get().forEach(System.out::println);
      /*
      Hello ForkJoinPool.commonPool-worker-19
      World ForkJoinPool.commonPool-worker-5
      Hello
      World
      */
      ```
      
    - 모든 작업 결과를 컬렉션으로 반환받기 (내가 리팩토링한 코드)

      ```java
      CompletableFuture<List<String>> results = CompletableFuture.allOf(hello, world)
              .thenApply(v -> Arrays.asList(hello.join(), world.join()));
      results.get().forEach(System.out::println);
      /*
      Hello ForkJoinPool.commonPool-worker-19
      World ForkJoinPool.commonPool-worker-5
      Hello
      World
      */
      ```
  - `anyOf()` : 여러 작업 중에 가장 빨리 끝난 하나의 결과에 콜백 실행

    ```java
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
    System.out.println("Hello " + Thread.currentThread().getName());
        return "Hello";
    });

    CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
        System.out.println("World " + Thread.currentThread().getName());
        return "World";
    });

    CompletableFuture<Void> future = CompletableFuture.anyOf(hello, world).thenAccept(System.out::println);
    future.get();
    ```
- 예외 처리
  - `exeptionally(Function)`

    ```java
    boolean throwError = true;
        
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
        if (throwError) {
            throw new IllegalArgumentException();
        }
        System.out.println("Hello " + Thread.currentThread().getName());
        return "Hello";
    }).exceptionally(ex -> {
        System.out.println(ex);
        return "Error!";
    });

    System.out.println(hello.get());
    /*
    java.util.concurrent.CompletionException: java.lang.IllegalArgumentException
    Error!
    */
    ```
  - `handle(BiFunction)` : 정상적인 경우와 예외인 경우 둘 다 사용 가능
    - BiFunction의 첫번째 파라미터는 정상결과값, 두번째 파라미터는 에러값

    ```java
    boolean throwError = true;

    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
        if (throwError) {
            throw new IllegalArgumentException();
        }
        System.out.println("Hello " + Thread.currentThread().getName());
        return "Hello";
    }).handle((result, ex) -> {
        if (ex != null) {
            System.out.println(ex);
            return "ERROR!";
        }
        return result;
    });

    System.out.println(hello.get());
    ```
