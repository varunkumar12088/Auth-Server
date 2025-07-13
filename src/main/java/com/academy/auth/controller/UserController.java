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


    @RequestMapping(value = "/{email}/verify-email", method = RequestMethod.GET)
    public ResponseEntity<?> verifyEmail(@PathVariable String email, @RequestParam String token) {
        String message = userService.verifyEmail(email, token);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/{email}/resend-verification-email", method = RequestMethod.GET)
    public ResponseEntity<?> resendVerificationEmail(@PathVariable String email, @RequestParam String token) {
        String message = userService.verifyEmail(email, token);
        return ResponseEntity.ok(message);
    }
}
