package com.jorge.api.service;

import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.EmptyEntity;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.CourseRequest;
import com.jorge.api.request.StudentRequest;
import com.jorge.api.response.CourseResponse;
import com.jorge.api.response.StudentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    void getAllStudents() {
        //Given
        List<Student> students = Stream.of(Student.builder().id(1L).name("Jorge").build()).collect(Collectors.toList());
        when(studentRepository.findAll()).thenReturn(students);
        //When
        List<Student> studentsFound = studentService.getAllStudents();
        //Then
        assertEquals(1, studentsFound.size());
    }

    @Test
    void getStudentById() {
        //Given
        Student c = Student.builder().id(1L).name("Jorge").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.ofNullable(c));
        //When
        Student studentFound = studentService.getStudentById(1L);
        //Then
        assertNotNull(studentFound);
        assertSame("Jorge", studentFound.getName());
    }

    @Test
    void getCoursesByStudent() {
        //Given
        Student c = Student.builder()
                .id(1L)
                .name("Jorge")
                .courses(Stream.of(Course.builder().id(1L).name("Java").build()).collect(Collectors.toSet()))
                .build();
        when(studentRepository.findById(1L)).thenReturn(Optional.ofNullable(c));
        //When
        List<CourseResponse> courses = studentService.getCoursesByStudent(1L);
        //Then
        assertNotNull(courses);
        assertFalse(courses.isEmpty());
        assertEquals(1, courses.size());
    }

    @Test
    void save() {
        //Given
        Student c = Student.builder().id(1L).name("Jorge").build();
        when(studentRepository.save(any())).thenReturn(c);
        //When
        Student studentFound = studentService.save(StudentRequest.builder().name("Jorge").build());
        //Then
        assertNotNull(studentFound);
        assertSame("Jorge", studentFound.getName());
    }

    @Test
    void update() {
        //Given
        Student c = Student.builder().id(1L).name("Jorge").build();
        when(studentRepository.findById(any())).thenReturn(Optional.ofNullable(c));
        Student cu = Student.builder().id(1L).name("Jorge2").build();
        when(studentRepository.save(any())).thenReturn(cu);
        //When
        Student studentFound = studentService.update(1L, StudentRequest.builder().name("Jorge2").build());
        //Then
        assertNotNull(studentFound);
        assertSame("Jorge2", studentFound.getName());
    }

    @Test
    void delete() {
        //Given
        when(studentRepository.existsById(any())).thenReturn(true);
        doNothing().when(studentRepository).deleteById(any());
        //When
        studentService.delete(1L);
        //Then
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void getStudentsWithoutCourses() {
        //Given
        List<EmptyEntity> emptyEntities = Stream.of(EmptyEntity.builder().id(1L).name("Jorge").build()).collect(Collectors.toList());
        when(studentRepository.fetchStudentsWithoutCourses()).thenReturn(emptyEntities);
        //When
        List<StudentResponse> coursesFound = studentService.getStudentsWithoutCourses();
        //Then
        assertEquals(1, coursesFound.size());
    }
}