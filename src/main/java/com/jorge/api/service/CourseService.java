package com.jorge.api.service;

import com.jorge.api.exception.ResourceNotFoundException;
import com.jorge.api.model.Course;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.CourseRequest;
import com.jorge.api.response.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id){
        return courseRepository.findById(id);
    }

    public List<StudentResponse> getAllStudentsFromCourse(Long id){
        return studentRepository.findStudentsByCoursesId(id).stream()
                .map(student -> StudentResponse.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    public Course save(CourseRequest courseRequest){
        return courseRepository.save(Course.builder()
                .name(courseRequest.getName())
                .build());
    }

    public Course update(Long id, CourseRequest courseRequest){
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Not found Course with Id: "+id));
        course.setName(courseRequest.getName());
        return courseRepository.save(course);
    }

    public void delete(Long id){
        courseRepository.deleteById(id);
    }

}
