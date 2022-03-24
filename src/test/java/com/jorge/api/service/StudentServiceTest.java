package com.jorge.api.service;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import com.jorge.api.repository.EmptyEntityImpl;
import com.jorge.api.repository.IEmptyEntity;
import com.jorge.api.repository.StudentRepository;
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
        Student studentFound = studentService.findStudentById(1L);
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
        List<Course> courses = studentService.findCoursesByStudent(1L);
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
        Student studentFound = studentService.saveStudent(Student.builder().name("Jorge").email("email@gmail.com").build());
        //Then
        assertNotNull(studentFound);
        assertSame("Jorge", studentFound.getName());
    }

    @Test
    public void saveShouldThrowAnExceptionWhenNameIsMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            studentService.saveStudent(Student.builder().name(null).build());
        });
        String expectedMessage = "The Student name is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        //Given
        Student c = Student.builder().id(1L).name("Jorge").build();
        when(studentRepository.findById(any())).thenReturn(Optional.ofNullable(c));
        Student cu = Student.builder().id(1L).name("Jorge2").build();
        when(studentRepository.save(any())).thenReturn(cu);
        //When
        Student studentFound = studentService.saveStudent(Student.builder().id(1L).name("Jorge2").email("email@gmail.com").build());
        //Then
        assertNotNull(studentFound);
        assertSame("Jorge2", studentFound.getName());
    }

    @Test
    public void updateShouldThrowAnExceptionWhenNameIsMissing() {
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            studentService.saveStudent(Student.builder().id(1L).name(null).build());
        });
        String expectedMessage = "The Student name is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        //Given
        when(studentRepository.existsById(any())).thenReturn(true);
        doNothing().when(studentRepository).deleteById(any());
        //When
        studentService.deleteStudent(1L);
        //Then
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteShouldThrowAnExceptionWhenStudentDoesNotExist() {
        when(studentRepository.existsById(any())).thenReturn(false);
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            studentService.deleteStudent(1L);
        });
        String expectedMessage = "Student with Id: 1 does not exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getStudentsWithoutCourses() {
        //Given
        List<IEmptyEntity> emptyEntities = Stream.of(new EmptyEntityImpl(1L, "Jorge")).collect(Collectors.toList());
        when(studentRepository.fetchStudentsWithoutCourses()).thenReturn(emptyEntities);
        //When
        List<Student> coursesFound = studentService.findStudentsWithoutCourses();
        //Then
        assertEquals(1, coursesFound.size());
    }
}