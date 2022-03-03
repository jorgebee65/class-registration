package com.jorge.api.repository;

import com.jorge.api.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCoursesByStudentsId(Long courseId);
}
