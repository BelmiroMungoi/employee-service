package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.AuthenticateRequest;
import com.bbm.employeeservice.model.dto.AuthenticationResponse;
import com.bbm.employeeservice.model.dto.RegisterRequest;
import com.bbm.employeeservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AppResponse> register(@RequestBody RegisterRequest request) {
        var registry = authenticationService.register(request);
        return new ResponseEntity<>(registry, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticateRequest request) {
        var authentication = authenticationService.authenticate(request);
        return new ResponseEntity<>(authentication, HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<AppResponse> confirmUser(@RequestParam("token") String token) {
        boolean isSuccess = authenticationService.verifyToken(token);
        AppResponse response = AppResponse.builder()
                .responseCode("200")
                .responseMessage("A sua conta foi verificada com sucesso!!!")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/refresh-token", produces = "application/json")
    public void refreshToken(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        System.out.println("TESTANDO O REFRESH TOKEN");
        authenticationService.refreshToken(token, response);
    }
}
