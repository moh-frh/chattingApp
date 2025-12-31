# Postman Testing Guide for Chat App Backend

## Prerequisites
- Backend server must be running on `http://localhost:8080`
- Postman installed and running

## 1. Test User Registration

### Endpoint Details
- **URL**: `http://localhost:8080/api/auth/register`
- **Method**: `POST`
- **Authentication**: None required (public endpoint)

### Step-by-Step Instructions

1. **Open Postman** and create a new request

2. **Set the Request Type**:
   - Select `POST` from the dropdown

3. **Enter the URL**:
   ```
   http://localhost:8080/api/auth/register
   ```

4. **Set Headers**:
   - Go to the **Headers** tab
   - Add the following header:
     - **Key**: `Content-Type`
     - **Value**: `application/json`

5. **Set Request Body**:
   - Go to the **Body** tab
   - Select **raw**
   - Select **JSON** from the dropdown
   - Enter the following JSON:
   ```json
   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123"
   }
   ```

6. **Send the Request**:
   - Click the **Send** button

### Expected Success Response (200 OK)
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5...",
  "username": "testuser",
  "userId": 1,
  "message": "Registration successful"
}
```

### Expected Error Responses

#### Validation Error (400 Bad Request)
```json
{
  "token": null,
  "username": null,
  "userId": null,
  "message": "Username must be between 3 and 20 characters"
}
```

#### Duplicate Username (400 Bad Request)
```json
{
  "token": null,
  "username": null,
  "userId": null,
  "message": "Username already exists"
}
```

#### Duplicate Email (400 Bad Request)
```json
{
  "token": null,
  "username": null,
  "userId": null,
  "message": "Email already exists"
}
```

### Validation Rules
- **Username**: 
  - Required
  - Minimum 3 characters
  - Maximum 20 characters
- **Email**: 
  - Required
  - Must be a valid email format
- **Password**: 
  - Required
  - Minimum 6 characters

## 2. Test User Login

### Endpoint Details
- **URL**: `http://localhost:8080/api/auth/login`
- **Method**: `POST`
- **Authentication**: None required (public endpoint)

### Request Body
```json
{
  "username": "testuser",
  "password": "password123"
}
```

### Expected Success Response (200 OK)
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5...",
  "username": "testuser",
  "userId": 1,
  "message": "Login successful"
}
```

### Expected Error Response (401 Unauthorized)
```json
{
  "token": null,
  "username": null,
  "userId": null,
  "message": "Invalid credentials"
}
```

## 3. Test Get All Users (Requires Authentication)

### Endpoint Details
- **URL**: `http://localhost:8080/api/users`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

### Steps
1. First, register or login to get a token
2. Copy the `token` from the response
3. Create a new GET request to `http://localhost:8080/api/users`
4. Go to the **Authorization** tab
5. Select **Bearer Token** from the Type dropdown
6. Paste the token in the **Token** field
7. Click **Send**

### Expected Response (200 OK)
```json
[
  {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "createdAt": "2025-12-31T02:00:00"
  }
]
```

## 4. Test Get Current User (Requires Authentication)

### Endpoint Details
- **URL**: `http://localhost:8080/api/users/me`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

### Steps
Same as above, but use the `/api/users/me` endpoint

## 5. Test Send Message (Requires Authentication)

### Endpoint Details
- **URL**: `http://localhost:8080/api/chat/send`
- **Method**: `POST`
- **Authentication**: Required (JWT Token)

### Request Body
```json
{
  "receiverId": 2,
  "content": "Hello, this is a test message!"
}
```

## Quick Test Scenarios

### Scenario 1: Successful Registration
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securepass123"
}
```

### Scenario 2: Invalid Email Format
```json
{
  "username": "jane_doe",
  "email": "invalid-email",
  "password": "password123"
}
```
Expected: Error message "Email should be valid"

### Scenario 3: Username Too Short
```json
{
  "username": "ab",
  "email": "test@example.com",
  "password": "password123"
}
```
Expected: Error message "Username must be between 3 and 20 characters"

### Scenario 4: Password Too Short
```json
{
  "username": "testuser2",
  "email": "test2@example.com",
  "password": "12345"
}
```
Expected: Error message "Password must be at least 6 characters"

### Scenario 5: Duplicate Registration
Try registering the same user twice with the same username or email.

## Postman Collection Setup Tips

1. **Create an Environment**:
   - Create a new environment in Postman
   - Add variable: `baseUrl` = `http://localhost:8080`
   - Add variable: `token` = (will be set after login)

2. **Use Variables**:
   - Use `{{baseUrl}}/api/auth/register` instead of hardcoding the URL
   - Use `{{token}}` in Authorization header

3. **Save Token Automatically**:
   - In the Tests tab of login/register request, add:
   ```javascript
   if (pm.response.code === 200) {
       var jsonData = pm.response.json();
       pm.environment.set("token", jsonData.token);
   }
   ```

4. **Create a Collection**:
   - Group all requests in a collection called "Chat App API"
   - Organize by: Auth, Users, Chat

## Troubleshooting

### Connection Refused
- Make sure the backend is running: `mvn spring-boot:run` in the backend directory
- Check if port 8080 is available: `netstat -ano | findstr :8080`

### 401 Unauthorized
- Make sure you're using a valid JWT token
- Check if the token has expired (default: 24 hours)
- Ensure the Authorization header format is: `Bearer <token>`

### 400 Bad Request
- Check the request body format (must be valid JSON)
- Verify all required fields are present
- Check validation rules (username length, email format, password length)

### CORS Error
- This shouldn't happen in Postman, but if testing from browser, make sure CORS is configured correctly


