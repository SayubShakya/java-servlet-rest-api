# User Management REST API (Java Servlets)

This project is a **Java Servlet-based REST API** for managing users. It allows clients to perform basic CRUD (Create, Read, Update, Delete) operations using standard HTTP methods. The API is built using **JSP/Servlet**, **JDBC**, and connects to a **MySQL** database.

---

## 📌 Features / Postman Request

- Create new user (`POST`): [http://localhost:8080/users](http://localhost:8080/users)
- Read all users (`GET`): [http://localhost:8080/users](http://localhost:8080/users)
- Read single user by ID (`GET`): [http://localhost:8080/users/27](http://localhost:8080/users/27)
- Update user by ID (`PUT`): [http://localhost:8080/users/27](http://localhost:8080/users/27)
- Delete user by ID (`DELETE`): [http://localhost:8080/users/27](http://localhost:8080/users/27)
- JSON-based request and response
- Follows RESTful principles

---

## 🛠️ Tech Stack

- Java (Servlets, JSP)
- JDBC
- MySQL
- Maven
- JSON
- Jetty Server

---

## 🚀 Getting Started

### Prerequisites

- Java JDK
- Apache Maven
- MySQL Server
- Postman
- Jetty Server (configured via Maven plugin)

### Clone & Run

1. **Clone the repository:**

   ```bash
   git clone https://github.com/SayubShakya/java-servlet-rest-api.git
   cd java-servlet-rest-api
