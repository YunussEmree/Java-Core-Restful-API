# User Management System

A simple web application that allows users to submit data through a form, save it to a database, and display the data in a table on the same page.

## Features

- Bootstrap-based responsive UI design
- Form for submitting user data
- Real-time table display of all users
- RESTful API for data operations
- Error handling and user feedback

## Technical Stack

- Core Java for backend
- Bootstrap 5 for UI components
- jQuery for DOM manipulation
- AJAX for asynchronous API calls
- Jackson for JSON processing

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── yunussemree/
│   │           ├── database/
│   │           │   └── DatabaseConfig.java
│   │           ├── handler/
│   │           │   ├── CorsHandler.java
│   │           │   └── UserHandler.java
│   │           ├── model/
│   │           │   └── User.java
│   │           ├── repository/
│   │           │   └── UserRepository.java
│   │           ├── util/
│   │           │   └── JsonUtils.java
│   │           └── Main.java
│   └── resources/
│       └── static/
│           ├── css/
│           │   └── style.css
│           ├── js/
│           │   └── script.js
│           └── index.html
```

## How to Run

1. Make sure you have Java 17 or higher installed
2. Clone this repository
3. Build the project using Maven:
   ```
   mvn clean package
   ```
4. Run the application:
   ```
   java -jar target/mobildev-task-1.0-SNAPSHOT.jar
   ```
5. Open your browser and navigate to http://localhost:8000

## API Endpoints

- `GET /api/users` - Retrieve all users
- `POST /api/users` - Create a new user

## License

This project is open source and available under the [MIT License](LICENSE). 
