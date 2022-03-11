package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public RegisterService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Student save(RegisterRequest registerRequest) {
        Student student = studentRepository.findById(registerRequest.getStudentId())
                .orElseThrow(()-> new ApiRequestException("Not found Student with Id: "+registerRequest.getStudentId()));
        if(student.getCourses().size()+registerRequest.getCourses().size() > 5) {
            throw new ApiRequestException( "Only allowed a maximum of 5 Courses by Student, currently you have "
                    +student.getCourses().size()+" courses and you are trying to register "+registerRequest.getCourses().size()+" more.");
        }
            for(RegisterRequest.CourseRequest courseRequest: registerRequest.getCourses()){
                Course c = courseRepository.getById(courseRequest.getId());
                if(c.getStudents().size()>=50){
                    throw new ApiRequestException("The Course "+c.getName()+" is full.");
                }
                if(!c.getName().equals(courseRequest.getName())){
                    throw new ApiRequestException("The name of the course doesn't match, requested: '"+courseRequest.getName()+"' with: '"+c.getName()+"'");
                }
                if(student.getCourses().contains(c)){
                    throw new ApiRequestException("You are trying to register a duplicate Course: "+c.getName());
                }
                student.addCourse(c);
            }
            return studentRepository.save(student);
    }

}
