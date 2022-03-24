package com.jorge.api.dto;

import com.jorge.api.dto.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseFullDto {
    private Long id;
    private String name;

    private List<StudentDto> students;
}
