package com.jorge.api.service;

import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.RegisterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterServiceTest {

    @Autowired
    private RegisterService registerService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    public void registerUserSuccessful() {
        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(
                        Student.builder()
                                .id(1L)
                                .name("Jose Canseco")
                                .courses(Stream.of(Course.builder().id(1L).name("Java").build(),
                                        Course.builder().id(2L).name("PLSQL").build()).collect(Collectors.toSet()))
                                .build()));
        when(courseRepository.getById(3L))
                .thenReturn(Course.builder().id(3L).name("React").build());
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
        Student student = registerService.save(RegisterRequest.builder()
                .studentId(1L)
                .courses(Stream.of(
                        RegisterRequest.CourseRequest.builder()
                                .id(3L)
                                .name("React")
                                .build()
                ).collect(Collectors.toSet()))
                .build()
        );
        assertNotNull(student);
        assertEquals(3, student.getCourses().size());
    }

}
