# H2 Database Access Guide

## Accessing the H2 Database Console

The application uses H2 in-memory database. You can access it through the H2 Console web interface.

### Prerequisites
- Backend server must be running on `http://localhost:8080`

### Steps to Access H2 Console

1. **Open your web browser**

2. **Navigate to the H2 Console URL:**
   ```
   http://localhost:8080/h2-console
   ```

3. **Login with the following credentials:**
   - **JDBC URL**: `jdbc:h2:mem:chatappdb`
   - **User Name**: `sa`
   - **Password**: (leave empty)

4. **Click "Connect"**

### Connection Settings

| Setting | Value |
|---------|-------|
| **JDBC URL** | `jdbc:h2:mem:chatappdb` |
| **User Name** | `sa` |
| **Password** | (empty - leave blank) |
| **Driver Class** | `org.h2.Driver` (auto-filled) |

### Important Notes

⚠️ **In-Memory Database**: 
- The database is **in-memory**, which means all data will be **lost** when you restart the backend server
- Data persists only while the application is running
- Each time you restart the backend, the database starts fresh

### Available Tables

Once connected, you can view and query the following tables:

1. **USERS** - Stores user information
   - `ID` (BIGINT, PRIMARY KEY)
   - `USERNAME` (VARCHAR, UNIQUE)
   - `EMAIL` (VARCHAR, UNIQUE)
   - `PASSWORD` (VARCHAR)
   - `CREATED_AT` (TIMESTAMP)

2. **CHAT_MESSAGES** - Stores chat messages
   - `ID` (BIGINT, PRIMARY KEY)
   - `SENDER_ID` (BIGINT, FOREIGN KEY to USERS)
   - `RECEIVER_ID` (BIGINT, FOREIGN KEY to USERS)
   - `CONTENT` (VARCHAR)
   - `CREATED_AT` (TIMESTAMP)
   - `IS_READ` (BOOLEAN)

### Useful SQL Queries

#### View All Users
```sql
SELECT * FROM USERS;
```

#### View All Messages
```sql
SELECT * FROM CHAT_MESSAGES;
```

#### View Messages with User Details
```sql
SELECT 
    m.ID,
    m.CONTENT,
    m.CREATED_AT,
    s.USERNAME AS SENDER,
    r.USERNAME AS RECEIVER,
    m.IS_READ
FROM CHAT_MESSAGES m
JOIN USERS s ON m.SENDER_ID = s.ID
JOIN USERS r ON m.RECEIVER_ID = r.ID
ORDER BY m.CREATED_AT DESC;
```

#### Count Messages per User
```sql
SELECT 
    u.USERNAME,
    COUNT(m.ID) AS MESSAGE_COUNT
FROM USERS u
LEFT JOIN CHAT_MESSAGES m ON u.ID = m.SENDER_ID OR u.ID = m.RECEIVER_ID
GROUP BY u.ID, u.USERNAME;
```

#### View Unread Messages
```sql
SELECT * FROM CHAT_MESSAGES WHERE IS_READ = FALSE;
```

#### Clear All Data (Use with caution!)
```sql
DELETE FROM CHAT_MESSAGES;
DELETE FROM USERS;
```

### Troubleshooting

#### Cannot Access H2 Console

1. **Check if backend is running:**
   - Verify the backend is running on port 8080
   - Check the backend console for any errors

2. **Check the URL:**
   - Make sure you're using: `http://localhost:8080/h2-console`
   - Not: `https://` (use http)

3. **Check Security Configuration:**
   - The `/h2-console/**` path should be permitted in SecurityConfig
   - It's already configured to allow access without authentication

#### Connection Error

1. **Verify JDBC URL:**
   - Must be exactly: `jdbc:h2:mem:chatappdb`
   - Case-sensitive

2. **Check Username:**
   - Must be: `sa` (lowercase)

3. **Password:**
   - Leave it **empty** (blank)

#### Database is Empty

- This is normal if you just started the backend
- Register some users through the API or frontend
- Data will appear in the database after registration

### Alternative: Using Database Tools

You can also connect to the H2 database using external database tools like:
- **DBeaver**
- **IntelliJ IDEA Database Tool**
- **Eclipse Database Development Tools**

**Connection Details:**
- **Driver**: H2 Database
- **URL**: `jdbc:h2:mem:chatappdb`
- **User**: `sa`
- **Password**: (empty)

**Note**: For external tools, you may need to change the database from in-memory to file-based in `application.properties`:
```properties
spring.datasource.url=jdbc:h2:file:./data/chatappdb
```

This will create a file-based database that persists across restarts.

### Switching to File-Based Database (Persistent)

If you want the database to persist across restarts, update `application.properties`:

```properties
# Change from in-memory to file-based
spring.datasource.url=jdbc:h2:file:./data/chatappdb
```

Then the JDBC URL in H2 Console would be:
```
jdbc:h2:file:./data/chatappdb
```

The database file will be created in the `backend/data/` directory.

