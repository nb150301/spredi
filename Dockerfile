#FROM eclipse-temurin:17-jdk-alpine as build
#WORKDIR /app
#
#COPY build.gradle settings.gradle gradlew ./
#COPY gradle /app/gradle
#
#RUN ./mvnw package || return 0
#COPY . .
#RUN ./mvnw package -x test
#
## Nếu bạn đang chạy Docker trên Windows thì thêm dòng này
## RUN dos2unix gradlew
#
#RUN ./mvnw package -x test
#
#FROM eclipse-temurin:17-jre-alpine as production
#WORKDIR /app
#COPY --from=build target/spring-redis-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]


FROM  eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY . .

RUN ./mvnw package
FROM eclipse-temurin:21-jre-jammy as production
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]


#FROM eclipse-temurin:21-jre-jammy
#
#WORKDIR /app
#
#COPY /target/*.jar app.jar
#
#ENTRYPOINT ["java","-jar","app.jar"]