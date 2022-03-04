package com.jorge.api.controller;

import com.jorge.api.exception.ApiRequestException;
import com.jorge.api.model.Student;
import com.jorge.api.request.RegisterRequest;
import com.jorge.api.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<Student> createCourse(@RequestBody RegisterRequest registerRequest) {
       Student _student = registerService.save(registerRequest);
       return new ResponseEntity<>(_student, HttpStatus.CREATED);
    }

}
