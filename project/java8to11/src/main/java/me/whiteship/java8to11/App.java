package me.whiteship.java8to11;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        // List 안에 "optional"으로 시작되는 값이 있을지 없을지 모르므로 Optional로 감싸서 반환함
        Optional<OnlineClass> optional = springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("spring"))
                .findFirst();

        // Optional 안에 값이 있는지 확인
        boolean present = optional.isPresent();
        System.out.println("present = " + present);

        // Java 11부터 제공, 값이 없는지 확인
        boolean empty = optional.isEmpty();
        System.out.println("empty = " + empty);

        // 값이 있으면 문제가 없는데, 값이 없으면 RuntimeException인 NoSuchElementException 발생하므로 검사 먼저 하기
        if (optional.isPresent()) {
            OnlineClass onlineClass = optional.get();
            System.out.println("onlineClass = " + onlineClass.getTitle());
        }

        // 검사 방법은 if문 말고 다르게 확인하는 메소드들이 있음. 따라서 get()을 가급적 사용하지말고 메소드를 사용한다
        // 값이 있으면 안에 함수를 실행한다.
        optional.ifPresent(oc -> System.out.println(oc.getTitle()));

        // 위 방법에서 만약 값을 가지고 뭔가를 해야한다면 orElse()를 사용한다.
        // 있으면 값을 꺼내고, 없으면 매개변수에 정의한 값을 리턴하는 메소드이다.
        // 단점은 Optional에 값이 있든 없든 무조건 안에 정의한 값을 수행한다.
        OnlineClass onlineClass = optional.orElse(createNewClass());
        System.out.println("onlineClass = " + onlineClass.getTitle());

        // 위 메소드와 비슷하게 작동하면서 단점을 해결한 것이 orElseGet()이다.
        // 이것은 값이 없을때만 정의한 값을 수행하게 된다.
        OnlineClass onlineClass1 = optional.orElseGet(App::createNewClass);
        System.out.println("onlineClass1 = " + onlineClass1.getTitle());

        // 값이 있으면 꺼내고, 없으면 에러를 던진다.
        OnlineClass onlineClass2 = optional.orElseThrow(IllegalStateException::new);
        System.out.println("onlineClass2 = " + onlineClass2.getTitle());

        // filter조건에 충족하는 값을 가져온다. 조건에 해당하는게 없다면 Optional.empty() 반환함
        Optional<OnlineClass> onlineClass3 = optional.filter(OnlineClass::isClosed);
        System.out.println(onlineClass3.isPresent());

        // Optional에 들어있는 값 반환한다.
        Optional<Integer> integer = optional.map(OnlineClass::getId);
        System.out.println("integer = " + integer.isPresent());

        // 리턴되는 값이 Optional이면 한번더 꺼낸다.
        Optional<Progress> progress = optional.flatMap(OnlineClass::getProgress);
        Optional<Optional<Progress>> progress1 = optional.map(OnlineClass::getProgress);
        Optional<Progress> progress2 = progress1.orElse(Optional.empty());

    }

    private static OnlineClass createNewClass() {
        System.out.println("creating new online class");
        return new OnlineClass(10, "New Class", false);
    }
}