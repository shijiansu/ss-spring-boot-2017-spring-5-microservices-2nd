FROM openjdk:13-jdk-alpine

RUN apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring \
  && mkdir /app && chown spring:spring /app
USER spring
COPY --chown=spring:spring target/book.jar /app

COPY --chown=spring:spring docker/docker-entrypoint.sh /app
RUN chmod +x /app/docker-entrypoint.sh

EXPOSE 8080

# ENTRYPOINT ["/app/docker-entrypoint.sh"]
# it converts to "/bin/sh -c /app/docker-entrypoint.sh"
# error shows "/bin/sh: /app/docker-entrypoint.sh: not found"
ENTRYPOINT ["sh", "/app/docker-entrypoint.sh"]
