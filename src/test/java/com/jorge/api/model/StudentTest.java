package com.jorge.api.model;

import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StudentTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void createNewGroup() {
        Course course = Course.builder()
                .name("Programming")
                .build();

        Course course2 = Course.builder()
                .name("Data Bases")
                .build();

        Course course3 = Course.builder()
                .name("Algorithms")
                .build();

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(course2);
        courses.add(course3);

        courseRepository.saveAll(courses);
    }

    @Test
    public void createNewStudent() {
        Optional<Course> course = courseRepository.findById((long)1);
        assertTrue(course.isPresent());
        Student studentToSave = Student.builder()
                .name("Jaime Duende")
                .build();
        studentToSave.addCourse(course.get());
        Student studentSaved = studentRepository.save(studentToSave);
        assertNotNull(studentSaved);
    }

    @Test
    public void createNewStudentWithTwoCourses() {
        Optional<Course> course = courseRepository.findById((long)2);
        Optional<Course> course2 = courseRepository.findById((long)3);
        assertTrue(course.isPresent());
        assertTrue(course2.isPresent());
        Student studentToSave = Student.builder()
                .name("Pelusa Caligari")
                .build();
        studentToSave.addCourse(course.get());
        studentToSave.addCourse(course2.get());
        Student studentSaved = studentRepository.save(studentToSave);
        assertNotNull(studentSaved);
    }

    @Test
    public void getAllCoursesByStudents() {
        log.info("Print all students");
        List<Student> students = studentRepository.findAll();
        students.forEach(s -> {
            log.info("Student: {}", s.getName());
            courseRepository.findCoursesByStudentsId(s.getId()).forEach(c-> log.info("Curso: {}",c.getName()));
        });


    }
}
