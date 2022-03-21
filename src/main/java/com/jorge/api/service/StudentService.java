package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Student;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.StudentRequest;
import com.jorge.api.response.CourseResponse;
import com.jorge.api.response.StudentResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id){
        return studentRepository.findById(id).orElseThrow(() -> new ApiRequestException("Not found Student with id = " + id));
    }

    public List<CourseResponse> getCoursesByStudent(Long id){
        return getStudentById(id).getCourses().stream()
                .map(course -> CourseResponse.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    public Student save(StudentRequest studentRequest){
        if(StringUtils.isEmpty(studentRequest.getName())){
            throw new ApiRequestException("The Student name is required");
        }
        return studentRepository.save(Student.builder()
                .name(studentRequest.getName())
                .build());
    }

    public Student update(Long id, StudentRequest studentRequest){
        if(StringUtils.isEmpty(studentRequest.getName())){
            throw new ApiRequestException("The Student name is required");
        }
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new ApiRequestException("Not found Student with Id: "+id));
        student.setName(studentRequest.getName());
        return studentRepository.save(student);
    }

    public void delete(Long id){
        boolean exist = studentRepository.existsById(id);
        if(!exist){
            throw new ApiRequestException("Student with Id: "+id+" does not exist");
        }
        studentRepository.deleteById(id);
    }

    public List<StudentResponse> getStudentsWithoutCourses(){
        return studentRepository.fetchStudentsWithoutCourses().stream()
                .map(iEmptyCourse -> StudentResponse.builder()
                        .id(iEmptyCourse.getId())
                        .name(iEmptyCourse.getName())
                        .build()).collect(Collectors.toList());
    }

}
