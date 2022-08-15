# Stream 소개

기존에는 Collection 또는 Iterator 메소드로 순회하거나 for문을 사용해서 많은 수의 데이터를 다뤘다. <br>
이 경우 데이터 소스마다 다른 방식으로 처리하기 힘들다.<br>
이런 문제점을 해결하기 위해 스트림(Stream)이 나왔다.<br>
스트림은 데이터 소스를 추상화하고, 데이터를 다루는데 자주 사용되는 메소드를 정의해놨다.<br>
예를 들면 다음과 같다.<br>
문자열 배열 `strArr` 과 같은 내용의 문자열을 저장하는 리스트인 `strList`가 있을 때
```java
// 데이터 소스
String[] strArr = {"aaa", "bbb", "ccc"};
List<String> strList = Arrays.asList(strArr);

// 위 데이터 소스의 데이터를 읽어서 정렬하고 출력함. 이 때 데이터 소스가 정렬되는 것은 아님.
Arrays.stream(strArr).sorted().forEach(System.out::println);
strList.stream().sorted().forEach(System.out::println);

// 자바8 이전
Arrays.sort(strArr);
Collections.sort(strList);

for (String str : strArr) System.out.println(str);
for (String str : strList) System.out.println(str);
```
스트림으로 하는 작업은 한 줄로 표현되며, 기존 데이터 소스를 변경하지 않는다.<br>
하지만 자바8 이전방식으로는 데이터 소스들을 정렬시킨 후 출력하게 된다.<br>
이처럼 스트림을 사용한 코드가 간결하고 이해하기 쉬우며 재사용성이 높다는 것을 알 수 있다.

<br>

### 스트림은 데이터 소스를 변경하지 않는다
```java
List<String> names = new ArrayList<>();
names.add("myomyo");
names.add("eee");
names.add("aaa");
names.add("ddd");

// 이것의 결과는 또 다른 스트림이 된다. 스트림으로 전달받은 데이터 자체를 대문자로 바꾸는 것이 아니다.
Stream<String> stringStream = names.stream().map(String::toUpperCase);
names.forEach(System.out::println);
/*
myomyo
eee
aaa
ddd
*/
```
`toUpperCase`로 소문자를 대문자로 변환하고 있다.<br>
이 때 `names`의 데이터들은 소문자로 출력되고 있다.<br>
즉, 스트림은 데이터 소스로부터 데이터를 읽기만 할 뿐 변경하지 않는다는 말이다.

<br>

필요에 따라 정렬되거나 변환된 결과를 컬렉션이나 배열에 담아서 아래 코드처럼 반환할 수 있다.
```java
List<String> stringStream = names.stream()
                                  .map(String::toUpperCase)
                                  .collect(Collectors.toList());
```

<br>

### 스트림은 일회용이다
스트림은 Iterator처럼 일회용이다. Iterator로 컬렉션의 요소를 모두 읽고나면 다시 사용할 수 없는 것처럼,<br>
스트림도 한번 사용하면 닫혀서 재사용이 불가능하다. 필요하다면 스트림을 다시 생성해야 한다.
```java
Stream<String> stream = names.stream();
stream.forEach(System.out::println);
long number = stream.count(); // ERROR. 스트림이 닫혀있음
```
스트림이 `forEach`에서 사용되고 닫혔는데 그 후 `count()`를 사용하려고하면 `IllegalStateException : stream has already been operated upon or closed` 발생

<br>

### 스트림은 작업을 내부 반복으로 처리한다

스트림이 간결할 수 있는 이유이다.<br>
내부 반복이라는 것은 반복문을 메소드 내부에 숨긴다는 것이다.<br>
아래 코드에서 for문이 forEach() 메소드안으로 들어간 것이다.
```java
for(String str : names)
	System.out.println(str);

// 스트림으로 변경
stream.forEach(System.out::println);
```

<br>

### 스트림 연산

스트림은 다양한 연산을 제공하는데 다음 2종류로 분류된다.

- 중간 연산 : 연산결과를 스트림으로 반환하기에 중간 연산을 연속해서 연결 가능
  - distinct : 중복 제거
  - filter : 조건에 안맞는 요소 제외
  - limit : 일부 잘라내기
  - skip : 일부 건너뛰기
  - peek : 스트림 요소에 작업 수행
  - sorted : 스트림 요소 정렬
  - ...
- 최종 연산 : 스트림의 요소를 소모하면서 연산을 수행하므로 1번만 연산 가능
  - forEach | forEachOrdered : 각 요소에 지정된 작업 수행
  - count : 스트림 요소 개수 반환
  - max | min : 스트림 최대값 | 최소값 반환
  - findAny | findFirst : 스트림 요소 아무거나 하나 반환 | 첫번째 요소 반환
  - toArray : 스트림 모든 요소를 배열로 반환
  - collect : 스트림의 요소를 그룹화하거나 분할한 결과를 컬렉션에 담아 반환
  - ...
