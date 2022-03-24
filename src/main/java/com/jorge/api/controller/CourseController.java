package com.jorge.api.controller;

import com.jorge.api.dto.CourseDto;
import com.jorge.api.model.Course;
import com.jorge.api.dto.CourseFullDto;
import com.jorge.api.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    private final  ModelMapper modelMapper;

    public CourseController(CourseService courseService, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(){
        List<Course> courses = courseService.findAllCourses();

        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseFullDto> getCourseById(@PathVariable("id") long id) {
        return new ResponseEntity<>( convertToFullDto(courseService.findCourseById(id)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Course> createCourse(@RequestBody CourseDto courseDto) throws ParseException {
        Course _course = courseService.saveCourse(convertToEntity(courseDto));
        return new ResponseEntity<>(_course, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@RequestBody CourseDto courseDto) throws ParseException {
        return new ResponseEntity<>(courseService.saveCourse( convertToEntity(courseDto) ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<CourseFullDto> getFullCourseById(@PathVariable("id") long id) {
        CourseFullDto courseFullDto = convertToFullDto(courseService.findCourseById(id));
        if(courseFullDto.getStudents().isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courseFullDto, HttpStatus.OK);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<CourseDto>> getEmptyCourses(){
        List<CourseDto> courses = courseService.findCoursesWithoutStudents().stream().map(this::convertToDto).collect(Collectors.toList());

        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/full")
    public ResponseEntity<List<CourseFullDto>> getCoursesAndStudentsRelationship(){
        List<CourseFullDto> courses = courseService.findCoursesWithStudents().stream()
                .map(this::convertToFullDto).collect(Collectors.toList());
        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    private CourseFullDto convertToFullDto(Course course) {
        return modelMapper.map(course, CourseFullDto.class);
    }

    private CourseDto convertToDto(Course course) {
        return modelMapper.map(course, CourseDto.class);
    }

    private Course convertToEntity(CourseDto courseDto) throws ParseException {
        return modelMapper.map(courseDto, Course.class);
    }
}
