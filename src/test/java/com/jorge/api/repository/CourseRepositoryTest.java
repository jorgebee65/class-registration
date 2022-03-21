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
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void itShouldFetchCoursesWithoutStudents() {
        //given
        Course course = Course.builder().name("Java").build();
        courseRepository.saveAndFlush(course);
        //when
        List<IEmptyEntity> emptyEntities = courseRepository.fetchCoursesWithoutStudents();
        //then
        assertFalse(emptyEntities.isEmpty());
    }

}