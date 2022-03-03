package com.jorge.api.service;

import com.jorge.api.exception.ResourceNotFoundException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Student save(RegisterRequest registerRequest){
        Student student = studentRepository.findById(registerRequest.getStudentId())
                .orElseThrow(()-> new ResourceNotFoundException("Not found Student with Id: "+registerRequest.getStudentId()));
        List<Course> coursesRegistered = courseRepository.findCoursesByStudentsId(student.getId());
        if(coursesRegistered.size()+registerRequest.getCourses().size() > 5) {
            throw new ResourceNotFoundException( "Only allowed a maximum of 5 Courses by Student" );
        }
            for(RegisterRequest.CourseRequest courseRequest: registerRequest.getCourses()){
                Course c = courseRepository.getById(courseRequest.getId());
                student.addCourse(c);
            }
            return studentRepository.save(student);
    }

}
