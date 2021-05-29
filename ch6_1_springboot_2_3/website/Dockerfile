FROM openjdk:13-jdk-alpine

RUN apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring \
  && mkdir /app && chown spring:spring /app
USER spring
COPY --chown=spring:spring target/website.jar /app

COPY --chown=spring:spring docker/docker-entrypoint.sh /app
RUN chmod +x /app/docker-entrypoint.sh

EXPOSE 8001

ENTRYPOINT ["sh", "/app/docker-entrypoint.sh"]
