# Optional 소개

`Optional<T>`는 제네릭 클래스로 T타입의 객체를 감싸는 래퍼 클래스이다.<br>
객체 담아서 반환을 하면, if문으로 null체크하는 대신 `Optional`에 정의된 메소드로 손쉽게 처리가능하다.

<br>

### 📚 자바 프로그래밍에서 NullPointerException을 보는 이유
- null을 리턴하거나 null 체크를 안했기 때문

```java
OnlineClass spring_boot = new OnlineClass(1, "spring boot", true);
Duration studyDuration = spring_boot.getProgress().getStudyDuration(); // NullPointerException 발생
System.out.println(studyDuration);
```

위 코드는 null이 들어올 수도 있는 값인데 체크르 하지 않아 예외가 발생했다.<br>
이런 코드를 아래와 같이 체크하게 된다.

```java
OnlineClass spring_boot = new OnlineClass(1, "spring boot", true);
// 에러를 만들기 좋은 코드 , null 체크 깜빡할 수도 있음
Progress progress = spring_boot.getProgress();
if(progress != null) {
  System.out.println(progress.getStudyDuration());
}
```

<br>

### 📚 메소드에서 작업 중 특별한 상황에서 값을 제대로 리턴할 수 없는 경우 선택할 수 있는 방법
- 예외를 던진다

  : 예외가 발생하면 스택 트레이스를 찍으므로 비용이 비쌈
  ```java
  public OnlineClass {
  ...
  public Progress getProgress() {
    if(this.progress == null) { // null 체크
      throw new IllegalStateException(); // Runtime Exception으로 던지면 클라이언트쪽 코드의 고통이 덜하다
    } // Checked Exception은 예외를 강제해야하므로
    return progress;
  }
  ```
- null을 리턴함

  : 비용 문제는 없지만 이 코드를 사용하는 클라이언트 코드가 주의해야 함
- Optional을 리턴함 (Java 8 이후)

  : 클라이언트 코드에게 명시적으로 빈 값일 수도 있다는 것을 알려주고, 빈 값인 경우에 대한 처리를 강제시킨다
  - 리턴타입으로만 사용하는 것이 권장사항
  - Optional.of(T)는 무조건 값이 있어야함 -> 없으면 NullPointerException 발생
  - Optional.ofNullable(T)는 T가 null인 것을 허용함
  
  
  ```java
  public Optional<Progress> getProgress() {
        return Optional.ofNullable(progress);
    }
  ```
  
<br>

### 📚 Optional
- 오직 값 한 개만 있을 수도 없을 수도 있는 컨테이너

<br>

### 😈 주의할 것
- 리턴값으로만 쓰기를 권장함
  - 권장하지 않는 방법
    - 메소드 매개변수 타입

    ```java
    public void setProgress(Optional<Progress> progress) {
      progress.ifPresent(p -> this.pregress = p);
    }
    ```
    
    Progress를 설정하는 메소드를 호출할 때 `setProgress(null)`로도 할 수 있다.<br>
    그러면 null을 사용하기에 NullPotinterException 발생도하며, 이러한 null 체크를 더 넣어야하므로 번거로워진다.
    - 맵(Map)의 키 타입

      : 맵의 키는 절대 null이 될 수 없음
    - 인스턴스 필드 타입

      : 도메인 클래스 설계의 문제임. 권장X
- Optional을 리턴하는 메소드에서 null을 리턴하지 말자
  
  : null 대신에 `Optional.empty()` 반환하기
  ```java
  public Optional<Progress> getProgress() {
    // return null; // null 대신 아래코드 사용
    return Optional.empty(); // 비어있는 Optional 반환
  }
  ```
- primitive 타입용 Optional이 따로 있음 (OptionalInt, OptionalLong ...)

  : Optinal에 기본형 타입을 넣을 순 있는데 박싱&언박싱이 이뤄지므로 성능상 좋지않음
  ```java
  Optional.of(10); // 권장 X
  OptionalInt.of(10); // 권장 O
  ```
  
- Collection, Map, Stream Array, Optional은 Optional로 감싸지 말 것

  - 컨테이너 성격의 인스턴스를 Optional로 감싸지 말 것 (의미가 없음)
  - 비어있는지 표현할 수 있는 컨테이너 성격의 타입들이기 때문
  - Optional로 감싸면 2번이나 감싸는 것

<br>

# 🙋‍♂️ 추가적으로 궁금한 부분

### NPE(NullPointerException)은 언제 발생하는가?
- null의 인스턴스 호출할 때
- null object의 필드를 수정 또는 접근할 경우
- null array에서 길이를 알려고 할 때
- null array를 수정 또는 접근할 경우
- Throwable 값인것 처럼 null을 던질 경우

<br>

### Optional을 인스턴스 필드 타입으로 쓰면 안되는 이유?

Optional은 메소드 리턴타입으로 쓰라는 용도로 만들어 진 것이다.<br>
그 외의 용도로 사용하면 만든 취지에 어긋난 것이다.

설계 측면에서 인스턴스 필드 타입으로 쓰게되면 "어떤 필드가 있을 수도 있고 없을 수도 있다"와도 같다.<br>
따라서 해당 필드가 그 도메인에 있는게 맞는지 다시 생각해봐야 한다.

[스택오버플로우 댓글에서 참고하기](https://stackoverflow.com/questions/26327957/should-java-8-getters-return-optional-type/26328555#26328555)

