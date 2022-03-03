package com.jorge.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                CascadeType.MERGE,
                CascadeType.PERSIST
    })
    @JoinTable(
            name = "Student_Courses",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course){
        if(courses == null){
            courses = new HashSet<>();
        }
        courses.add(course);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
