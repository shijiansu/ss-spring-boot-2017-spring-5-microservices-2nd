FROM openjdk:13-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring \
  && mkdir /app && chown spring:spring /app
USER spring
COPY --chown=spring:spring target/fares.jar /app

EXPOSE 8082

ENTRYPOINT ["java","-jar","/app/fares.jar"]
