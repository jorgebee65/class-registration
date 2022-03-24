package com.jorge.api.service;

import com.jorge.api.dto.RegisterDto;
import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestPropertySource(properties = {
        "app.max-courses-per-student=3",
        "app.max-students-per-course=5"
})
public class RegisterServiceTest {

    @Autowired
    private RegisterService registerService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    public void itShouldThrowAnExceptionWhenStudentIdIsMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(RegisterDto.builder().studentId(null).build());
        });
        String expectedMessage = "Student ID is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenCoursesAreMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(RegisterDto.builder().studentId(1L).build());
        });
        String expectedMessage = "Please provide the list of courses to register";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenACourseIdIsMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(RegisterDto.builder().studentId(1L)
                    .courses(Stream.of(
                            RegisterDto.CourseRequest.builder()
                                    .name("React")
                                    .build()
                    ).collect(Collectors.toSet()))
                    .build());
        });
        String expectedMessage = "Course ID is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenACourseNameIsMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(RegisterDto.builder().studentId(1L)
                    .courses(Stream.of(
                            RegisterDto.CourseRequest.builder()
                                    .id(3L)
                                    .build()
                    ).collect(Collectors.toSet()))
                    .build());
        });
        String expectedMessage = "Course name is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenStudentIdIsNotFounded() {
        //Given
        RegisterDto registerRequest = RegisterDto.builder()
                .studentId(9L)
                .courses(Stream.of(
                        RegisterDto.CourseRequest.builder()
                                .id(3L)
                                .name("React")
                                .build()
                ).collect(Collectors.toSet()))
                .build();
        //When
        when(studentRepository.findById(any()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(registerRequest);
        });
        String expectedMessage = "Not found Student with Id:";
        String actualMessage = exception.getMessage();
        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenTryToExceedTheMaximumCoursesNumberByStudent() {
        //Given
        RegisterDto registerRequest = RegisterDto.builder()
                .studentId(1L)
                .courses(Stream.of(
                        RegisterDto.CourseRequest.builder()
                                .id(4L)
                                .name("JUnit")
                                .build()
                ).collect(Collectors.toSet()))
                .build();
        Student studentWith3Courses = Student.builder()
                .id(1L)
                .name("Jose Smith")
                .courses(Stream.of(
                        Course.builder().id(1L).name("Java").build(),
                        Course.builder().id(2L).name("PLSQL").build(),
                        Course.builder().id(3L).name("React").build()
                ).collect(Collectors.toSet()))
                .build();
        //When
        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(studentWith3Courses));
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(registerRequest);
        });
        String expectedMessage = "Only allowed a maximum of";
        String actualMessage = exception.getMessage();
        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenTryToExceedTheMaximumStudentsNumberByCourse() {
        //Given
        RegisterDto registerRequest = RegisterDto.builder()
                .studentId(1L)
                .courses(Stream.of(
                        RegisterDto.CourseRequest.builder()
                                .id(4L)
                                .name("JUnit")
                                .build()
                ).collect(Collectors.toSet()))
                .build();
        Student student = Student.builder()
                .id(1L)
                .name("Jose Smith")
                .courses(Stream.of(
                        Course.builder().id(1L).name("Java").build()
                ).collect(Collectors.toSet()))
                .build();

        Course courseWith5Students = Course.builder().id(4L).name("JUnit").students(
                Stream.of(
                        Student.builder().id(1L).name("Jaime").build(),
                        Student.builder().id(2L).name("Jacob").build(),
                        Student.builder().id(3L).name("Julia").build(),
                        Student.builder().id(4L).name("Jules").build(),
                        Student.builder().id(5L).name("Jude").build()
                ).collect(Collectors.toSet())
        ).build();
        //When
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(courseRepository.findById(4L)).thenReturn(Optional.of(courseWith5Students));
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(registerRequest);
        });
        String expectedMessage = "The Course JUnit is full, it only accepts";
        String actualMessage = exception.getMessage();
        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenACourseNameDoesNotMatchWithTheRequested() {
        //Given
        RegisterDto registerRequest = RegisterDto.builder()
                .studentId(1L)
                .courses(Stream.of(
                        RegisterDto.CourseRequest.builder()
                                .id(4L)
                                .name("JUnit")
                                .build()
                ).collect(Collectors.toSet()))
                .build();
        Student student = Student.builder()
                .id(1L)
                .name("Jose Smith")
                .courses(Stream.of(
                        Course.builder().id(1L).name("Java").build()
                ).collect(Collectors.toSet()))
                .build();

        Course course = Course.builder().id(4L).name("Spring").students(
                Stream.of(
                        Student.builder().id(1L).name("Jaime").build()
                ).collect(Collectors.toSet())
        ).build();
        //When
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(courseRepository.findById(4L)).thenReturn(Optional.of(course));
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(registerRequest);
        });
        String expectedMessage = "The name of the course doesn't match, requested: 'JUnit' with: 'Spring'";
        String actualMessage = exception.getMessage();
        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void itShouldThrowAnExceptionWhenTryToRegisterADuplicatedCourse() {
        //Given
        RegisterDto registerRequest = RegisterDto.builder()
                .studentId(1L)
                .courses(Stream.of(
                        RegisterDto.CourseRequest.builder()
                                .id(4L)
                                .name("JUnit")
                                .build()
                ).collect(Collectors.toSet()))
                .build();
        Student student = Student.builder()
                .id(1L)
                .name("Jose Smith")
                .courses(Stream.of(
                        Course.builder().id(4L).name("JUnit").build()
                ).collect(Collectors.toSet()))
                .build();

        Course course = Course.builder().id(4L).name("JUnit").students(
                Stream.of(
                        Student.builder().id(1L).name("Jaime").build()
                ).collect(Collectors.toSet())
        ).build();
        //When
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(courseRepository.findById(4L)).thenReturn(Optional.of(course));
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            registerService.save(registerRequest);
        });
        String expectedMessage = "You are trying to register a duplicate Course: JUnit";
        String actualMessage = exception.getMessage();
        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void registerUserSuccessful() {
        //Given
        RegisterDto registerRequest = RegisterDto.builder()
                .studentId(1L)
                .courses(Stream.of(
                        RegisterDto.CourseRequest.builder()
                                .id(3L)
                                .name("React")
                                .build()
                ).collect(Collectors.toSet()))
                .build();
        //When
        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(
                        Student.builder()
                                .id(1L)
                                .name("Jose Canseco")
                                .courses(Stream.of(Course.builder().id(1L).name("Java").build(),
                                        Course.builder().id(2L).name("PLSQL").build()).collect(Collectors.toSet()))
                                .build()));
        when(courseRepository.findById(3L))
                .thenReturn(Optional.of(Course.builder().id(3L).name("React").build()));
        when(studentRepository.save(any()))
                .thenReturn(Student.builder().id(1L)
                        .name("Jose Canseco")
                        .courses(Stream.of(
                                        Course.builder().id(1L).name("Java").build(),
                                        Course.builder().id(2L).name("PLSQL").build(),
                                        Course.builder().id(3L).name("React").build()
                                )
                                .collect(Collectors.toSet()))
                        .build());
        Student student = registerService.save(registerRequest);
        assertNotNull(student);
        assertEquals(3, student.getCourses().size());
    }

}
