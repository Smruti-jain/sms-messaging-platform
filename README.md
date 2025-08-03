# SMS Platform 

## 📌 Overview 
This project is an **SMS Platform simulation** built with Spring Boot.  
It demonstrates:
- Modular design for Customer API, Kafka, DB API, Backend Scheduler, and Telecom API
- Kafka-based message queueing
- Scheduler-driven message processing
- REST-based Telecom simulation with success/failure responses

## 🏗 Architecture

Customer API → Kafka Producer → Kafka Topic
↓ ↑
DB Validation (Internal DB API) |
↓ |
Kafka Consumer → DB insert NEW messages
↓
Backend Scheduler picks NEW → calls Telecom API
Telecom API returns ACCEPTED/REJECTED → DB updates SENT/FAILED

## 🛢 Database
**Database Name**: `sms_platform`  
**Tables**:
- `users`
- `send_msg`

**Sample Users**:
| account_id | username | password  |
|------------|----------|-----------|
| 1001       | user1    | password1 |
| 1002       | user2    | password2 |

## 🔑 Configuration
- Copy `application-example.properties` → `application.properties`
- Update DB credentials & Kafka configs as per your environment

## 📦 Dependencies
This project uses the following key dependencies (defined in `pom.xml`):

### **Spring Boot Starters**
- `spring-boot-starter-web` → REST API support
- `spring-boot-starter-data-jpa` → Database access (Hibernate + JPA)
- `spring-boot-starter-validation` → Request validation (`@Valid`)
- `spring-boot-starter-test` → Unit & integration testing

### **Database**
- `mysql-connector-j` → MySQL JDBC driver

### **Kafka**
- `spring-kafka` → Kafka Producer/Consumer integration

### **Lombok**
- `lombok` → Reduces boilerplate (Getters, Setters, @Slf4j for logging) (Optional)

## 📂 Project Structure

com.telco.smsplatform
├── customer       # Customer API
├── kafka          # Kafka Producer & Consumer
├── db             # Internal DB API & Service
├── backend        # Backend Scheduler
├── telecom        # Telecom API
├── exception      # Global Exception Handler

## 🌐 APIs
1. Customer API
Request :
   GET /telco/sendmsg?username=user1&password=password1&mobile=9876543210&message=Hello
Success Response :
   STATUS: ACCEPTED~~RESPONSE_CODE: SUCCESS~~<ACK_ID>
Failure Response :
   STATUS: REJECTED~~RESPONSE_CODE: FAILURE

2. Telecom API (Called by Scheduler)
Request :
   GET /telco/smsc?account_id=1001&mobile=9876543210&message=Hello
Success Response :
   STATUS: ACCEPTED~~RESPONSE_CODE: SUCCESS~~<ACK_ID>
Failure Response :
   STATUS: REJECTED~~RESPONSE_CODE: FAILURE

## 🔄 Backend Scheduler
- Runs every 1 second
- Picks NEW messages from send_msg
- Sets status → INPROCESS
- Calls Telecom API
- Updates status → SENT or FAILED

## 🧪 Testing
Unit Tests
- Customer API
- Kafka Producer & Consumer
- Internal DB Service
- Telecom API
- Backend Scheduler

## 📝 Notes and Future Scope

- InternalDbController is present for microservice simulation (unused in monolith).
- Can call via RestTemplate if converted to separate microservice.
- All services can be separated later into independent modules.
