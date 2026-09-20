# Bug Tracker Android App

A mobile **Bug Tracker application** built with **Kotlin and Android Studio**. The project is designed to demonstrate how an Android application can manage software issue tickets locally, prepare data for remote synchronization, handle application lifecycle changes, retry failed synchronization tasks, and use Git/GitHub for version control.

## Project Overview

The Bug Tracker app is designed around the same basic idea as issue-tracking systems such as GitHub Issues or Jira.

Users can create and manage bug or issue tickets containing information such as:

* Issue title
* Description
* Priority
* Status
* Creation date
* Synchronization status

The application uses **Room Database** for local storage and **Retrofit** as the remote API communication layer.

The project also uses **WorkManager** to support background synchronization and retry operations when network connectivity is available.

> **Note:** The project currently demonstrates the architecture for remote synchronization. A production backend API endpoint has not been provided for this academic project, so successful communication with a live remote server is not being claimed.

---

## Project Goals

The main goals of this project are to demonstrate:

1. Local data storage using Room.
2. CRUD operations for issue tickets.
3. Remote API integration using Retrofit.
4. Repository-based application architecture.
5. Lifecycle-aware state management using ViewModel.
6. Background synchronization using WorkManager.
7. Retry handling when synchronization fails.
8. Git branching and version control.
9. GitHub collaboration and release tagging.

---

## Application Architecture

The project follows a layered architecture:

```text
┌──────────────────────┐
│      User Interface  │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│      ViewModel       │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│      Repository      │
└───────┬────────┬─────┘
        │        │
        ▼        ▼
┌────────────┐ ┌────────────┐
│    Room    │ │  Retrofit  │
│  Database  │ │  REST API  │
└────────────┘ └────────────┘
        │
        ▼
┌──────────────────────┐
│     WorkManager      │
│ Background Sync/Retry│
└──────────────────────┘
```

The **Room database** provides local persistence, while **Retrofit** provides the structure needed for communication with a remote REST API.

---

## Project Structure

The main project structure is currently organized as follows:

```text
BugTracker/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/bugtracker/
│           │       │
│           │       ├── data/
│           │       │   ├── Issue.kt
│           │       │   ├── IssueDao.kt
│           │       │   ├── BugTrackerDatabase.kt
│           │       │   ├── IssueApi.kt
│           │       │   └── IssueRepository.kt
│           │       │
│           │       ├── worker/
│           │       │   └── SyncWorker.kt
│           │       │
│           │       ├── ui/
│           │       │   └── IssueViewModel.kt
│           │       │
│           │       └── BugTrackerApplication.kt
│           │
│           └── res/
│               └── layout/
│                   └── activity_main.xml
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── .gitignore
```

---

## Local Database

The application uses **Room** as the local database layer.

Each issue contains:

| Field          | Description                                            |
| -------------- | ------------------------------------------------------ |
| `id`           | Unique issue identifier                                |
| `title`        | Issue title                                            |
| `description`  | Detailed description                                   |
| `priority`     | Issue priority                                         |
| `status`       | Current issue status                                   |
| `creationDate` | Date the issue was created                             |
| `syncStatus`   | Indicates whether the issue is pending or synchronized |

Example:

```text
Issue
├── ID: 1
├── Title: Login button does not work
├── Description: Login button does not respond after entering credentials.
├── Priority: HIGH
├── Status: OPEN
├── Creation Date: 2026-09-20
└── Sync Status: PENDING
```

---

## CRUD Operations

The application provides the data layer required for the standard CRUD operations:

* **Create** — Add a new issue.
* **Read** — Retrieve stored issues.
* **Update** — Modify an existing issue.
* **Delete** — Remove an issue.

The `IssueDao` provides the database operations, while the `IssueRepository` provides a single access point for the application's data sources.

---

## Remote API

Retrofit is used to define the remote API operations.

The API interface currently provides endpoints for:

```text
GET     /issues
POST    /issues
PUT     /issues/{id}
DELETE  /issues/{id}
```

These endpoints represent the intended REST API structure for the application.

A production server URL can be configured later when a real backend service is available.

