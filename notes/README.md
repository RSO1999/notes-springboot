<!-- README.md -->
# Notes (tiny Spring Boot backend)

This is a small, intentionally boring Spring Boot backend for pairing-interview practice.

It aims to feel like a newly-started team project: readable layering, a couple endpoints, real persistence, and multiple unfinished features to implement live.

## What the app currently does

- Stores **Notes** in an in-memory **H2** database using Spring Data JPA.
- Seeds **3–5 notes** on startup for quick testing.
- Supports:
    - `POST /notes` (create)
    - `GET /notes` (list all)

Notes have:
- `id` (generated)
- `title`
- `content`
- `createdAt` (automatically set)
- `updatedAt` (currently only changes when updates are implemented)

JPA nuance included: **optimistic locking** via `@Version`.

## How to run (high level)

- Run the Spring Boot app (from your IDE or `./gradlew bootRun`).
- The API will be available on `http://localhost:8080`.

## H2 console

If you enabled the H2 console via `application.properties`, you can typically use:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`

(Exact values depend on your `application.properties`.)

## Current endpoints

### Create a note
- `POST /notes`
- Body (JSON):
  ```json
  { "title": "Hello", "content": "World" }
