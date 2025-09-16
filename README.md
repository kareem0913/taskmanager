# TaskManager

## About

**taskmanager** — backend for a task management application with clean code architecture and RESTful APIs.


**Technologies:** Java, Spring Boot, Spring Data JPA, Hibernate, Liquibase, Spring Security, PostgreSQL

### Project Overview

* Built task management backend with clean code architecture and RESTful APIs.
* Task management with categories, priorities, and statuses.
* Database migrations with Liquibase.
* RESTful API with Swagger UI for interactive docs.

---

## Features

* User authentication & authorization (Spring Security With JWT).
* Database migrations with Liquibase.
* RESTful API with Swagger UI for interactive docs.

---
## API Testing with Postman
We provide a Postman collection to help you quickly test our API endpoints. This collection contains pre-configured requests for all available operations.

<a href="https://raw.githubusercontent.com/kareem0913/taskmanager/main/taskmanager.postman_collection.json" download>
  <img src="https://img.icons8.com/color/48/000000/postman.png" alt="Postman" width="20"/>
  Download Postman Collection
</a>
---

## Quick Start (using Docker Hub)

This project provides a pre-built image on Docker Hub. To run the app on a new machine:

1. Download the `docker-compose.yml` from the repo:

```bash
wget https://raw.githubusercontent.com/kareem0913/taskmanager/main/docker-compose.yml
```

2. Start the services with Docker Compose:

```bash
docker compose up
```

3. Open the Swagger UI to explore the API:

```
http://localhost:8080/api/swagger-ui/index.html
```
---

## Running locally (build from source)

If you prefer to build the image locally and run it with Compose (no Docker Hub required):

1. Build the application image from the project root (where the `Dockerfile` exists):

```bash
docker build -t kareemzaher/taskmanager-app:latest .
```

2. Start services (build step not required now because image exists locally):

```bash
docker compose up -d
```

---

## Database migrations

Liquibase is used to manage DB schema changes. Migrations are executed when the application starts (depending on your Spring Boot configuration). Check the `src/main/resources/db/changelog` folder for changeSets.

---

## API Docs

Swagger UI is available at:

```
http://localhost:8080/api/swagger-ui/index.html
```
---

## Contact

Kareem — <kareem.345@outlook.com>
