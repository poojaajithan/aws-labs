# AWS Labs with Spring Boot 🚀

This project is a **Spring Boot lab collection** to practice and demonstrate real-world integration with **AWS cloud services**.  
It is structured for **interview preparation** and showcases best practices such as logging, exception handling, and clean code.

---

## ✅ Current Coverage

### 1. AWS S3 (File Storage)
- Upload files (multipart)
- Download files (direct + streaming for huge files)
- Centralized exception handling (`@ControllerAdvice`)
- Structured error responses (`ApiError`)
- Logging with SLF4J + Logback

---

## 🛠 Planned Modules (Upcoming)
- **DynamoDB** – CRUD APIs with Spring Data
- **SNS/SQS** – Messaging between microservices
- **Lambda Integration** – Event-driven processing
- **CloudWatch** – Centralized logging and monitoring
- **IAM Best Practices** – Fine-grained access control
- **VPC / Networking Concepts** – For system design interviews

---

## ⚙️ Tech Stack
- Java 17+
- Spring Boot 3
- AWS SDK v2
- Lombok (logging + boilerplate reduction)
- Logback (logging framework)

---

## 📡 API Endpoints (S3 Module)

### List Buckets
