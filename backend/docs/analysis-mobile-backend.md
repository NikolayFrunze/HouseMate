# HouseMate – Analysis: Mobile Client and REST API

> Owner: Raul Fariñas Vazquez (Mobile Client, REST API)
> Scope of this section: Android app and Spring Boot backend only.
> The Web Application section (Anushree Shukla) and Data Management (Daniel Dobrianskyi) are **not** covered here.

---

## Members (my part)

| Name | Role | Contribution |
|---|---|---|
| Raul Fariñas Vazquez | Mobile Client, REST API | MoSCoW analysis, SMART goals |

---

## Objective

Deliver a working Android app in which users register, log in and reach their home screen, backed by a Spring Boot REST API that stores users in PostgreSQL with hashed passwords.

- Build the Android app in **Java**
  - Login screen
  - Registration screen
  - User home screen (house and expenses)
- Build the **Spring Boot** REST API
  - User registration – `POST /users`
  - User login – `POST /users/login`
  - House – `GET /users/{userId}/house`, `POST /users/{userId}/house`
  - Expenses – `GET /users/{userId}/expenses`, `POST /users/{userId}/expenses`
- Connect the app to the API with **Retrofit 2.11**
- Store users in **PostgreSQL** with **BCrypt** password hashes
- More features will be added as the app grows

---

## Project Design

### SMART Goals

- **Specific**
  - Functioning app that registers and logs in users against a Spring Boot API backed by PostgreSQL.
  - Users can create or join a house and register expenses that are split between its members.
- **Measurable**
  - Registration and login work end to end.
  - Every endpoint returns the correct HTTP status: `201` (created), `200` (ok), `400` (invalid input), `401` (wrong credentials), `409` (email already registered).
  - Demo data (5 users, 1 house, 4 expenses) loads and is visible in the app.
- **Achievable**
  - Use free, open-source tools only: Android Studio, Spring Boot, PostgreSQL.
  - Test the app with classmates and debug the issues they find.
- **Realistic**
  - Start with authentication, then add home features one at a time (house, then expenses).
- **Time-bound**
  - 6 weeks (shared project timeline). Current status: week 2 of 6.

### Project Phasing (Android + Backend)

| Phase | Activity | Status |
|---|---|---|
| Week 1: Planning and Design | Prototype of the app screens; diagram of the backend code; API contract; database entities (User, House, HouseMember, Expense) | Done |
| Week 2: Analyzing features | Registration and login (BCrypt, backend validation); house and expense endpoints with expense splitting; Android screens (login, registration, user, house, expenses) connected with Retrofit; demo data | Done |
| Week 3: Developing | To be defined | – |
| Week 4: Authenticating and Implementing | To be defined | – |
| Week 5: Testing | To be defined | – |
| Week 6: | To be defined | – |

---

## MoSCoW Method (Android + Backend)

- **Must Have**
  - User registration and login
  - Passwords stored hashed with BCrypt, never in plain text
  - Input validation on the backend (`@Valid` requests return `400` when invalid)
  - User home screen with house information and expense list
  - Expense splitting between house members
- **Should Have**
  - Session or token authentication after login (the current login returns the user only, without a token)
  - HTTPS between the app and the backend (the app currently calls `http://10.0.2.2:8080/`)
  - Logging and documentation of the API
- **Could Have**
  - To be defined
- **Won't Have**
  - To be defined

---

## Scope (Android + Backend)

**Within scope**
- User registration and login (Android + Spring Boot)
- User home screen
- REST API consumed by the Android app
- House and expense endpoints
- Home searching feature

**Not within scope**
- Web application (covered by its own section)
- IoT devices and data management (covered by their own sections)
- Payments or bank integrations

---

## Notes for the team

- This section covers the work done up to week 2. The plan for weeks 3 to 6 has not been defined yet.
- The HTTP status mapping (`409` for duplicated email, `401` for wrong credentials) should be confirmed in the controllers before the final submission.
