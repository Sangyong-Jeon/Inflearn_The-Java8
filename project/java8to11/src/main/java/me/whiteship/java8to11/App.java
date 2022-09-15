package me.whiteship.java8to11;

import java.util.Arrays;

@Chicken("양념")
@Chicken("마늘간장")
public class App {
    public static void main(String[] args) {
        // 클래스에서 getAnnotationsByType()에 Chicken 타입을 주면, 그 타입에 해당하는 애노테이션을 배열로 다 가져옴
        Chicken[] chickens = App.class.getAnnotationsByType(Chicken.class);
        Arrays.stream(chickens).forEach(c -> {
            System.out.println(c.value());
        });

        // getAnnotation()은 애노테이션 1개만 가져오므로, 여기서는 ChickenContainer을 가져와서 감싸고 있는 값들을 전부 출력
        ChickenContainer chickenContainer = App.class.getAnnotation(ChickenContainer.class);
        Arrays.stream(chickenContainer.value()).forEach(c -> {
            System.out.println(c.value());
        });
    }
}