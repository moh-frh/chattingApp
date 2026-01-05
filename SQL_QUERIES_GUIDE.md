# SQL Queries Guide for Chat App Database

## Important Notes

⚠️ **Table Names are Case-Sensitive in H2:**
- The actual table name is `users` (lowercase), not `USERS`
- The actual table name is `chat_messages` (lowercase), not `CHAT_MESSAGES`

⚠️ **Backend Must Be Running:**
- Tables are created automatically when Spring Boot starts
- If you see "Table not found", make sure the backend is running
- In-memory database means tables disappear when backend stops

## Correct SELECT Queries

### Get All Users (Correct - lowercase)
```sql
SELECT * FROM users;
```

### Get All Users with Specific Columns
```sql
SELECT id, username, email, created_at FROM users;
```

### Get All Users (Exclude Password)
```sql
SELECT id, username, email, created_at FROM users;
```

### Get All Messages
```sql
SELECT * FROM chat_messages;
```

## If Tables Don't Exist

### Option 1: Start the Backend (Recommended)
1. Make sure backend is running: `mvn spring-boot:run`
2. Tables are created automatically on startup
3. Then connect to H2 Console and run queries

### Option 2: Create Tables Manually (If needed)
If for some reason tables weren't created, you can create them manually:

```sql
-- Create USERS table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP
);

-- Create CHAT_MESSAGES table
CREATE TABLE chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);
```

## Common Queries

### View All Users
```sql
SELECT * FROM users;
```

### View All Users (Formatted)
```sql
SELECT 
    id,
    username,
    email,
    FORMATDATETIME(created_at, 'yyyy-MM-dd HH:mm:ss') AS created_at
FROM users
ORDER BY created_at DESC;
```

### Count Users
```sql
SELECT COUNT(*) AS total_users FROM users;
```

### View All Messages
```sql
SELECT * FROM chat_messages;
```

### View Messages with User Details
```sql
SELECT 
    m.id,
    m.content,
    m.created_at,
    m.is_read,
    s.username AS sender,
    r.username AS receiver
FROM chat_messages m
JOIN users s ON m.sender_id = s.id
JOIN users r ON m.receiver_id = r.id
ORDER BY m.created_at DESC;
```

### View Unread Messages
```sql
SELECT * FROM chat_messages WHERE is_read = FALSE;
```

### Count Messages per User
```sql
SELECT 
    u.username,
    COUNT(m.id) AS message_count
FROM users u
LEFT JOIN chat_messages m ON u.id = m.sender_id OR u.id = m.receiver_id
GROUP BY u.id, u.username;
```

## Troubleshooting

### Error: "Table 'USERS' not found"
**Solution:** Use lowercase: `SELECT * FROM users;`

### Error: "Table 'users' not found"
**Solution:** 
1. Make sure backend is running
2. Check backend console for errors
3. Tables are created on first startup

### Database is Empty
**Solution:**
1. This is normal for a fresh start
2. Register some users through the API or frontend
3. Data will appear in the database

### Can't Connect to H2 Console
**Solution:**
1. Make sure backend is running on port 8080
2. Use URL: `http://localhost:8080/h2-console`
3. JDBC URL: `jdbc:h2:mem:chatappdb`
4. Username: `sa`
5. Password: (empty)

## Table Structure Reference

### users table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| username | VARCHAR(255) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| created_at | TIMESTAMP | |

### chat_messages table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| sender_id | BIGINT | NOT NULL, FOREIGN KEY |
| receiver_id | BIGINT | NOT NULL, FOREIGN KEY |
| content | VARCHAR(1000) | NOT NULL |
| created_at | TIMESTAMP | |
| is_read | BOOLEAN | DEFAULT FALSE |

## Quick Reference

**Always use lowercase table names:**
- ✅ `users` (correct)
- ❌ `USERS` (wrong)
- ✅ `chat_messages` (correct)
- ❌ `CHAT_MESSAGES` (wrong)

**Always ensure backend is running before querying!**

