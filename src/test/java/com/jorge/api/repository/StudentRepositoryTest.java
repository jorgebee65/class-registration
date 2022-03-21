package com.jorge.api.repository;

import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void itShouldFetchStudentsWithoutCourses() {
        //given
        Student student = Student.builder()
                .name("Jorge")
                .build();
        studentRepository.save(student);
        //when
        List<IEmptyEntity> emptyEntities = studentRepository.fetchStudentsWithoutCourses();
        //then
        assertFalse(emptyEntities.isEmpty());
    }

}