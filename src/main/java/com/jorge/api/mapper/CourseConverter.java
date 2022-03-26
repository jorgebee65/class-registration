package com.jorge.api.mapper;

import com.jorge.api.dto.CourseDto;
import com.jorge.api.dto.CourseFullDto;
import com.jorge.api.model.Course;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseConverter {

    private final ModelMapper modelMapper;

    public CourseConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CourseFullDto convertToFullDto(Course course) {
        return modelMapper.map(course, CourseFullDto.class);
    }

    public List<CourseFullDto> convertToFullDto(Collection<Course> courses) {
        return courses.stream().map(this::convertToFullDto).collect(Collectors.toList());
    }
    public CourseDto convertToDto(Course course) {
        return modelMapper.map(course, CourseDto.class);
    }

    public List<CourseDto> convertToDto(Collection<Course> courses) {
        return courses.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Course convertToEntity(CourseDto courseDto) throws ParseException {
        return modelMapper.map(courseDto, Course.class);
    }
}
