FROM eclipse-temurin:21.0.2_13-jdk-alpine

# Set thư mục làm việc trong container
WORKDIR /app

# Copy file .jar vào container
COPY target/about-0.0.1-SNAPSHOT.jar app.jar

# Mở port 8761 (hoặc port mà ứng dụng Spring Boot của bạn đang sử dụng)
EXPOSE 9999

# Cấu hình lệnh chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]