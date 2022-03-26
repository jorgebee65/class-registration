package com.jorge.api.service;

import com.jorge.api.dto.CourseDto;
import com.jorge.api.dto.StudentDto;
import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.mapper.CourseConverter;
import com.jorge.api.mapper.StudentConverter;
import com.jorge.api.model.Student;
import com.jorge.api.repository.StudentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final CourseConverter courseConverter;

    public StudentService(StudentRepository studentRepository, StudentConverter studentConverter, CourseConverter courseConverter) {
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
        this.courseConverter = courseConverter;
    }

    public List<StudentDto> getAllStudents() {
        return studentConverter.convertToDto(studentRepository.findAll());
    }

    public StudentDto findStudentById(Long id) {
        return studentConverter.convertToDto(studentRepository.findById(id).orElseThrow(() -> new ApiRequestException("Not found Student with id = " + id)));
    }

    public List<CourseDto> findCoursesByStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ApiRequestException("Not found Student with id = " + id));
        return courseConverter.convertToDto(student.getCourses());
    }

    public StudentDto saveStudent(StudentDto studentDto) {
        if (StringUtils.isEmpty(studentDto.getName())) {
            throw new ApiRequestException("The Student name is required");
        }
        if (StringUtils.isEmpty(studentDto.getEmail())) {
            throw new ApiRequestException("The Email is required");
        }
        if (studentDto.getId() != null) {
            studentRepository.findById(studentDto.getId())
                    .orElseThrow(() -> new ApiRequestException("Not found Student with Id: " + studentDto.getId()));
        }
        Student studentEntity;
        try {
            studentEntity = studentConverter.convertToEntity(studentDto);
        } catch (ParseException e) {
            throw new ApiRequestException(String.format("Can not convert to student: %s", studentDto.toString()));
        }
        return studentConverter.convertToDto(studentRepository.save(studentEntity));
    }

    public void deleteStudent(Long id) {
        boolean exist = studentRepository.existsById(id);
        if (!exist) {
            throw new ApiRequestException("Student with Id: " + id + " does not exist");
        }
        studentRepository.deleteById(id);
    }

    public List<StudentDto> findStudentsWithoutCourses() {
        return studentConverter.convertToDto(studentRepository.fetchStudentsWithoutCourses().stream()
                .map(iEmptyCourse -> Student.builder()
                        .id(iEmptyCourse.getId())
                        .name(iEmptyCourse.getName())
                        .build()).collect(Collectors.toList()));
    }

}
