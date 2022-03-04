package com.jorge.api.controller;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Student;
import com.jorge.api.request.StudentRequest;
import com.jorge.api.response.CourseResponse;
import com.jorge.api.response.StudentResponse;
import com.jorge.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllCourses(){
        List<Student> students = studentService.getAllStudents();

        if(students.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getCourseById(@PathVariable("id") long id) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new ApiRequestException("Not found Student with id = " + id));
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Student> createCourse(@RequestBody StudentRequest studentRequest) {
        Student _student = studentService.save(studentRequest);
        return new ResponseEntity<>(_student, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateCourse(@PathVariable("id") long id, @RequestBody StudentRequest studentRequest) {
        Student student = studentService.update(id, studentRequest );
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        studentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponse>> getCoursesById(@PathVariable("id") long id) {
        List<CourseResponse> courses = studentService.getCoursesByStudent(id);
        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<StudentResponse>> getEmptyCourses(){
        List<StudentResponse> courses = studentService.getStudentsWithoutCourses();
        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
}
