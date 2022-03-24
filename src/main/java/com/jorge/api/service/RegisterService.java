package com.jorge.api.service;

import com.jorge.api.dto.RegisterDto;
import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RegisterService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Value("${app.max-courses-per-student}")
    private int maxCoursesPerStudent;

    @Value("${app.max-students-per-course}")
    private int maxStudentsPerCourse;

    public RegisterService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Student save(RegisterDto registerRequest) {
        if(null == registerRequest.getStudentId() ){
            throw new ApiRequestException("Student ID is required");
        }
        if(null == registerRequest.getCourses() || registerRequest.getCourses().isEmpty() ){
            throw new ApiRequestException("Please provide the list of courses to register");
        }
        registerRequest.getCourses().forEach(c->{
            if(null == c.getId()){
                throw new ApiRequestException("Course ID is required");
            }
            if(StringUtils.isEmpty(c.getName())){
                throw new ApiRequestException("Course name is required");
            }
        });
        Student student = studentRepository.findById(registerRequest.getStudentId())
                .orElseThrow(()-> new ApiRequestException("Not found Student with Id: "+registerRequest.getStudentId()));
        if(student.getCourses().size()+registerRequest.getCourses().size() > maxCoursesPerStudent) {
            throw new ApiRequestException( "Only allowed a maximum of "+maxCoursesPerStudent+" Courses by Student, currently you have "
                    +student.getCourses().size()+" courses and you are trying to register "+registerRequest.getCourses().size()+" more.");
        }
            for(RegisterDto.CourseRequest courseRequest: registerRequest.getCourses()){
                Course c = courseRepository.findById(courseRequest.getId()).orElseThrow(()-> new ApiRequestException("Not found Course with Id: "+courseRequest.getId()));
                if(c.getStudents().size()>=maxStudentsPerCourse){
                    throw new ApiRequestException("The Course "+c.getName()+" is full, it only accepts "+maxStudentsPerCourse+ " students");
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
