# BookIt - Backend

A table reservation system built with Spring Boot, React, and MongoDB.

## 🚀 Getting Started

### Prerequisites

- Java 17
- Maven
- MongoDB

### Run the Application

```bash
./mvnw spring-boot:run
```

### Swagger API Docs

📄 Swagger UI: http://localhost:8080/swagger-ui/index.html

### MongoDB

[MongoDB Schema and CRUD](https://sjsu-shamathmika.atlassian.net/wiki/spaces/SSE/pages/15335450/MongoDB+-+Schema+and+CRUD) <br>

1. Rename the ```.env.example``` file to ```.env```
2. Fill in the Database name and other details from the Atlas Cluster

### Testing JWT Authorization

1. Generate secret key and save it in ```JWT_SECRET_KEY``` in ```.env``` file
2. Update ```JWT_EXPIRATION``` to time in milliseconds (86400000 for 24h)
3. Use ```localhost:8080/api/auth/signup``` ```POST``` request to add a document to ```user``` collection.<br>
    1. Provide a JSON as follows (at the minimum, you can add phoneNumber too) in the Body
   ```json 
   {
        "name": "first last",
        "email": "first.last@email.com", // Ensure right format, checks are in place. Also ensure the email is not already in DB
        "password": "password_in_plain_text", // This will be encrypted before saving in DB
        "role": "ROLE_CUSTOMER" // Can be ROLE_MANAGER or ROLE_ADMIN
   }

4. Once registered successfully, sign in using ```POST``` request on ```localhost:8080/api/auth/signin```
    1. Provide a JSON as follows in the Body
   ```json 
   {
        "email": "first.last@email.com", 
        "password": "password_in_plain_text"
   }
5. Copy the ```token``` obtained from the response Body (without the quotes) - it will have 3 parts separated by ```.```
6. Run a ```GET``` request on ```localhost:8080/api/test/```
    - should receive "Public access" in the response Body
7. Run a ```GET``` request on ```localhost:8080/api/test/bookTable``` with Header having Key as ```Authorization``` and
   Value as ```Bearer <token>```. Ensure to have ```Bearer ``` with one space before the ```token```
    - should receive "Booking Table" in the response Body. This is possible for valid Customer, Manager and Admin
8. Run a ```GET``` request on ```localhost:8080/api/test/manager``` with the same Header as 5
    - should get "Manager Portal" if signed in as manager. Error 403 otherwise
9. Run a ```GET``` request on ```localhost:8080/api/test/admin``` with the same Header as 5
    - should get "Admin Portal" if signed in as admin. Error 403 otherwise
10. If you attempt to access any random path that is not defined like ```localhost:8080/api/test/admin123```, you will
    get Error 404
11. If you attempt to access ```localhost:8080/api/test/bookTable``` without passing the Header, you will get Error 401