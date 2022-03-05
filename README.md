# class-registration

This is a Springboot Rest API project in order to try to simulate a Student - Courses registration System, 
all the information is stored in a MySQL database using Docker Compose.

Note: Some data related to Students and Courses is preloaded  

## Prerequisites
In order to run this app with Docker Compose it relies on Docker Engine, so make sure you have 
**Docker Engine installed**

## Getting started

1. Start the `class-registration` api along the mysql database in the background:

    ```bash
    docker-compose up -d
    ```

## Usage

### Get all the Students
This method retrieves all the Students but doesn't show the details (courses)
GET http://localhost:8080/api/v1/students

### Get all the Courses
This method retrieves all the Courses but doesn't show the details (students)
GET http://localhost:8080/api/v1/courses

### Register a Student
This method is used to enroll a Student with in one or multiple courses
POST http://localhost:8080/api/v1/register

   ```json
    {
    "studentId": "1",
    "courses": [
        {
        "id": 4,
        "name": "Programming"
        },
        {
          "id": 5,
          "name": "Android"
        }
      ]
    }
```

### View Relationships between Students and Courses
This is just a get all courses with details (that means including the students enrolled) so in the front end its possible to show the relationship between a particular student and courses
GET http://localhost:8080/api/v1/courses/full

### Filter Students for a specific course
This method retrieves all the students given a course ID
GET http://localhost:8080/api/v1/courses/{courseID}/students

### Filter Courses for a specific student
This method retrieves all the courses which in the student is enrolled
GET http://localhost:8080/api/v1/students/{studentID}/courses

### Filter all courses without any students
This method retrieves all the courses which there are no students enrolled yet
GET http://localhost:8080/api/v1/courses/empty

### Filter all students without any courses
This method retrieves all the students which not have been enrolled in any course yet
GET http://localhost:8080/api/v1/students/empty

This API includes all the CRUD methods for Students and Courses, once the app is running you can access to the Swagger UI with this link: http://localhost:8080/swagger-ui.html