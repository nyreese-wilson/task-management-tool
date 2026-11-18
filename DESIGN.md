# Task Management Tool — Technical Design Document

## 1. Database Schema (ERD)

Parent-child hierarchy using primary keys (`id`) and foreign keys (`_id`):

* **`users`**: `id`, `email`, `password_hash`, `created_at`
* **`workspaces`**: `id`, `name`, `owner_id` (FK → `users.id`)
* **`boards`**: `id`, `title`, `workspace_id` (FK → `workspaces.id`)
* **`lists`** (Columns): `id`, `title`, `position` (integer for column order), `board_id` (FK → `boards.id`)
* **`tasks`** (Cards): `id`, `title`, `description`, `status`, `position` (integer for card order), `list_id` (FK → `lists.id`), `assignee_id` (FK → `users.id`, nullable)

## 2. Core REST API Contracts

* **`POST /api/boards`** — Create a new board inside a workspace.
* **`GET /api/boards/{boardId}`** — Fetch a board along with all its nested lists and tasks.
* **`POST /api/boards/{boardId}/lists`** — Add a new column (e.g., "To-Do") to a board.
* **`POST /api/lists/{listId}/tasks`** — Create a new task card inside a specific list.
* **`PATCH /api/tasks/{taskId}`** — Update a task's description, status, or move it to a different `list_id` / `position`.
