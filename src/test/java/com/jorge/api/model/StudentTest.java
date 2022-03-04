package com.jorge.api.model;

import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.IEmptyEntity;
import com.jorge.api.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @Order(2)
    @Rollback(value = false)
    public void createNewStudentWithoutCourses() {
        Student studentToSave = Student.builder()
                .name("Jaime Duende")
                .build();
        Student studentSaved = studentRepository.save(studentToSave);
        assertNotNull(studentSaved);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    public void createNewStudentWithTwoCourses() {
        Optional<Course> course = courseRepository.findById(2L);
        Optional<Course> course2 = courseRepository.findById(3L);
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
    @Order(4)
    public void getStudentsWithoutCourses() {
        List<IEmptyEntity> iEmptyEntities = studentRepository.fetchStudentsWithoutCourses();
        assertTrue(!iEmptyEntities.isEmpty());
        assertTrue(iEmptyEntities.size()==1);
        assertTrue(iEmptyEntities.get(0).getName().equals("Jaime Duende"));
    }

}
