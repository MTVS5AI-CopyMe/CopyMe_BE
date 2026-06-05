# 1. 실행 환경으로 가볍고 보안이 뛰어난 Eclipse Temurin Java 21 JRE 이미지 사용
FROM eclipse-temurin:21-jre-alpine

# 2. 컨테이너 내부의 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 가벼운 bootJar 파일만 컨테이너 내부로 복사
# (copyme-0.0.1-SNAPSHOT.jar 같은 파일을 app.jar라는 이름으로 복사)
COPY build/libs/*-SNAPSHOT.jar app.jar

# 4. 컨테이너가 외부와 소통할 포트 지정 (스프링 기본 포트)
EXPOSE 8080

# 5. 컨테이너가 시작될 때 스프링 부트를 실행할 명령어 정의
# 개발자님이 사용하는 dev 프로필 설정을 기본값으로 넣어둡니다.
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]