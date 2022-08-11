package me.whiteship.java8to11;

public class DefaultFoo implements Foo, Bar {

    String name;

    public DefaultFoo(String name) {
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(name);
    }

    @Override
    public void printNameUpperCase() {
        Foo.super.printNameUpperCase();
    }

    @Override
    public String getName() {
        return name;
    }
}
