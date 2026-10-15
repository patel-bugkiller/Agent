# Phase 01: Project Boilerplate & Setup

## Purpose
This phase was created to establish the initial project boilerplate and structure for a web application using a framework-free Java backend and plain HTML/CSS/JS frontend. The goal was to provide a solid foundation for adding an HTTP server, authentication, and MySQL database access in later steps.

## Starting State
- **Prior state:** Empty directory.
- **Requirements:** 
  - Frontend: HTML, CSS, JavaScript.
  - Backend: Java.
  - Database: MySQL (planned).
  - Build tool: Gradle.
- **Constraints:** 
  - No frameworks (no Spring, Spring Boot, React, Angular, Vue, Hibernate, ORM, etc.).
  - Use plain Java and standard JDK APIs.
  - Keep dependencies to an absolute minimum.
  - No authentication or database functionality implemented yet.

## Final Outcome
The project successfully established the boilerplate:
- **Build tool:** Gradle is configured with the `java` and `application` plugins.
- **Entry point:** A minimal Java `Main` class exists that returns and prints a greeting.
- **Testing:** JUnit 5 is set up and a simple unit test for the `Main` class is passing.
- **Frontend:** A basic `index.html` structure is connected to a stylesheet (`style.css`) and script (`script.js`).

## Implementation Journey
The implementation followed a guided, manual test-driven development (TDD) approach:
1. **Phase 1:** `build.gradle` and `settings.gradle` were created. The user encountered issues with Gradle not being installed globally. A `winget` installation failed, so the Gradle wrapper files were downloaded directly from the Gradle GitHub repository (v8.10.0) via PowerShell `Invoke-WebRequest`.
2. **Phase 2:** TDD RED step. A test `MainTest.java` was created expecting a `Main` class. The user encountered a parsing error due to saving an incomplete file, which was corrected. The expected `cannot find symbol` failure was then successfully generated.
3. **Phase 3:** TDD GREEN step. `Main.java` was created. Tests passed and `./gradlew run` successfully output "Application is running".
4. **Phase 4:** Frontend boilerplate. The user created `index.html`, `style.css`, and `script.js` in the `frontend/` directory.

## Repository Changes
The following structure was created:
```text
project-root/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/java/com/example/app/Main.java
│   └── test/java/com/example/app/MainTest.java
└── frontend/
    ├── css/style.css
    ├── js/script.js
    └── index.html
```

## Important Code and Configuration
- **Application Main Class:** `com.example.app.Main` (defined in `build.gradle` as `mainClass`).
- **Java Configuration:** Default Java version on the system. No specific toolchain is enforced.
- **Frontend Entry:** `frontend/index.html`

## Dependencies
Only JUnit is used, kept to a minimum as requested:
- `org.junit.jupiter:junit-jupiter-api:5.10.0` (testImplementation)
- `org.junit.jupiter:junit-jupiter-engine:5.10.0` (testRuntimeOnly)

## Commands and Operations
- **Wrapper Setup:** `Invoke-WebRequest` was used in PowerShell to download `gradlew`, `gradlew.bat`, `gradle-wrapper.jar`, and `gradle-wrapper.properties` directly from the `gradle/gradle` GitHub repository at `v8.10.0`.
- **Test:** `./gradlew test` (or `.\gradlew.bat test` in PowerShell).
- **Run:** `./gradlew run` (or `.\gradlew.bat run` in PowerShell).

## Verification
- **Build & Test:** `gradlew test` succeeded.
- **Application Startup:** `gradlew run` succeeded, outputting "Application is running".
- **Frontend:** Manually verified that `index.html` renders properly without errors (as executed by the user).

## Failures and Fixes
- **Failure:** `gradle` command not found.
  - **Fix:** Provided PowerShell commands to fetch the Gradle wrapper files natively, bypassing a global installation.
- **Failure:** `winget install Gradle.Gradle` returned "No package found matching input criteria".
  - **Fix:** Used the direct wrapper download method mentioned above.
- **Failure:** `error: reached end of file while parsing` in `MainTest.java`.
  - **Cause:** Incomplete copy-paste/save by the user.
  - **Fix:** Instructed the user to ensure the file was fully saved. Fixed successfully.
- **Failure:** `bash: gradle: command not found` at the end of the phase.
  - **Cause:** User switched to Git Bash and tried using the global `gradle` command.
  - **Fix:** Reminded the user to use `./gradlew` instead.

## Decisions and Rationale
- **Framework-free Architecture:** Strictly enforced. No Spring Boot or frontend frameworks were used.
- **Gradle Wrapper:** Decided to download the wrapper directly via HTTP rather than block the user on a global Gradle installation or package manager failure.
- **Package Convention:** Adopted `com.example.app` as a standard, simple Java package hierarchy.

## Final Technical State
- The Java application compiles and executes via the Gradle `application` plugin.
- A single test passes.
- The project is fully independent of any global Gradle installation due to the wrapper.
- There is no `.git` repository initialized yet.

## Deferred / Out of Scope
The following were explicitly requested but intentionally **not** implemented in this phase:
- MySQL database access and tables.
- HTTP server / API.
- User authentication (registration, login, logout, sessions, password hashing).
- Roles or security mechanisms.

## Known Risks or Open Questions
- A local web server to serve the frontend files does not exist yet; currently, `index.html` is meant to be opened directly via the filesystem, or served in future phases.

## Commit Linkage
- **Status:** This phase has not been committed yet. The repository does not currently contain a `.git` directory.

## Resume From Here
- **Next Phase:** The user stated the next planned feature is **registration**. 
- **Action:** A future agent should begin by establishing the HTTP server and MySQL database connection necessary to support the registration feature. Ensure you continue using plain Java with no frameworks.
