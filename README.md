## Job Swipe Server

This repository contains the backend server for the Job Swipe application. It is built using Spring Boot and utilizes several technologies to provide a comprehensive platform for job seekers and companies.

## Technologies used:

-   **Spring Boot**: Provides a robust framework for building the backend server.
-   **Spring Data JPA**: Enables seamless interaction with the PostgreSQL database for data persistence.
-   **Spring Security**: Ensures secure authentication and authorization for users and companies.
-   **JWT (JSON Web Token)**: Facilitates secure authentication and authorization through tokens.
-   **Redis**: Provides caching and session management for enhanced performance.
-   **Amazon S3**: Securely stores uploaded files (e.g., user avatars, company logos).
-   **Mailtrap**: Provides a reliable and secure email testing service.
-   **TCP-Socket**: Enables real-time communication between the server and clients for features like instant messaging and notifications.

## Features:

-   **User Authentication & Authorization**:
    -   Users can register and log in using email and password.
    -   Authentication and authorization are handled using JWT.
    -   Securely stores user information in a PostgreSQL database.
    -   Supports login and registration for users, companies, and an administrator.
-   **User Profiles**:
    -   Users can create detailed profiles including basic information, education, experience, awards, and other descriptions.
    -   Users can upload their avatar images.
    -   Provides features for updating and deleting user profile information.
-   **Company Profiles**:
    -   Companies can create profiles with details about their company, website, and established date.
    -   Companies can upload their logo images.
    -   Provides features for updating and deleting company profile information.
-   **Matching System**:
    -   Users and companies can request to match with each other.
    -   Matches are accepted or rejected by both parties.
    -   Supports features like requesting, accepting, rejecting, and cancelling matches.
    -   Triggers real-time notifications to users and companies on matching events.
-   **Chat System**:
    -   Once matched, users and companies can communicate via a secure and private chat system.
    -   Supports features like sending messages, uploading files, and reading messages.
    -   Provides real-time message delivery and read notifications.
-   **Notifications**:
    -   Users and companies receive real-time notifications for various events, such as new matches, chat messages, and admin actions.
-   **Admin Panel**:
    -   An administrator can manage user and company accounts.
    -   Supports features like activating and deactivating accounts.
    -   Includes a dashboard to monitor user activity and overall platform performance.

## How to run the application:

### Prerequisites

-   Docker (for Docker Compose)
-   Java Development Kit (JDK) 17 (for Maven)
-   Maven 3.6.1 or higher (for Maven build)
-   PostgreSQL database (can be run with Docker)
-   Redis (can be run with Docker)
-   AWS S3 account (or use a local alternative like MinIO for development)
-   Mailtrap account

### 1. Clone the repository:

```bash
git clone https://github.com/dhv-pbl5/job-swipe-server.git
```

### 2. Change directory to project:

```bash
cd job-swipe-server
```

### 3. Run using Docker Compose (recommended for development):

-   Configure .env:
    -   Create a `.env` file in the root directory of the project.
    -   Add the environment variables from docker-compose.yml file to the `.env` file
-   Run the application:
    ```bash
    docker-compose up -d
    ```

### 4. Run using Maven:

-   Configure application-dev.yml:
    -   Copy `application.yml` to `application-dev.yml`
    -   Fill in the missing values in `application-dev.yml`
-   Run the application:
    ```bash
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ```

### 5. Access the API:

-   Once the application is running, the API is available at `http://localhost:8080/api`.
-   The real-time socket server is available at `http://localhost:8888`.

## Contributing:

Contributions are welcome! Please feel free to submit pull requests or raise issues.

## License:

This project is licensed under the [MIT License](LICENSE).
