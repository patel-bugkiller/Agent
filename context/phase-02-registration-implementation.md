# Phase 02: Registration Implementation & Database Setup

## Purpose
This phase documents the implementation of the full-stack user registration subsystem, relational database persistence with MySQL, BCrypt password hashing, and single-origin static file hosting. It represents all work conducted directly after the initial project boilerplate commit (`7aecbfec44484a9881ed79e49314956ec6de2f2f`).

---

## Starting State
- **Baseline Commit:** `7aecbfec44484a9881ed79e49314956ec6de2f2f` (`commit (amend): boiler plate`).
- **Initial Codebase State:**
  - Java application configured with Gradle (`build.gradle`, `settings.gradle`, `gradlew`).
  - Minimal entry point `com.example.app.Main` containing a dummy `getGreeting()` method.
  - Minimal test in `MainTest.java`.
  - Basic frontend skeletons in `frontend/index.html`, `frontend/css/style.css`, and `frontend/js/script.js`.
  - Zero database connectivity, no persistence schema, no HTTP server, and no authentication or hashing libraries.
- **Constraints Maintained:**
  - Framework-free: Plain Java without Spring, Spring Boot, Hibernate, Jersey, Express, React, Angular, or Vue.
  - Standard Java/JDK APIs (`com.sun.net.httpserver.*`, `java.sql.*`).
  - Minimal dependencies: Only `mysql-connector-java` (JDBC driver) and `jbcrypt` (password hashing) added to runtime dependencies.

---

## Final Outcome
The registration feature is fully functional end-to-end:
1. **Relational Persistence (MySQL):** Database `web_app` with table `users` containing `phone_number`, `email`, `username`, `password` (hashed), and `created_at`.
2. **JDBC Data Access:** Centralized connection factory in `DatabaseConfig.java` and repository operations in `UserRepository.java` using parameterized `PreparedStatement`.
3. **Password Security:** One-way salting and hashing via `BCrypt.hashpw(password, BCrypt.gensalt())`.
4. **Registration Endpoint:** Native HTTP server endpoint at `POST /api/register` with URL-encoded form parsing, CORS support, and comprehensive HTTP status codes (`201 Created`, `400 Bad Request`, `409 Conflict`, `500 Internal Server Error`).
5. **Frontend Registration UI:** HTML form with input fields for phone number, email, username, and password, styled with responsive CSS and connected via JavaScript Fetch API.
6. **Single-Origin Static File Server:** Java HTTP server handles static assets (`.html`, `.css`, `.js`) via `StaticFileHandler.java` on port 8080, preventing CORS and protocol issues.

---

## Implementation Journey

The registration system was built using a guided, phase-by-phase test-driven development workflow:

### 1. Database Schema & Dependency Setup
- Created `src/main/resources/schema.sql` defining `web_app` database and `users` table with auto-increment primary key and `UNIQUE` constraints on `email` and `username`.
- Added runtime dependencies to `build.gradle`:
  - `mysql:mysql-connector-java:8.0.33`
  - `org.mindrot:jbcrypt:0.4`
- Created `DatabaseConfig.java` providing `getConnection()` with environment variable fallbacks (`DB_URL`, `DB_USER`, `DB_PASS`).

### 2. Domain Model & Data Access Layer
- Created `User.java` domain model with fields: `phoneNumber`, `email`, `username`, and `password`.
- Created `UserRepository.java` implementing `save(User)` via `INSERT INTO users (phone_number, email, username, password) VALUES (?, ?, ?, ?)`.
- Created `UserRepositoryTest.java` validating the user model getters and database repository wiring.

### 3. Registration Business Logic & Password Hashing (TDD)
- **RED Step:** Created `RegistrationServiceTest.java` with test cases:
  - `testValidationFailsForEmptyEmail`: Throws `IllegalArgumentException("Email cannot be empty")`.
  - `testValidationFailsForShortPassword`: Throws `IllegalArgumentException("Password must be at least 6 characters")`.
  - `testValidationFailsForEmptyPhoneNumber`: Throws `IllegalArgumentException("Phone number cannot be empty")`.
  - Compilation failed as expected (`cannot find symbol class RegistrationService`).
