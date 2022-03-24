package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.StudentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Student findStudentById(Long id){
        return studentRepository.findById(id).orElseThrow(() -> new ApiRequestException("Not found Student with id = " + id));
    }

    public List<Course> findCoursesByStudent(Long id){
        return findStudentById(id).getCourses().stream()
                .map(course -> Course.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    public Student saveStudent(Student student){
        if(StringUtils.isEmpty(student.getName())){
            throw new ApiRequestException("The Student name is required");
        }
        if(StringUtils.isEmpty(student.getEmail())){
            throw new ApiRequestException("The Email is required");
        }
        if(student.getId()!=null){
            studentRepository.findById(student.getId())
                    .orElseThrow(()-> new ApiRequestException("Not found Student with Id: "+student.getId()));
        }
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id){
        boolean exist = studentRepository.existsById(id);
        if(!exist){
            throw new ApiRequestException("Student with Id: "+id+" does not exist");
        }
        studentRepository.deleteById(id);
    }

    public List<Student> findStudentsWithoutCourses(){
        return studentRepository.fetchStudentsWithoutCourses().stream()
                .map(iEmptyCourse -> Student.builder()
                        .id(iEmptyCourse.getId())
                        .name(iEmptyCourse.getName())
                        .build()).collect(Collectors.toList());
    }

}
