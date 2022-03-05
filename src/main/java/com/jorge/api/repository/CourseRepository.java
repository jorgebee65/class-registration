package com.jorge.api.repository;

import com.jorge.api.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCoursesByStudentsId(Long studentId);

    @Query(value = "SELECT A.id AS id, A.name as name FROM courses as A LEFT JOIN student_courses as B ON A.id = B.course_id WHERE B.student_id is NULL", nativeQuery = true)
    List<IEmptyEntity> fetchCoursesWithoutStudents();
}
