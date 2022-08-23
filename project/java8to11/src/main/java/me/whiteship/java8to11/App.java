package me.whiteship.java8to11;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Calendar myomyoBirthDay = new GregorianCalendar(1997, Calendar.JANUARY, 15);
        System.out.println(myomyoBirthDay.getTime()); // 캘린더로 getTime하니까 날짜가 나옴
        myomyoBirthDay.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println(myomyoBirthDay.getTime());
    }
}