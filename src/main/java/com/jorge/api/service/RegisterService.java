package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Student save(RegisterRequest registerRequest) {
        Student student = studentRepository.findById(registerRequest.getStudentId())
                .orElseThrow(()-> new ApiRequestException("Not found Student with Id: "+registerRequest.getStudentId()));
        List<Course> coursesRegistered = courseRepository.findCoursesByStudentsId(student.getId());
        if(coursesRegistered.size()+registerRequest.getCourses().size() > 5) {
            throw new ApiRequestException( "Only allowed a maximum of 5 Courses by Student, currently you have "
                    +coursesRegistered.size()+" courses and you are trying to register "+registerRequest.getCourses().size()+" more.");
        }
            for(RegisterRequest.CourseRequest courseRequest: registerRequest.getCourses()){
                Course c = courseRepository.getById(courseRequest.getId());
                if(coursesRegistered.contains(c)){
                    throw new ApiRequestException("You are trying to register a duplicate Course: "+c.getName());
                }
                student.addCourse(c);
            }
            return studentRepository.save(student);
    }

}
