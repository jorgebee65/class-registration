package com.jorge.api.model;

import com.jorge.api.repository.CourseRepository;
import com.jorge.api.repository.IEmptyEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void createCourse() {
        Course course = Course.builder()
                .name("React")
                .build();
        Course saved = courseRepository.save(course);
        assertNotNull(saved);
    }



    @Test
    @Order(3)
    public void getEmptyCourses() {
        log.info("Empty Courses");
        List<IEmptyEntity> iEmptyEntities = courseRepository.fetchCoursesWithoutStudents();
        assertTrue(!iEmptyEntities.isEmpty());
        log.info("Empty Courses number {}",iEmptyEntities.size());
        assertTrue(iEmptyEntities.size()==3);
        iEmptyEntities.forEach(course->{
            log.info("Course name: {}", course.getName());
        });
    }
}
