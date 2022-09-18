# Metaspace
JVM의 여러 메모리 영역 중에 PermGen 메모리 영역이 없어지고, Metaspace 영역이 생김

- PermGen(Permanent Generation)
  - 클래스와 메소드의 메타데이터를 담는 곳 (클래스 이름, 스태틱 필드 등)
  - Heap 영역에 속함
  - 기본값으로 제한된 크기를 가지고 있음
    - 크기가 고정됐으므로 데이터가 넘치면 에러 발생함
    - 에러 발생 시, 어디선가 동적으로 클래스 생성되고있는 것이므로 최대 사이즈를 변경한다 해결되진 않음. (e.g. 클래스나 인터페이스의 프록시를 동적 생성하는 스프리 AOP)
  - PermGen 초기 사이즈 설정 : `-XX:PermSize=N`
  - PermGen 최대 사이즈 설정 : `-XX:MaxPermSize=N`
- Metaspace
  - 클래스 메타데이터를 담는 곳
  - Heap 영역이 아닌, Native 메모리 영역에 속함
    - 다만, Perm 영역에 존재하던 Static Object는 Heap 영역으로 옮겨져서 GC의 대상이 최대한 될 수 있도록 함
  - 기본값으로 제한된 크기를 가지고 있지 않음
    - 필요한 만큼 계속 늘어남
    - OS가 자동으로 크기 조절
  - 자바 8부터는 PermGen 관련 java 옵션은 무시
  - Metaspace 초기 사이즈 설정 : `-XX:MetaspaceSize=N`
  - Metaspace 최대 사이즈 설정 : `-XX:MaxMetaspaceSize=N`
    - 설정 안하면 서버 메모리를 다 잡아먹으므로 매우 위험함
    - 제일 좋은 것은 모니터링하면서 적절한 값을 찾아 MAX값 설정하는 것


참고
- [기계인간 블로그](https://johngrib.github.io/wiki/java8-why-permgen-removed/)
- 백기선님의 더 자바, Java8 강의
- http://mail.openjdk.java.net/pipermail/hotspot-dev/2012-September/006679.html
- https://m.post.naver.com/viewer/postView.nhn?volumeNo=23726161&memberNo=3673
3075
- https://m.post.naver.com/viewer/postView.nhn?volumeNo=24042502&memberNo=3673
3075
- https://dzone.com/articles/java-8-permgen-metaspace
