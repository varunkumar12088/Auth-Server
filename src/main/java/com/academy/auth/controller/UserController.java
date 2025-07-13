package com.academy.auth.controller;

import com.academy.auth.service.UserService;
import com.academy.auth.dto.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest request){
        userService.registerUser(request);
        return ResponseEntity.ok("Registration successful! Please check your email to activate your account.");
    }


    @RequestMapping(value = "/verify-email", method = RequestMethod.GET)
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("User activated successfully!");
    }
}
