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

Use these commands directly in your terminal:

```bash
# List Buckets
curl -X GET "http://localhost:8080/s3/buckets"

# Upload File
curl -X POST "http://localhost:8080/s3/upload" \
  -F "bucketName=my-bucket" \
  -F "file=@/path/to/local/file.txt"

# Download File
curl -X GET "http://localhost:8080/s3/download?bucketName=my-bucket&key=file.txt" \
  -o file.txt

# Stream Download (for huge files)
curl -X GET "http://localhost:8080/s3/download/stream?bucketName=my-bucket&key=bigfile.zip" \
  -o bigfile.zip


### List Buckets
