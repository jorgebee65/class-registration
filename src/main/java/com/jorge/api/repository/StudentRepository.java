package com.jorge.api.repository;

import com.jorge.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findStudentsByCoursesId(Long studentId);
}
