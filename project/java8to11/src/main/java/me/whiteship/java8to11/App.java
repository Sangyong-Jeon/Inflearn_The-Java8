package me.whiteship.java8to11;

public class App {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                // 현재 thread를 깨워서 예외처리
            }
        });
        thread.start();
        System.out.println("Hello: " + Thread.currentThread().getName());
        try {
            thread.join(); // 이 쓰레드 끝날때까지 main쓰레드는 기다림
        } catch (InterruptedException e) { // 멀티 쓰레드 처리방법으로 쓰레드 한두개만해도 예외처리가 복잡해짐.
            // 위 thread가 끝나는걸 대기중인데 main 쓰레드를 깨워서 예외처리
        }
        System.out.println(thread + " is finished");
    }
}