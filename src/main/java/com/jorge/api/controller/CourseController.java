package com.jorge.api.controller;

import com.jorge.api.dto.CourseDto;
import com.jorge.api.dto.CourseFullDto;
import com.jorge.api.dto.StudentDto;
import com.jorge.api.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseService.findAllCourses();
        if (courses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseFullDto> getCourseById(@PathVariable("id") long id) {
        return new ResponseEntity<>(courseService.findCourseById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) throws ParseException {
        CourseDto _course = courseService.saveCourse(courseDto);
        return new ResponseEntity<>(_course, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody CourseDto courseDto) throws ParseException {
        return new ResponseEntity<>(courseService.saveCourse(courseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentDto>> getFullCourseById(@PathVariable("id") long id) {
        List<StudentDto> students = courseService.getAllStudentsFromCourse(id);
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<CourseDto>> getEmptyCourses() {
        List<CourseDto> courses = courseService.findCoursesWithoutStudents();
        if (courses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/full")
    public ResponseEntity<List<CourseFullDto>> getCoursesAndStudentsRelationship() {
        List<CourseFullDto> courses = courseService.findCoursesWithStudents();
        if (courses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }


}
