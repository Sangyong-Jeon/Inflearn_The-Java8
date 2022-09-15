package me.whiteship.java8to11;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 컨테이너가 가지고 있는 리텐션과 타겟 정보는 반드시 자기 자신을 감쌀 애노테이션보다 같거나 더 넓어야함
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface ChickenContainer {

    // 자기가 감싸고 있을 어노테이션을 배열로 가지고 있어야 함
    Chicken[] value();
}
