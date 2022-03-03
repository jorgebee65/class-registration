package com.jorge.api.service;

import com.jorge.api.exception.ResourceNotFoundException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.CourseRequest;
import com.jorge.api.request.StudentRequest;
import com.jorge.api.response.CourseResponse;
import com.jorge.api.response.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id){
        return studentRepository.findById(id);
    }

    public List<CourseResponse> getCoursesByStudent(Long id){
        return courseRepository.findCoursesByStudentsId(id).stream()
                .map(course -> CourseResponse.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    public Student save(StudentRequest studentRequest){
        return studentRepository.save(Student.builder()
                .name(studentRequest.getName())
                .build());
    }

    public Student update(Long id, StudentRequest studentRequest){
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Not found Student with Id: "+id));
        student.setName(studentRequest.getName());
        return studentRepository.save(student);
    }

    public void delete(Long id){
        studentRepository.deleteById(id);
    }

}
