package me.whiteship.java8to11;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Repeatable(ChickenContainer.class) // 여러개의 어노테이션을 감싸고 있을 컨테이너 애노테이션 타입을 선언해야 함
public @interface Chicken {
    String value();
}
