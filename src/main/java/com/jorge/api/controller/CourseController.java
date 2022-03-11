package com.jorge.api.controller;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.request.CourseRequest;
import com.jorge.api.response.CourseFullResponse;
import com.jorge.api.response.CourseResponse;
import com.jorge.api.response.StudentResponse;
import com.jorge.api.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(){
        List<Course> courses = courseService.getAllCourses();

        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") long id) {
        return new ResponseEntity<>(courseService.getCourseById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Course> createCourse(@RequestBody CourseRequest course) {
        Course _course = courseService.save(course);
        return new ResponseEntity<>(_course, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") long id, @RequestBody CourseRequest courseRequest) {
        return new ResponseEntity<>(courseService.update(id, courseRequest ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        courseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentResponse>> getCoursseById(@PathVariable("id") long id) {
        List<StudentResponse> allStudentsFromCourse = courseService.getAllStudentsFromCourse(id);
        if(allStudentsFromCourse.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allStudentsFromCourse, HttpStatus.OK);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<CourseResponse>> getEmptyCourses(){
        List<CourseResponse> courses = courseService.getCoursesWithoutStudents();

        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/full")
    public ResponseEntity<List<CourseFullResponse>> getCoursesAndStudentsRelationship(){
        List<CourseFullResponse> courses = courseService.getCoursesWithStudents();

        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
}
