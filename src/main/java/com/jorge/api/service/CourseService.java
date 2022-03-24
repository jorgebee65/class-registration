package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAllCourses(){
        return courseRepository.findAll();
    }

    public Course findCourseById(Long id){
        return courseRepository
                .findById(id).orElseThrow(() -> new ApiRequestException("Not found Course with id = " + id));
    }

    public List<Student> getAllStudentsFromCourse(Long id){
        return courseRepository.findById(id).orElseThrow(() -> new ApiRequestException("Not found Course with id = " + id)).getStudents().stream()
                .map(student -> Student.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    public Course saveCourse(Course course){
        if(StringUtils.isEmpty(course.getName())){
            throw new ApiRequestException("The Course name is required");
        }
        if(!courseRepository.findByNameEquals(course.getName()).isEmpty()){
            throw new ApiRequestException("The Course with name "+course.getName()+" already exist");
        }
        if(course.getId()!=null){
            courseRepository.findById(course.getId())
                    .orElseThrow(()-> new ApiRequestException("Not found Course with Id: "+course.getId()));
        }
        return courseRepository.save(Course.builder()
                .name(course.getName())
                .build());
    }
/*
    public Course updateCourse(Long id, CourseRequest courseRequest){
        if(StringUtils.isEmpty(courseRequest.getName())){
            throw new ApiRequestException("The Course name is required");
        }
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new ApiRequestException("Not found Course with Id: "+id));
        course.setName(courseRequest.getName());
        return courseRepository.save(course);
    }
*/
    public void deleteCourse(Long id){
        boolean exist = courseRepository.existsById(id);
        if(!exist){
            throw new ApiRequestException("Not found Course with Id: "+id);
        }
        courseRepository.deleteById(id);
    }

    public List<Course> findCoursesWithoutStudents(){
        return courseRepository.fetchCoursesWithoutStudents().stream()
                .map(iEmptyCourse -> Course.builder()
                        .id(iEmptyCourse.getId())
                        .name(iEmptyCourse.getName())
                        .build()).collect(Collectors.toList());
    }

    public List<Course> findCoursesWithStudents(){
        return courseRepository.findAll().stream().filter(c -> !c.getStudents().isEmpty()).collect(Collectors.toList());
    }
}
