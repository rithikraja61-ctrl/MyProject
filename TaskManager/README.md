# TaskManager

A Java console-based task management application with user authentication. Users can log in and perform CRUD operations on their personal tasks.

## Features

- **User login** — Authenticate before accessing tasks
- **Add tasks** — Create new tasks with details
- **Update tasks** — Modify existing task information
- **Delete tasks** — Remove tasks from the list
- **List tasks** — View all tasks for the logged-in user
- **Interactive menu** — Simple console-based navigation

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java |
| Interface | Console (Scanner-based input) |
| Architecture | Interface-based OOP design |

## Project Structure

```
TaskManager/
├── Main.java                  # Application entry point and menu loop
├── Task.java                  # Task entity
├── TaskInterface.java         # Task operations interface
├── TaskManager.java           # Task CRUD implementation
├── TaskManagerInterface.java  # Task manager interface
├── TaskManagerUtil.java       # Utility methods
├── User.java                  # User entity
├── UserInterface.java         # User interface
├── UserManager.java           # User login and management
└── UserManagerInterface.java  # User manager interface
```

## Getting Started

### Prerequisites
- Java JDK 8 or higher

### Compile and Run

```bash
cd TaskManager
javac *.java
java Main
```

### Usage

1. Log in with your credentials when prompted
2. Choose from the menu:
   - `1` — Add a new task
   - `2` — Update an existing task
   - `3` — Delete a task
   - `4` — List all your tasks
   - `5` — Exit and return to login

## Architecture

The project follows interface-driven design:
- `TaskManagerInterface` / `TaskManager` — Handles all task operations
- `UserManagerInterface` / `UserManager` — Handles user authentication
- `TaskInterface` / `UserInterface` — Define entity contracts
