# Chatting App

A full-stack chat application built with Spring Boot (backend) and React.js (frontend), featuring user registration, authentication, and one-to-one messaging.

## Features

- User Registration and Login
- JWT-based Authentication
- One-to-One Chat between users
- Real-time message polling
- H2 In-Memory Database

## Technology Stack

### Backend
- Spring Boot 3.2.0
- Spring Security with JWT
- Spring Data JPA
- H2 Database
- Java 17

### Frontend
- React 18.2.0
- React Router DOM
- Axios for API calls

## Prerequisites

- OpenJDK 17.0.14 or higher
- Node.js 14.x or higher
- Maven 3.6.x or higher
- npm or yarn

## Getting Started

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the Spring Boot application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the React development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/me` - Get current authenticated user

### Chat
- `POST /api/chat/send` - Send a message
- `GET /api/chat/conversation/{userId}` - Get conversation with a user
- `GET /api/chat/unread` - Get unread messages
- `PUT /api/chat/read/{messageId}` - Mark message as read

## Database

The application uses H2 in-memory database. You can access the H2 console at:
`http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:chatappdb`
- Username: `sa`
- Password: (empty)

## Usage

1. Start both backend and frontend servers
2. Open `http://localhost:3000` in your browser
3. Register a new account or login with existing credentials
4. Select a user from the sidebar to start chatting
5. Type and send messages

## Project Structure

```
chattingApp/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/chatapp/
│   │   │   │   ├── config/          # Security configuration
│   │   │   │   ├── controller/      # REST controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── filter/          # JWT filter
│   │   │   │   ├── model/           # Entity models
│   │   │   │   ├── repository/      # JPA repositories
│   │   │   │   ├── service/         # Business logic
│   │   │   │   └── util/            # Utility classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
└── frontend/
    ├── public/
    └── src/
        ├── components/      # React components
        ├── context/         # React context (Auth)
        ├── services/        # API service layer
        └── App.js
```

## Notes

- The JWT secret key is configured in `application.properties`. For production, use a secure random key.
- Messages are polled every 2 seconds. For real-time updates, consider implementing WebSockets.
- The H2 database is in-memory, so data will be lost when the application restarts.


