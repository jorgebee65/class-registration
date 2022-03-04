package com.jorge.api.repository;

import com.jorge.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findStudentsByCoursesId(Long studentId);

    @Query(value = "SELECT A.id, A.name FROM students as A LEFT JOIN student_courses as B ON A.id = B.student_id WHERE B.course_id is NULL", nativeQuery = true)
    List<IEmptyEntity> fetchStudentsWithoutCourses();
}
