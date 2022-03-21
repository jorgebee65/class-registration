package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.request.CourseRequest;
import com.jorge.api.response.CourseFullResponse;
import com.jorge.api.response.CourseResponse;
import com.jorge.api.response.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id){
        return courseRepository
                .findById(id).orElseThrow(() -> new ApiRequestException("Not found Course with id = " + id));
    }

    public List<StudentResponse> getAllStudentsFromCourse(Long id){
        return courseRepository.findById(id).orElseThrow(() -> new ApiRequestException("Not found Course with id = " + id)).getStudents().stream()
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
                .orElseThrow(()-> new ApiRequestException("Not found Course with Id: "+id));
        course.setName(courseRequest.getName());
        return courseRepository.save(course);
    }

    public void delete(Long id){
        boolean exist = courseRepository.existsById(id);
        if(!exist){
            throw new ApiRequestException("Not found Course with Id: "+id);
        }
        courseRepository.deleteById(id);
    }

    public List<CourseResponse> getCoursesWithoutStudents(){
        return courseRepository.fetchCoursesWithoutStudents().stream()
                .map(iEmptyCourse -> CourseResponse.builder()
                        .id(iEmptyCourse.getId())
                        .name(iEmptyCourse.getName())
                        .build()).collect(Collectors.toList());
    }

    public List<CourseFullResponse> getCoursesWithStudents(){
        List<CourseFullResponse> courses = new ArrayList<>();
        courseRepository.findAll().forEach(course -> courses.add(CourseFullResponse.builder()
                .id(course.getId())
                        .name(course.getName())
                                .students( course.getStudents().stream()
                                        .map(student -> StudentResponse.builder()
                                                .id(student.getId())
                                                .name(student.getName())
                                                .build()
                                        ).collect(Collectors.toList())
                                )
                .build()));
        return courses;
    }
}
