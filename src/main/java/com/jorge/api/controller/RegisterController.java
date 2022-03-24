package com.jorge.api.controller;

import com.jorge.api.dto.RegisterDto;
import com.jorge.api.model.Student;
import com.jorge.api.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Student> createCourse(@RequestBody RegisterDto registerRequest) {
       Student _student = registerService.save(registerRequest);
       return new ResponseEntity<>(_student, HttpStatus.CREATED);
    }


}
