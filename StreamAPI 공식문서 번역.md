A sequence of elements supporting sequential and parallel aggregate operations. 

일련의 요소들은 순차적 및 병렬 집계 연산을 지원합니다.

The following example illustrates an aggregate operation using Stream and IntStream:

다음 예제에서 Stream과 IntStream을 사용하는 집계연산을 보여줍니다.

```java
int sum = widgets.stream()
                 .filter(w -> w.getColor() == RED)
                 .mapToInt(w -> w.getWeight())
                 .sum();
```

In this example, `widgets` is a `Collection<Widget>`. 

이 예시에서 widgets(위젯들)은 Widget 타입의 컬렉션입니다.

We create a stream of `Widget` objects via `Collection.stream()`, 

Collection.stream()을 사용하여 위젯 객체의 스트림을 생성하고,

filter it to produce a stream containing only the red widgets, 

RED인 위젯들만 들어있는 스트림을 제공하도록 필터하고,

and then transform it into a stream of `int` values representing the weight of each red widget. 

그런 다음 RED 위젯의 각각의 weight(무게)를 표현하는 int(정수)값의 스트림으로 변환시킵니다.

Then this stream is summed to produce a total weight.

그 다음에 이 스트림은 총 무게를 생성하려고 합쳐집니다.

In addition to `Stream`, which is a stream of object references, there are primitive specializations for `IntStream`, `LongStream`, and `DoubleStream`, 

객체 참조형 스트림 외에도 primitive(기본형)에 특화된 IntStream, LongStream, DoubleStream이 있으며,

all of which are referred to as "streams" and conform to the characteristics and restrictions described here.

그 모든 것이 “streams"라고 하며 여기에 설명된 특징과 제한을 따릅니다.

To perform a computation, stream operations are composed into a *stream pipeline*. 

연산을 수행하기위해 스트림 작업(stream operations)은 스트림 파이프라인으로 구성된다.

A stream pipeline consists of a source (which might be an array, a collection, a generator function, an I/O channel, etc), 

스트림 파이프라인은 소스(배열, 컬렉션, 제너레이터 함수, I/O 채널 등),

zero or more *intermediate operations* (which transform a stream into another stream, such as `filter(Predicate)`), 

0 또는 다수의 중간 작업(스트림을 다른 스트림으로 변환, 예를 들면 filter(Predicate)),

and a *terminal operation* (which produces a result or side-effect, such as `count()` or `forEach(Consumer)`). 

그리고 종료 작업(결과 또는 사이드이펙트 생성, 예를 들면 count() 또는 forEach(Consumer))로 구성되어있다.

Streams are lazy; computation on the source data is only performed when the terminal operation is initiated, and source elements are consumed only as needed.

스트림은 게으르다. 소스 데이터에 대한 연산은 오직 종료 작업이 시작될 때만 수행되고, 소스 요소는 필요한만큼 소모된다.

Collections and streams, while bearing some superficial similarities, have different goals.

컬렉션과 스트림은 일부 표면적 유사성을 지니긴 하지만, 다른 목표를 가지고 있습니다.

Collections are primarily concerned with the efficient management of, and access to, their elements.

컬렉션은 주로 해당 요소의 효율적인 관리 및 접근과 관련된 것입니다.

By contrast, streams do not provide a means to directly access or manipulate their elements, 

대조적으로, 스트림은 해당 요소에 직접 접근 또는 조작하는 방법을 제공하지 않으며,

and are instead concerned with declaratively describing their source and the computational operations which will be performed in aggregate on that source.

대신에 그것들의 소스와 그 소스에서 종합적으로 수행될 컴퓨터의 작업을 설명하는 것과 관련이 있다.

However, if the provided stream operations do not offer the desired functionality, 

그러나, 제공됐던 스트림 작업이 원했던 기능을 제공받지 않으면,

the BaseStream.iterator() and BaseStream.spliterator() operations can be used to perform a controlled traversal.

BaseStream.iterator() 와 BaaseStream.spliterator() 연산을 사용하여 제어된 순회를 수행할 수 있다.

A stream pipeline, like the "widgets" example above, can be viewed as a *query* on the stream source.

위에 위젯 예시와 같은 스트림 파이프라인은 스트림 소스에 대한 쿼리로 볼 수 있습니다.

Unless the source was explicitly designed for concurrent modification (such as a ConcurrentHashMap), 

소스가 동시 수정용(예를 들면 ConcurrentHashMap)으로 명시적으로 설계되지 않은 경우,

unpredictable or erroneous behavior may result from modifying the stream source while it is being queried.

쿼리하는 동안 스트림 소스를 수정하면 예측할 수 없거나 잘못된 동작이 발생할 수 있습니다.
