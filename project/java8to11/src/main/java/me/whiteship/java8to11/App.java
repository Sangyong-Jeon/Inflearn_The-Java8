package me.whiteship.java8to11;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.TimeZone;

public class App {
    public static void main(String[] args) {
        Instant instant = Instant.now(); // 지금 시간
        System.out.println(instant); // 사용자 친화적, 기준시 UTC (GMT)
        System.out.println(instant.atZone(ZoneId.of("UTC"))); // 사용자 친화적, 기준시 UTC (GMT)

        ZoneId zone = ZoneId.systemDefault();
        System.out.println(zone);
        ZonedDateTime zonedDateTime = instant.atZone(zone);
        System.out.println(zonedDateTime);

        System.out.println("====");

        LocalDateTime now = LocalDateTime.now();// 휴먼용, 현재 시스템 Zone에 해당하는 일시 리턴
        System.out.println(now);
        LocalDateTime birthDay = LocalDateTime.of(1997, Month.JANUARY, 1, 0, 0, 0);
        System.out.println(birthDay);
        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));// 특정 Zone의 시간을 보고 싶다
        System.out.println(nowInKorea);

        Instant nowInstant = Instant.now();
        ZonedDateTime zonedDateTime1 = nowInstant.atZone(ZoneId.of("Asia/Seoul"));
        System.out.println(zonedDateTime1); // Instant <-> ZonedDateTime <-> LocalDateTime

        System.out.println("====");

        LocalDate today = LocalDate.now();
        LocalDate thisYearBirthDay = LocalDate.of(2022, Month.JANUARY, 1);

        Period period = Period.between(today, thisYearBirthDay); // 휴먼용 시간 비교
        System.out.println(period.getDays());

        Period until = today.until(thisYearBirthDay); // 오늘부터 이때까지
        System.out.println(until.get(ChronoUnit.DAYS));

        System.out.println("====");

        Instant now1 = Instant.now();
        Instant plus = now1.plus(10, ChronoUnit.SECONDS); // ChronoUnit은 외우기. 이뮤터블하기 때문에 인스턴스 만들어야함
        Duration between = Duration.between(now1, plus);  // 머신용 시간 비교
        System.out.println(between.getSeconds());

        System.out.println("====");

        LocalDateTime now2 = LocalDateTime.now();
        DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.println(now.format(MMddyyyy));

        LocalDate parse = LocalDate.parse("07/15/1999", MMddyyyy);
        System.out.println(parse);

        System.out.println("====");
        // 레거시 코드와 호환가능
        Date date = new Date();
        Instant instant1 = date.toInstant();
        Date newDate = Date.from(instant1);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        LocalDateTime dateTime = gregorianCalendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        ZonedDateTime zonedDateTime2 = gregorianCalendar.toInstant().atZone(ZoneId.systemDefault());
        GregorianCalendar from = GregorianCalendar.from(zonedDateTime2);

        ZoneId zoneId = TimeZone.getTimeZone("PST").toZoneId(); // 예전 api에서 최신 api로
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);

        System.out.println("===오늘부터 내년 생일까지 몇일 남았는가?===");
        LocalDate today2 = LocalDate.now(); // 2022-08-25
        LocalDate future = LocalDate.of(2023, Month.JANUARY, 1);// 2023-01-01
        long day = ChronoUnit.DAYS.between(today2, future);
        System.out.println(day); // 129
    }
}