```java
stream.distinct().limit(5).sorted().forEach(System.out::println);
			// 중간연산   중간연산   중간연산    최종연산
```

<br>

### 지연된 연산

스트림 연산에서 중요한 점은 최종 연산이 수행되기 전까지는 중간 연산이 수행되지 않는다는 점이다.<br>
즉, 중간 연산을 호출해도 즉각적인 연산이 수행되는 것은 아니다.<br>
최종 연산이 수행되어야 스트림의 요소들이 중간 연산을 거쳐 최종 연산에서 소모된다.

예를 들면 최종 연산(종료 오퍼레이션)이 없는 경우
```java
List<String> names = new ArrayList<>();
names.add("myomyo");
names.add("eee");
names.add("aaa");
names.add("ddd");

names.stream().map((s) -> {
	System.out.println(s);
	return s.toUpperCase();
}); // 중개 오퍼레이션은 종료 오퍼레이션이 없기 때문에 출력되지 않음

System.out.println("================");
names.forEach(System.out::println);
/*
================
myomyo
eee
aaa
ddd
*/
```

최종 연산이 있는 경우
```java
List<String> names = new ArrayList<>();
names.add("myomyo");
names.add("eee");
names.add("aaa");
names.add("ddd");

names.stream().map((s) -> {
	System.out.println(s);
	return s.toUpperCase();
}).collect(Collectors.toList()); // collect라는 종료 오퍼레이션을 수행하기에 출력됨

System.out.println("================");
names.forEach(System.out::println);
/*
myomyo
eee
aaa
ddd
================
myomyo
eee
aaa
ddd
*/
```

<br>

### Stream<Integer>와 IntStream
  
요소의 타입이 T인 스트림은 기본적으로 Stream<T>이다.<br>
오토박싱&언박싱으로 인한 비효율을 줄이기 위해 데이터 소스의 요소를 기본형으로 다루는 스트림이 제공된다. (IntStream, LongStream, DoubleStream)<br>
따라서 Stream<Integer> 보단 IntStream이 효율적이다.
  
<br>
  
### 병렬 스트림
```java
//일반 for문은 병렬 처리가 힘듬
for (String name : names) {
	if(name.startsWith("m")) {
		System.out.println(name.toUpperCase());
	}
}

// 스트림은 JVM이 알아서 병렬적 처리해줌
names.parallelStream()
			.filter(s -> s.startsWith("m"))
			.forEach(s -> System.out.println(s.toUpperCase()));
```
`names`에서 "m"으로 시작하는 이름을 대문자로 변경하여 출력하는 코드를 일반 for문과 스트림으로 구현한 것이다.<br>
- 일반 for문은 병렬 처리가 힘듬
- 스트림은 JVM이 알아서 병렬 처리를 손쉽게 해줌
- 무조건 병렬 처리가 성능적으로 좋은 것은 아님
  - 데이터가 정말 방대할 때 병렬 처리를 사용하고, 그 외의 경우엔 그냥 스트림을 사용함

<br>
  
# 결론
  
### Stream
- 연속된 데이터를 처리하는 오퍼레이션의 모음
- 데이터를 담고 있는 저장소(컬렉션)가 아니다
  - 컬렉션은 데이터를 가지는 것
  - 스트림은 이러한 데이터를 소스로 사용해서 어떤 처리를 하는 것
- Functional in nature , 스트림이 처리하는 데이터 소스를 변경하지 않음
- 스트림으로 처리하는 데이터는 오직 한번만 처리함
- 무제한일 수도 있음 (Short Circuit 메소드를 사용해서 제한할 수 있음)
  - 실시간으로 데이터 계속 들어올 수 있음
- 중개 오퍼레이션은 근본적으로 lazy 하다
- 손쉽게 병렬 처리를 할 수 있음
  
<br>
  
### 스트림 파이프라인
- 0 또는 다수의 중개 오퍼레이션(intermediate operation)과 한 개의 종료 오퍼레이션(terminal operation)으로 구성함
- 스트림의 데이터 소스는 오직 터미널 오퍼레이션을 실행할 때만 처리함
  
<br>
  
### 중개 오퍼레이션
- Stream을 리턴함
- Stateless | Stateful 오퍼레이션으로 더 상세하게 구분 가능
  - 대부분 Stateless이지만 `distinct`나 `sorted`처럼 이전 소스 데이터를 참조해야하는 오퍼레이션은 stateful 오퍼레이션이다
- filter, map, limit, skip, sorted, ...
  
<br>
  
### 종료 오퍼레이션
- Stream을 리턴하지 않음
- collect, allMatch, count, forEach, min, max, ...
