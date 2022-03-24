package com.jorge.api.controller;

import com.jorge.api.dto.StudentDto;
import com.jorge.api.model.Student;
import com.jorge.api.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    private final ModelMapper modelMapper;

    public StudentController(StudentService studentService, ModelMapper modelMapper) {
        this.studentService = studentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents(){
        List<StudentDto> students = studentService.getAllStudents().stream().map(this::convertToDto).collect(Collectors.toList());

        if(students.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") long id) {
        return new ResponseEntity<>( convertToDto(studentService.findStudentById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto studentDto) throws ParseException {
        Student _student = studentService.saveStudent(convertToEntity(studentDto));
        return new ResponseEntity<>(convertToDto(_student), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable("id") long id, @RequestBody StudentDto studentDto) throws ParseException {
        Student _student = studentService.saveStudent(convertToEntity(studentDto));
        return new ResponseEntity<>(convertToDto(_student), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<StudentDto> getCoursesByStudentId(@PathVariable("id") long id) {
        return new ResponseEntity<>( convertToDto(studentService.findStudentById(id)), HttpStatus.OK);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<StudentDto>> getStudentsWithoutCourses(){
        List<StudentDto> courses = studentService.findStudentsWithoutCourses().stream().map(this::convertToDto).collect(Collectors.toList());
        if(courses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    private StudentDto convertToDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    private Student convertToEntity(StudentDto studentDto) throws ParseException {
        return modelMapper.map(studentDto, Student.class);
    }

}
