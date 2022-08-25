# Date와 Time API

작성 날짜 : 2022-08-25

- 기계 시간으로 표현하는 방법
  - `Instant.now()` : 현재 UTC(GMT)를 리턴
  - Universal Time Coordinated == Greenwich Mean Time
  
  <br>
  
  ```java
  Instant instant = Instant.now(); // 지금 시간 기계시간으로 출력
  System.out.println(instant); // 2022-08-25T01:51:59.574386Z
  System.out.println(instant.atZone(ZoneId.of("UTC"))); // 2022-08-25T01:51:59.574386Z[UTC]
  
  ZoneId zone = ZoneId.system.Default();
  System.out.println(zone); // Asia/Seoul
  ZonedDateTime zonedDateTime = instant.atZone(zone);
  System.out.println(zonedDateTime); // 2022-08-25T10:54:46.219101+09:00[Asia/Seoul]
  ```

<br>

- 인류용 일시를 표현하는 방법
  - `LocalDateTime.now()` : 현재 시스템 Zone에 해당하는 (로컬)일시를 리턴
  - `LocalDateTime.of(int, Month, int, int, int, int)` : 로컬의 특정 일시를 리턴
  - `ZonedDateTime.of(int, Month, int, int, int, int, ZoneId)` : 특정 Zone의 특정 일시를 리턴

  <br>
  
  ```java
  LocalDateTime now = LocalDateTime.now(); // 현재 시스템 Zone에 해당하는 일시이므로 한국 기준 시간 리턴
  System.out.println(now); // 2022-08-25T10:54:46.258329
  LocalDateTime birthDay = LocalDateTime.of(1997, Month.JANUARY, 1, 0, 0, 0);
  System.out.println(birthDay); // 1997-01-01T00:00
  ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); // 특정 Zone의 시간대 리턴하는 방법
  System.out.println(nowInKorea); // 2022-08-25T10:59:56.849367+09:00[Asia/Seoul]
  
  Instant nowInstant = Instant.now();
  ZonedDateTime zonedDateTime = nowInstant.atZone(ZoneId.of("Asia/Seoul"));
  System.out.println(zonedDateTime); 
  zonedDateTime.toInstant(); // Instant <-> ZonedDateTime <-> LocalDateTime 등 여러가지로 서로 변환가능
  zonedDateTime.toLocalDateTime();
  zonedDateTime.toLocalDate();
  ```

<br>

- 기간을 표현하는 방법
  - `Period` : 인류용

  <br>

  ```java
  LocalDate today = LocalDate.now();
  LocalDate thisYearBirthDay = LocalDate.of(2022, Month.JANUARY, 1);
  
  Period period = Period.between(today, thisYearBirthDay); 
  System.out.println(period.getDays()); // -24
  
  Period until = today.until(thisYearBirthDay); // today부터 thisYearBirthDay까지
  System.out.println(until.get(ChronoUnit.DAYS)); // -24
  
  // 참고로 Period의 until()로 가보면 아래 주석이 있다.
  // these tow lines are equivalent , 아래 두 줄은 동일하다
  // period = start.until(end);
  // period = Period.between(start, end);
  ```
  
  - `Duration` : 기계용

  <br>
  
  ```java
  Instant now = Instant.now();
  // now.plus(10, ChronoUnit.SECONDS); // immutable하므로 이렇게만 작성하면 아무 일도 일어나지 않음
  Instant plus = now.plus(10, ChronoUnit.SECONDS);
  Duration between = Duration.between(now, plus);
  System.out.println(between.getSeconds()); // 10
  ```

<br>

- 파싱(parsing) 또는 포매팅(formatting)
  - 미리 정의된 포맷 및 패턴 작성방법 참고 [(공식문서)](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#predefined)
  
  <br>
  
  ```java
  LocalDateTime now = LocalDateTime.now();
  // 포매팅
  DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  System.out.println(now.format(MMddyyyy)); // 08/25/2022
  
  // 파싱
  LocalDate parse = LocalDate.parse("07/15/1999", MMddyyyy);
  System.out.println(parse); // 1999-07-15
  ```
  
<br>

- 레거시 API 지원
  - 예전 API와 호환이 가능하므로 서로 변환도 가능함

  <br>
  
  ```java
  Date date = new Date();
  Instant instant = date.toInstant(); // Date -> Instant
  Date newDate = Date.from(instant); // Instant -> Date
  
  GregorianCalendar gregorianCalendar = new GregorianCalendar();
  ZonedDateTime zonedDateTime = gregorianCalendar.toInstant().atZone(ZoneId.systemDefault()); // GregorianCalendar -> ZonedDateTime
  // ZonedDateTime에는 Instant, LocalDate, LocalDateTime 등 변환할 수 있는 메소드를 제공
  GregorianCalendar from = GregorianCalendar.from(zonedDateTime); // ZonedDateTime -> GregorianCalendar
  
  ZoneId zoneId = TimeZone.getTimeZone("PST").toZoneId(); // TimeZone -> ZoneId
  TimeZone timeZone = TimeZone.getTimeZone(zoneId); // ZoneId -> TimeZone
  ```
  
<br>

### 🙋‍♂️ 내년 생일까지 몇일 남았는지는 어떻게 구하나요?

`ChronoUnit.DAYS.between(now, future)`을 통해 구합니다.

```java
LocalDate now = LocalDate.now(); // 2022-08-25
LocalDate futer = LocalDAte.of(2023, Month.JANUARY, 1); // 2023-01-01
long day = ChronoUnit.DAYS.between(now, future);
System.out.println(day); // 129
```
