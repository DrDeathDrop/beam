# Beam

A mock of Steam — a small game-store web app built for practice. You can browse a
store of games, buy them (directly or through a cart), keep a library, request
refunds, and manage everything from an admin dashboard.

## What's in it

- Browse the store and view game details
- Register / log in (JWT auth)
- Add games to a cart and check out, or buy a single game directly
- Library of owned games with refunds
- Admin dashboard for managing games and users

## Tech stack

**Backend**
- Java 25
- Spring Boot 4 (Web, Data JPA, Security)
- JWT auth (jjwt)
- MapStruct + Lombok
- MySQL
- Maven

**Frontend**
- Angular 21 (standalone components, signals)
- TypeScript
- Plain CSS

## Requirements

- JDK 25
- Node.js 20+ and npm
- A running MySQL instance

## Running it

### 1. Database

Create the database (tables are created automatically on first run):

```sql
CREATE DATABASE beam;
```

The default connection is in `src/main/resources/application.properties`
(`jdbc:mysql://localhost:3306/beam`, user `root`, no password) — edit it if yours differs.

### 2. Backend

From the project root:

```bash
./mvnw spring-boot:run
```

Serves on `http://localhost:8080`. Requires JDK 25 — building on an older JDK fails
because of the Lombok / annotation-processor versions.

Run the tests with:

```bash
./mvnw test
```

### 3. Frontend

```bash
cd frontend/frontend
npm install
npm start
```

Serves on `http://localhost:4200` and proxies `/api` requests to the backend on `:8080`.

## Notes

- New accounts are regular users. To make one an admin (for the dashboard), update it
  directly in the database:

  ```sql
  UPDATE users SET role = 'ADMIN' WHERE email = 'you@example.com';
  ```

- The store is empty until games are added — do that from the admin dashboard.