- **GREEN Step:** Created `RegistrationService.java`.
  - Validates non-empty phone number, valid email with `@`, non-empty username, and password length >= 6.
  - Automatically salts and hashes the password using `BCrypt.hashpw(password, BCrypt.gensalt())`.
  - Instantiates `User` and invokes `userRepository.save(user)`.
  - All unit tests passed (`BUILD SUCCESSFUL`).

### 4. HTTP Server & Registration Handler
- Created `RegistrationHandler.java` implementing JDK `HttpHandler`.
  - Handles `OPTIONS` requests for CORS preflight.
  - Intercepts `POST` requests and parses `application/x-www-form-urlencoded` payloads using `URLDecoder`.
  - Delegates registration to `RegistrationService`.
  - Catches `IllegalArgumentException` and returns `400 Bad Request`.
  - Detects duplicate entry errors (`SQLIntegrityConstraintViolationException`) and returns `409 Conflict`.
  - Returns `201 Created` with message `"Registration successful"`.
- Updated `Main.java` to start `HttpServer` on port 8080 and bind context `/api/register`.

### 5. Frontend Registration Form & JavaScript Fetch
- Updated `frontend/index.html` with registration fields: `phoneNumber`, `email`, `username`, `password`, submit button, and a dynamic `#messageBox`.
- Updated `frontend/css/style.css` styling the form container, inputs, buttons, and alert message classes (`.message-box.success`, `.message-box.error`).
- Updated `frontend/js/script.js` to intercept form submission via `preventDefault()`, perform client validation, construct `URLSearchParams`, send asynchronous `fetch('/api/register')`, and render server response messages in the UI.

### 6. Single-Host Static File Server (`StaticFileHandler`)
- To eliminate cross-origin resource sharing (CORS) complications and `file://` protocol restrictions, created `StaticFileHandler.java`.
- Configured MIME type mapping (`text/html`, `text/css`, `application/javascript`) and file delivery via `Files.copy()`.
- Registered `/` in `Main.java` to serve static files from `frontend/`, allowing frontend and backend to operate on the same origin (`http://localhost:8080`).

---

## Repository Changes

```text
Project Root/
├── build.gradle                                            (MODIFIED: added MySQL and BCrypt dependencies)
├── src/main/resources/
│   └── schema.sql                                          (NEW: MySQL DDL schema for web_app.users)
├── src/main/java/com/example/app/
│   ├── DatabaseConfig.java                                 (NEW: JDBC connection factory)
│   ├── User.java                                           (NEW: User entity domain model)
│   ├── UserRepository.java                                 (NEW: JDBC repository with save method)
│   ├── RegistrationService.java                            (NEW: Registration validation & BCrypt hashing)
│   ├── RegistrationHandler.java                            (NEW: HTTP handler for /api/register)
│   ├── StaticFileHandler.java                              (NEW: HTTP file server for frontend assets)
│   └── Main.java                                           (MODIFIED: started HttpServer, mapped / and /api/register)
├── src/test/java/com/example/app/
│   ├── UserRepositoryTest.java                             (NEW: tests for user model and repository save)
│   └── RegistrationServiceTest.java                        (NEW: tests for validation rules)
└── frontend/
    ├── css/style.css                                       (MODIFIED: styles for registration card and alerts)
    ├── js/script.js                                        (MODIFIED: form submit, validation, fetch to /api/register)
    └── index.html                                          (MODIFIED: registration form fields and message area)
```

---

## Important Code and Configuration

