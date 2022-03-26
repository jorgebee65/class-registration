package com.jorge.api.service;

import com.jorge.api.dto.CourseDto;
import com.jorge.api.dto.CourseFullDto;
import com.jorge.api.dto.StudentDto;
import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.mapper.CourseConverter;
import com.jorge.api.mapper.StudentConverter;
import com.jorge.api.model.Course;
import com.jorge.api.repository.CourseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentConverter studentConverter;
    private final CourseConverter courseConverter;

    public CourseService(CourseRepository courseRepository, StudentConverter studentConverter, CourseConverter courseConverter) {
        this.courseRepository = courseRepository;
        this.studentConverter = studentConverter;
        this.courseConverter = courseConverter;
    }

    public List<CourseDto> findAllCourses() {
        return courseConverter.convertToDto(courseRepository.findAll());
    }

    public CourseFullDto findCourseById(Long id) {
        return courseConverter.convertToFullDto(
                courseRepository.findById(id)
                        .orElseThrow(() -> new ApiRequestException("Not found Course with id = " + id)
                        ));
    }

    public List<StudentDto> getAllStudentsFromCourse(Long id) {
        return studentConverter.convertToDto(
                courseRepository.findById(id).orElseThrow(() -> new ApiRequestException("Not found Course with id = " + id))
                        .getStudents());
    }

    public CourseDto saveCourse(CourseDto courseDto) {
        if (StringUtils.isEmpty(courseDto.getName())) {
            throw new ApiRequestException("The Course name is required");
        }
        if (!courseRepository.findByNameEquals(courseDto.getName()).isEmpty()) {
            throw new ApiRequestException("The Course with name " + courseDto.getName() + " already exist");
        }
        if (courseDto.getId() != null) {
            courseRepository.findById(courseDto.getId())
                    .orElseThrow(() -> new ApiRequestException("Not found Course with Id: " + courseDto.getId()));
        }
        Course entity;
        try {
            entity = courseConverter.convertToEntity(courseDto);
        } catch (ParseException e) {
            throw new ApiRequestException(String.format("Can not convert to course: %s", courseDto.toString()));
        }
        return courseConverter.convertToDto(courseRepository.save(entity));
    }

    public void deleteCourse(Long id) {
        boolean exist = courseRepository.existsById(id);
        if (!exist) {
            throw new ApiRequestException("Not found Course with Id: " + id);
        }
        courseRepository.deleteById(id);
    }

    public List<CourseDto> findCoursesWithoutStudents() {
        return courseConverter.convertToDto(courseRepository.fetchCoursesWithoutStudents().stream()
                .map(iEmptyCourse -> Course.builder()
                        .id(iEmptyCourse.getId())
                        .name(iEmptyCourse.getName())
                        .build()).collect(Collectors.toList()));
    }

    public List<CourseFullDto> findCoursesWithStudents() {
        return courseConverter.convertToFullDto(courseRepository.findAll().stream().filter(c -> !c.getStudents().isEmpty()).collect(Collectors.toList()));
    }
}
