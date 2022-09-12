# Callable과 Future

- Callable
  - Runnable과 유사하지만 작업의 결과를 받을 수 있음
- Future
  - 비동기적인 작업의 현재 상태를 조회하거나 결과를 가져올 수 있음

<br>

결과 가져오기 - `get()`
```java

```
- 블록킹 콜이다
- 타임아웃(최대한으로 기다릴 시간)을 설정 가능

<br>

작업 상태 확인하기 - `isDone()`
```java

```
- 완료했으면 true 아니면 false 리턴

<br>

작업 취소하기 - `cancle()`
```java

```
- 취소했으면 true, 못했으면 false 리턴
- parameter로 true를 전달하면 현재 진행중인 쓰레드를 interrupt하고 그러지 않으면 현재 진행중인 작업이 끝날때까지 기다림

<br>

여러 작업 동시에 실행하기 - `invokeAll()`
```java

```
- 동시에 실행한 작업 중에 제일 오래 걸리는 작업만큼 시간이 걸림

<br>

여러 작업 중 하나라도 먼저 응답이 오면 끝내기 - `invokeAny()`
```java

```
- 동시에 실행한 작업 중에 제일 짧게 걸리는 작업만큼 시간이 걸림
- 블록킹 콜이다.
