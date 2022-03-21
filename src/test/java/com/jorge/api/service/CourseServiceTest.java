package com.jorge.api.service;

import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.EmptyEntity;
import com.jorge.api.request.CourseRequest;
import com.jorge.api.response.CourseFullResponse;
import com.jorge.api.response.CourseResponse;
import com.jorge.api.response.StudentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    void getAllCourses() {
        //Given
        List<Course> courses = Stream.of(Course.builder().id(1L).name("Java").build()).collect(Collectors.toList());
        when(courseRepository.findAll()).thenReturn(courses);
        //When
        List<Course> coursesFound = courseService.getAllCourses();
        //Then
        assertEquals(1, coursesFound.size());
    }

    @Test
    void getCourseById() {
        //Given
        Course c = Course.builder().id(1L).name("Java").build();
        when(courseRepository.findById(1L)).thenReturn(Optional.ofNullable(c));
        //When
        Course courseFound = courseService.getCourseById(1L);
        //Then
        assertNotNull(courseFound);
        assertSame("Java", courseFound.getName());
    }

    @Test
    void getAllStudentsFromCourse() {
        //Given
        Course c = Course.builder()
                .id(1L)
                .name("Java")
                .students(Stream.of(Student.builder().id(1L).name("Jorge").build()).collect(Collectors.toSet()))
                .build();
        when(courseRepository.findById(1L)).thenReturn(Optional.ofNullable(c));
        //When
        List<StudentResponse> students = courseService.getAllStudentsFromCourse(1L);
        //Then
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
    }

    @Test
    void save() {
        //Given
        Course c = Course.builder().id(1L).name("Java").build();
        when(courseRepository.save(any())).thenReturn(c);
        //When
        Course courseFound = courseService.save(CourseRequest.builder().name("Java").build());
        //Then
        assertNotNull(courseFound);
        assertSame("Java", courseFound.getName());
    }

    @Test
    void update() {
        //Given
        Course c = Course.builder().id(1L).name("Java").build();
        when(courseRepository.findById(1L)).thenReturn(Optional.ofNullable(c));
        Course cu = Course.builder().id(1L).name("Java2").build();
        when(courseRepository.save(any())).thenReturn(cu);
        //When
        Course courseUpdated = courseService.update(1L, CourseRequest.builder().name("Java2").build());
        //Then
        assertNotNull(courseUpdated);
        assertSame("Java2", courseUpdated.getName());
    }

    @Test
    void delete() {
        //Given
        when(courseRepository.existsById(any())).thenReturn(true);
        doNothing().when(courseRepository).deleteById(any());
        //When
        courseService.delete(1L);
        //Then
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void getCoursesWithoutStudents() {
        //Given
        List<EmptyEntity> emptyEntities = Stream.of(EmptyEntity.builder().id(1L).name("Java").build()).collect(Collectors.toList());
        when(courseRepository.fetchCoursesWithoutStudents()).thenReturn(emptyEntities);
        //When
        List<CourseResponse> coursesFound = courseService.getCoursesWithoutStudents();
        //Then
        assertEquals(1, coursesFound.size());
    }

    @Test
    void getCoursesWithStudents() {
        //Given
        Course c = Course.builder()
                .id(1L)
                .name("Java")
                .students(Stream.of(Student.builder().id(1L).name("Jorge").build()).collect(Collectors.toSet()))
                .build();
        when(courseRepository.findAll()).thenReturn(Collections.singletonList(c));
        //When
        List<CourseFullResponse> students = courseService.getCoursesWithStudents();
        //Then
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
    }
}