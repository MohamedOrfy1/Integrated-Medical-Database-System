# Integrated-Medical-Database-System
## API Endpoints


## Login Endpoint

### Request
`POST /api/user/login`

**Content-Type:** `application/json`

**Request Body:**
json
{
  "username": "your_username",
  "password": "your_password"
}

**Requirements:**

User must exist in the database

Credentials must be valid

**Responses:**

Success:
Returns "successful login"

Failure Cases:
1-Returns error message (403 Forbidden if username does not exist in the database)
2-Returns "incorrect password" if username exists but password is not correct.