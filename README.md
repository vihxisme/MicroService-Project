# 🚀 Microservices Project (Tìm hiểu về kiến trúc microservice và ứng dụng xây dựng website thương mại điện tử)

Đây là một kiến trúc Microservices được xây dựng để cung cấp các dịch vụ thương mại điện tử. Dự án này bao gồm một tập hợp các dịch vụ nhỏ, độc lập, giao tiếp với nhau thông qua các API nhẹ.

## 🌟 Tổng quan dự án

Dự án này được thiết kế để giải quyết nhu cầu về một nền tảng thương mại điện tử mạnh mẽ. Mỗi dịch vụ được triển khai độc lập, cho phép phát triển và triển khai nhanh chóng.

### Các thành phần chính:

- **Discovery Service (Eureka Server):** Đăng ký và khám phá các dịch vụ.
- **API Gateway:** Điểm vào duy nhất cho tất cả các yêu cầu từ client, xử lý định tuyến, bảo mật, v.v.
- **Các Microservices lõi:** Cung cấp các chức năng nghiệp vụ cụ thể.
- **Monitoring/Admin:** Giám sát sức khỏe và hiệu suất của các dịch vụ.

## 📦 Kiến trúc Microservices

Dự án tuân theo kiến trúc Microservices, nơi mỗi dịch vụ được đóng gói độc lập và giao tiếp thông qua RESTful APIs hoặc Message Queues (nếu có).

### Các Microservices chính:

Dưới đây là danh sách các Microservices có trong dự án và vai trò của chúng:

1.  **`Eureka-Server`**: Dịch vụ đăng ký và khám phá, cho phép các microservices khác tự động tìm thấy nhau.
2.  **`about-service`**: Dịch vụ quản lý các thông tin của cửa hàng(vd: banner, store, thông tin liên hệ của cửa hàng,...)
3.  **`api-composition`**: Tổng hợp dữ liệu từ nhiều microservices khác để cung cấp một API phức tạp hơn cho client.
4.  **`api-gateway`**: Cổng API trung tâm, định tuyến yêu cầu từ client đến các microservices phù hợp, xử lý xác thực, ủy quyền và cân bằng tải.
5.  **`auth-service`**: Dịch vụ xác thực và ủy quyền người dùng. Quản lý đăng nhập, đăng ký và cấp phát token
6.  **`cart-service`**: Dịch vụ quản lý giỏ hàng của người dùng. Cho phép thêm/xóa sản phẩm vào giỏ hàng.
7.  **`customer-service`**: Dịch vụ quản lý thông tin khách hàng, bao gồm hồ sơ người dùng, địa chỉ, v.v.
8.  **`discount-service`**: Dịch vụ quản lý các chương trình giảm giá áp dụng chiết khấu.
9.  **`inventory-service`**: Dịch vụ quản lý tồn kho sản phẩm. Theo dõi số lượng sản phẩm có sẵn.
10. **`monitor-admin`**: Dịch vụ giám sát và quản lý các microservices khác, cung cấp thông tin về trạng thái và hiệu suất.
11. **`notification-service`**: Dịch vụ gửi thông báo cho người dùng (ví dụ: email, SMS, push notifications).
12. **`order-service`**: Dịch vụ quản lý các đơn đặt hàng của khách hàng, bao gồm tạo, cập nhật và theo dõi trạng thái đơn hàng.
13. **`payment-service`**: Dịch vụ xử lý các giao dịch thanh toán.
14. **`product-service`**: Dịch vụ quản lý thông tin sản phẩm, bao gồm danh mục, chi tiết sản phẩm, giá cả.

## 🛠️ Công nghệ sử dụng

- **Ngôn ngữ lập trình:** Java
- **Framework:** Spring Boot
- **Discovery Service:** Netflix Eureka
- **API Gateway:** Spring Cloud Gateway
- **Database:** MySQL, Redis
- **Message Broker:** RabbitMQ
- **Containerization:** Docker
- **Build Tool:** Maven
- **Version Control:** Git

## 🚀 Thiết lập và chạy dự án

### Yêu cầu tiên quyết:

- JDK (Java Development Kit) `21` (hoặc phiên bản bạn đang dùng)
- Maven `3.x`
- Docker Desktop (hoặc Docker Engine)
- MySQL, Redis, RabbitMQ

### Các bước khởi động:

1.  **Clone repository:**

    ```bash
    git clone https://github.com/vihxisme/MicroService-Project/
    cd your-repo
    ```

2.  **Cấu hình Database/Message Broker:**
    Đảm bảo các dịch vụ database và message broker (nếu có) của bạn đang chạy và được cấu hình đúng trong file `application.properties` hoặc `application.yml` của từng microservice.

3.  **Chạy với Docker Compose (khuyến nghị):**
    Đây là cách dễ nhất để chạy tất cả các dịch vụ cùng lúc.

    ```bash
    docker-compose up --build
    ```

    Lệnh này sẽ xây dựng lại các Docker image và khởi động tất cả các container được định nghĩa trong `docker-compose.yml`.

    Để chạy ở chế độ nền:

    ```bash
    docker-compose up --build -d
    ```

4.  **Chạy từng Microservice độc lập (nếu cần):**
    Nếu bạn muốn phát triển hoặc debug một dịch vụ cụ thể:
    - Mở từng thư mục dịch vụ (ví dụ: `cd eureka-server`).
    - Sử dụng Maven để đóng gói và chạy:
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```
    - Lặp lại cho các dịch vụ khác theo thứ tự phụ thuộc (ví dụ: Eureka Server -> API Gateway -> các dịch vụ khác).

### Endpoint chính:

- **Eureka Dashboard:** `http://localhost:8761` (hoặc cổng cấu hình)
- **API Gateway:** `http://localhost:8888` (hoặc cổng cấu hình)
  - Bạn có thể truy cập các dịch vụ thông qua Gateway, ví dụ: `http://localhost:8888/products/all`, `http://localhost:8888/customers/{id}` (thay đổi theo API của bạn).

## 🤝 Đóng góp

Đóng góp của bạn luôn được hoan nghênh! Vui lòng đọc `CONTRIBUTING.md` (nếu có) hoặc làm theo các bước sau:

1.  Fork repository này.
2.  Tạo một branch mới (`git checkout -b feature/AmazingFeature`).
3.  Thực hiện thay đổi của bạn.
4.  Commit thay đổi (`git commit -m 'Add some AmazingFeature'`).
5.  Push lên branch (`git push origin feature/AmazingFeature`).
6.  Mở một Pull Request.

## 📄 Giấy phép

Dự án này được cấp phép theo (chọn giấy phép của bạn, ví dụ: MIT License). Xem file `LICENSE` để biết thêm chi tiết.

## 📧 Liên hệ

- `NVV` - vihxisme
- https://github.com/vihxisme/

---
