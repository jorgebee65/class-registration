package com.jorge.api.request;

import com.jorge.api.model.Course;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class StudentRequest {
    private String name;
    private Set<CourseRequest> courses;
}
