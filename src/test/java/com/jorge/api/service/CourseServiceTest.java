package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.EmptyEntityImpl;
import com.jorge.api.repository.IEmptyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    public void saveShouldThrowAnExceptionWhenNameIsMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            courseService.saveCourse(Course.builder().name(null).build());
        });
        String expectedMessage = "The Course name is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void saveShouldThrowAnExceptionWhenNameIsDuplicated() {
        when(courseRepository.findByNameEquals(any()))
                .thenReturn(Stream.of(Course.builder().id(1L).name("Java").build()).collect(Collectors.toList()));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            courseService.saveCourse(Course.builder().name("Java").build());
        });
        String expectedMessage = "The Course with name Java already exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getAllCourses() {
        //Given
        List<Course> courses = Stream.of(Course.builder().id(1L).name("Java").build()).collect(Collectors.toList());
        when(courseRepository.findAll()).thenReturn(courses);
        //When
        List<Course> coursesFound = courseService.findAllCourses();
        //Then
        assertEquals(1, coursesFound.size());
    }

    @Test
    void getCourseById() {
        //Given
        Course c = Course.builder().id(1L).name("Java").build();
        when(courseRepository.findById(1L)).thenReturn(Optional.ofNullable(c));
        //When
        Course courseFound = courseService.findCourseById(1L);
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
        List<Student> students = courseService.getAllStudentsFromCourse(1L);
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
        Course courseFound = courseService.saveCourse(Course.builder().name("Java").build());
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
        Course courseUpdated = courseService.saveCourse( Course.builder().id(1L).name("Java2").build());
        //Then
        assertNotNull(courseUpdated);
        assertSame("Java2", courseUpdated.getName());
    }

    @Test
    public void updateShouldThrowAnExceptionWhenNameIsMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            courseService.saveCourse( Course.builder().id(1L).name(null).build());
        });
        String expectedMessage = "The Course name is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        //Given
        when(courseRepository.existsById(any())).thenReturn(true);
        doNothing().when(courseRepository).deleteById(any());
        //When
        courseService.deleteCourse(1L);
        //Then
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteShouldThrowAnExceptionWhenCourseDoesNotExist() {
        when(courseRepository.existsById(any())).thenReturn(false);
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            courseService.deleteCourse(1L);
        });
        String expectedMessage = "Not found Course with Id: 1";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCoursesWithoutStudents() {
        //Given
        List<IEmptyEntity> emptyEntities = Stream.of(new EmptyEntityImpl(1L, "Java")).collect(Collectors.toList());
        when(courseRepository.fetchCoursesWithoutStudents()).thenReturn(emptyEntities);
        //When
        List<Course> coursesFound = courseService.findCoursesWithoutStudents();
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
        List<Course> students = courseService.findCoursesWithStudents();
        //Then
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
    }
}