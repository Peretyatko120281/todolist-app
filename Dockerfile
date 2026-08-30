FROM amazoncorretto:17-alpine

# Создаем пользователя для безопасности (опционально)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Копируем JAR файл
COPY target/to-do-list-application-0.0.1-SNAPSHOT.jar app.jar

# Даем права пользователю
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]