### 1. Database DDL (`src/main/resources/schema.sql`)
```sql
CREATE DATABASE IF NOT EXISTS web_app;
USE web_app;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. Registration Service (`src/main/java/com/example/app/RegistrationService.java`)
```java
public void registerUser(String phoneNumber, String email, String username, String password) {
    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
        throw new IllegalArgumentException("Phone number cannot be empty");
    }
    if (email == null || email.trim().isEmpty()) {
        throw new IllegalArgumentException("Email cannot be empty");
    }
    if (!email.contains("@")) {
        throw new IllegalArgumentException("Invalid email format");
    }
    if (username == null || username.trim().isEmpty()) {
        throw new IllegalArgumentException("Username cannot be empty");
    }
    if (password == null || password.trim().length() < 6) {
        throw new IllegalArgumentException("Password must be at least 6 characters");
    }

    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    User user = new User(phoneNumber, email, username, hashedPassword);
    userRepository.save(user);
}
```

### 3. API Contract (`POST /api/register`)
- **Headers:** `Content-Type: application/x-www-form-urlencoded`
- **Request Parameters:** `phone_number`, `email`, `username`, `password`
- **Response Codes:**
  - `201 Created`: Registration successful.
  - `400 Bad Request`: Validation failure message (e.g. "Password must be at least 6 characters").
  - `409 Conflict`: "Email or Username already exists".
  - `500 Internal Server Error`: Database connection error or unexpected failure.

---

## Commands and Operations
- **Run Tests:**
  ```bash
  bash gradlew test
  ```
- **Start Web Application Server:**
  ```bash
  bash gradlew run
  ```
  *(Output: `Server started on http://localhost:8080`)*
- **Database Initialization via MySQL CLI:**
  ```sql
  mysql -u root -p
  source src/main/resources/schema.sql;
  ```

---

## Verification
- **Unit Tests:** `RegistrationServiceTest` and `UserRepositoryTest` pass with 0 errors.
- **End-to-End Verification:** Verified via browser submission on `http://localhost:8080/`. Data entered on the HTML form traverses the fetch API, is validated by Java, hashed via BCrypt, and persisted in MySQL. Success message is rendered on the UI.
- **Database Inspection:** Verified in MySQL CLI and phpMyAdmin via `SELECT * FROM users;`. Confirmed passwords stored as BCrypt hashes (`$2a$10$...`).

---

## Failures and Fixes Encountered
1. **Typo in `UserRepositoryTest.java`:**
   - *Failure:* `assertEquals(1, "John Doe", user.getName())` caused assertion failure.
   - *Fix:* Removed extraneous `1,` argument.
2. **Field Requirement Change (Name to Phone Number):**
   - *Failure:* Initial schema used `name`; user requested `phoneNumber`.
   - *Fix:* Updated schema, model, repository, service, tests, and frontend form to use `phoneNumber` / `phone_number`.
3. **Windows Environment Variable `JAVA_HOME` Misalignment:**
   - *Failure:* `ERROR: JAVA_HOME is set to an invalid directory: C:\Program Files\Eclipse Adoptium\jdk-21...`.
   - *Fix:* Installed Eclipse Adoptium Temurin OpenJDK 21, set `JAVA_HOME` in Windows System Environment Variables, restarted terminal.

---

## Decisions and Rationale
1. **Zero Frameworks:** Strictly utilized standard JDK APIs (`HttpServer`, JDBC) to ensure zero dependency bloat and high portability.
2. **URL-Encoded Payload Format:** Chose `application/x-www-form-urlencoded` over JSON to avoid introducing third-party parsing dependencies (e.g., Jackson or Gson).
3. **Single-Origin Architecture:** Embedding `StaticFileHandler` in Java eliminated CORS preflight issues and browser blocking when opening local HTML files.
4. **BCrypt Password Security:** Used standard `jbcrypt` with unique per-password salt to prevent rainbow table attacks.

---

## Commit Linkage
- **Commits Representing This Phase:**
  - `76b6cc4f4888806fdf651fb1578ff82ea8d3798e`: `commit: registration`
  - `8286d53df83cf6d971bf335e0d2ee2dc8e595480`: `commit (amend): registration connected to backend`
  - `fe66a13ebf352baffda2989f29fe9748e0bb21b3`: `commit: server same host`
- **Branch:** `master`.

---

## Resume From Here
- **Next Phase:** User Authentication (Login Page & Login API).
- **Target Implementation:** Extend `UserRepository` with email query, create `LoginService` with BCrypt password verification, expose `/api/login`, and build `login.html`.