---

## Synchronization and Retry

Issues created locally are initially marked:

```text
PENDING
```

The synchronization worker looks for pending issues and attempts to send them through the repository to the remote API.

When synchronization succeeds:

```text
PENDING → SYNCED
```

If synchronization fails, the WorkManager task returns:

```text
Result.retry()
```

The worker also uses a network constraint so that synchronization requires an available network connection.

The retry configuration uses exponential backoff to avoid repeatedly attempting synchronization immediately.

---

## Lifecycle and State Management

The application uses Android's `ViewModel` architecture to maintain application state separately from the Activity lifecycle.

The ViewModel exposes the issue list as a `StateFlow`.

This allows the UI to observe database changes while reducing the risk of losing application state during configuration changes such as screen rotation.

---

## Technologies Used

| Technology         | Purpose                              |
| ------------------ | ------------------------------------ |
| **Kotlin**         | Application programming language     |
| **Android Studio** | Android development environment      |
| **Room**           | Local SQLite database abstraction    |
| **Retrofit**       | REST API communication               |
| **Gson Converter** | JSON serialization/deserialization   |
| **ViewModel**      | Lifecycle-aware state management     |
| **StateFlow**      | Observable application state         |
| **WorkManager**    | Background synchronization and retry |
| **Git**            | Version control                      |
| **GitHub**         | Remote repository and collaboration  |

---

## Git Workflow

Git branches were used to organize development.

Example workflow:

```text
main
 │
 ├── feature/bug-sync
 │
 └── hotfix/bug-tracker
```

The project includes release tags:

```text
v1.0
v1.1
```

The feature branch was used to develop synchronization-related functionality before merging the work into `main`.

A separate hotfix branch was also created to demonstrate how urgent fixes can be developed and merged independently.

---

## Releases

### v1.0

Initial project release containing the main Room database and Retrofit API architecture.

### v1.1

Follow-up release containing the hotfix and clarification related to issue synchronization status.

---

## Testing Locally

The application can be tested using the Android Emulator or a physical Android device.

### Android Emulator

1. Open the project in Android Studio.
2. Open **Device Manager**.
3. Create or select an Android Virtual Device.
4. Start the emulator.
5. Select the emulator from the device selector.
6. Click **Run ▶**.

The application should then be installed and launched on the virtual device.

### Suggested Tests

When the user interface is implemented, the following scenarios can be tested:

```text
Test 1: Create an issue
       ↓
Issue saved to Room
       ↓
Issue appears in the application
```

```text
Test 2: Close and reopen application
       ↓
Room database remains available
       ↓
Previously saved issue is displayed
```

```text
Test 3: Update an issue
       ↓
Change issue status/priority
       ↓
Room database is updated
```

```text
Test 4: Delete an issue
       ↓
Issue removed from local database
```

```text
Test 5: Synchronization failure
       ↓
Remote request fails
       ↓
WorkManager retries the operation
```

---

## Current Status

### Completed

* [x] Android Studio project created
* [x] Kotlin project configured
* [x] Room database configured
* [x] Issue entity created
* [x] Issue DAO created
* [x] CRUD data operations created
* [x] Repository layer created
* [x] Retrofit API interface created
* [x] ViewModel created
* [x] WorkManager synchronization worker created
* [x] Retry mechanism configured
* [x] Internet permission configured
* [x] Git repository configured
* [x] Feature branch workflow demonstrated
* [x] Hotfix branch workflow demonstrated
* [x] Git tags `v1.0` and `v1.1` created

### In Progress

* [ ] Complete the final mobile user interface
* [ ] Connect UI controls to the ViewModel
* [ ] Test CRUD operations through the UI
* [ ] Test the application on an Android Emulator
* [ ] Add application screenshots
* [ ] Connect to a real backend API when available

---

## Academic Context

This project was developed as part of a mobile application development unit and is intended to demonstrate practical Android development concepts including local databases, remote data operations, lifecycle management, error handling, background processing and Git version control.

---

## Author

**Irene Nyangasi**

This repository contains the development work for the Bug Tracker Android application.
