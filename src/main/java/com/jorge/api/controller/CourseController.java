package com.jorge.api.controller;

import com.jorge.api.exception.ResourceNotFoundException;
import com.jorge.api.model.Course;
import com.jorge.api.request.CourseRequest;
import com.jorge.api.response.StudentResponse;
import com.jorge.api.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses(){
        List<Course> courses = courseService.getAllCourses();

        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long id) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Course with id = " + id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody CourseRequest course) {
        Course _course = courseService.save(course);
        return new ResponseEntity<>(_course, HttpStatus.CREATED);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") long id, @RequestBody CourseRequest courseRequest) {
        Course course = courseService.update(id, courseRequest );
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        courseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/courses/{id}/students")
    public ResponseEntity<List<StudentResponse>> getCoursseById(@PathVariable("id") long id) {
        List<StudentResponse> allStudentsFromCourse = courseService.getAllStudentsFromCourse(id);
        if(allStudentsFromCourse.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allStudentsFromCourse, HttpStatus.OK);
    }
}
