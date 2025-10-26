# OpenJDK 17 slim 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 이미지에 레이블 추가
LABEL type="application"

# OpenTelemetry Java Agent 다운로드
RUN apt-get update && apt-get install -y wget && \
    wget -O /opt/opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# 작업 디렉토리 설정
WORKDIR /apps

# 애플리케이션 jar 파일을 컨테이너로 복사
COPY build/libs/*.jar /apps/app.jar
RUN mkdir -p /logs

# 애플리케이션이 사용할 포트 노출
EXPOSE 8080

# 애플리케이션 실행 (OpenTelemetry 활성화)
CMD ["java", "-javaagent:/opt/opentelemetry-javaagent.jar", "-jar", "app.jar"]