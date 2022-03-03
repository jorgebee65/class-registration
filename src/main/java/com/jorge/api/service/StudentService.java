package com.jorge.api.service;

import com.jorge.api.model.Student;
import com.jorge.api.repository.StudentRepository;
import com.jorge.api.request.StudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public void saveStudent(StudentRequest studentRequest){
        studentRepository.save(Student.builder()
                .name(studentRequest.getName())
                .build());
    }

}
