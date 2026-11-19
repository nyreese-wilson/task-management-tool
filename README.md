# Full-Stack Task Management Tool (Kanban Board)

A full-stack Kanban task management application inspired by Trello, built with **Java 26**, **Spring Boot**, **PostgreSQL**, and **React + TypeScript**. Designed with strict relational data integrity, layered backend architecture, DTO isolation, and automated unit testing.

---

## System Architecture

```text
+-----------------------+       REST / JSON        +-----------------------------------+

|   React + TypeScript  | <----------------------> |       Spring Boot REST API        |
|   (Vite Dev Server)   |    http://localhost:8080 |  Controller -> Service -> JPA Repo|
+-----------------------+                          +-----------------------------------+
                                                                     |
                                                              JDBC / Hibernate
                                                                     v
                                                   +-----------------------------------+

                                                   |        PostgreSQL Database        |
                                                   |   (Relational Foreign Key Schema) |
                                                   +-----------------------------------+
```

### Key Architectural Decisions
* **Layered Backend Architecture (`Controller → Service → Repository`):** Separates HTTP transport concerns from transactional business logic (`@Transactional`) and data persistence, enabling isolated unit testing with Mockito.
* **Immutable Java `record` DTOs:** Decouples the REST API contract from internal JPA entities, preventing circular JSON serialization (`Board → TaskList → Board`) and protecting database models from over-posting.
* **Lazy Fetching & Cascading Deletes:** Configured `@ManyToOne(fetch = FetchType.LAZY)` to prevent `N+1` / unnecessary join queries, paired with `CascadeType.ALL` and `orphanRemoval = true` to maintain referential integrity when boards or lists are removed.
* **Position-Based Ordering:** Both `lists` and `tasks` maintain explicit `position` integer columns (`@OrderBy("position ASC")`) rather than relying on non-deterministic database insertion order.
* **Centralized Exception Handling (`@RestControllerAdvice`):** Translates domain exceptions (`ResourceNotFoundException`) and bean validation failures (`@Valid`) into consistent HTTP `404` and `400` JSON responses.

---

## Database Schema (ERD)

```text
users (id, email, name, created_at)
  └──< workspaces (id, name, owner_id [FK])
         └──< boards (id, title, workspace_id [FK])
                └──< lists (id, title, position, board_id [FK])
                       └──< tasks (id, title, description, status, position, list_id [FK], assignee_id [FK])
```

* **`TaskStatus` Enum:** Enforces strict state values (`TODO`, `IN_PROGRESS`, `DONE`) at both the Java application layer and PostgreSQL persistence layer.

---

## REST API Reference

| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/boards` | Retrieve all boards | — |
| `GET` | `/api/boards/{boardId}` | Retrieve a board with nested lists and tasks | — |
| `POST` | `/api/boards` | Create a new board in a workspace | `{ "title": "...", "workspaceId": 1 }` |
| `POST` | `/api/boards/{boardId}/lists` | Add a new column/list to a board | `{ "title": "In Progress" }` |
| `POST` | `/api/lists/{listId}/tasks` | Create a new task card in a list | `{ "title": "...", "description": "..." }` |
| `PATCH` | `/api/tasks/{taskId}` | Update task details, status, or move across lists | `{ "status": "DONE", "targetListId": 3, "position": 0 }` |
| `DELETE` | `/api/tasks/{taskId}` | Delete a task card | — |

---

## Tech Stack

* **Backend:** Java 26, Spring Boot, Spring Web, Spring Data JPA, Jakarta Bean Validation, Lombok
* **Database:** PostgreSQL, DBeaver
* **Frontend:** React 19, TypeScript, Vite, CSS3
* **Testing & Tooling:** JUnit 5, Mockito, Maven, Postman, Git

---

## Local Setup & Running Instructions

### Prerequisites
* JDK 26+ & Maven
* PostgreSQL (running locally on port `5432`)
* Node.js (v18+)

### 1. Database Setup
Create a PostgreSQL database named `task_manager_tool`:
```sql
CREATE DATABASE task_manager_tool;
```

### 2. Run the Spring Boot Backend
1. Navigate to the backend directory (`task-management-tool-backend` or root Maven directory) and update `src/main/resources/application.properties` with your PostgreSQL credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/task_manager_tool
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_PASSWORD
   ```
2. Start the server (automatically seeds a default user, workspace, board, lists, and sample tasks on first boot):
   ```bash
   ./mvnw spring-boot:run
   ```
3. Run the unit test suite:
   ```bash
   ./mvnw test
   ```

### 3. Run the React Frontend
1. Open a new terminal and navigate to `task-management-tool-frontend`:
   ```bash
   cd task-management-tool-frontend
   npm install
   npm run dev
   ```
2. Open **`http://localhost:5173`** in your browser.

---

## Future Roadmap
* JWT-based authentication & Spring Security multi-user authorization
* Drag-and-drop card reordering (`@dnd-kit`)
* Inline task description editing modal & due date reminders
* Spring Boot `MockMvc` end-to-end integration tests
