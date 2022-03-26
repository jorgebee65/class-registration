package com.jorge.api.mapper;

import com.jorge.api.dto.CourseFullDto;
import com.jorge.api.dto.StudentDto;
import com.jorge.api.model.Course;
import com.jorge.api.model.Student;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentConverter {

    private final ModelMapper modelMapper;

    public StudentConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public StudentDto convertToDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public Student convertToEntity(StudentDto studentDto) throws ParseException {
        return modelMapper.map(studentDto, Student.class);
    }

    public List<StudentDto> convertToDto(Collection<Student> student) {
        return student.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
