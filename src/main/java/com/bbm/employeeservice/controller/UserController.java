package com.bbm.employeeservice.controller;

import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.ChangePasswordRequest;
import com.bbm.employeeservice.model.dto.RegisterRequest;
import com.bbm.employeeservice.model.dto.UserResponse;
import com.bbm.employeeservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/")
    public ResponseEntity<AppResponse> updateUser(@Valid @RequestBody RegisterRequest request,
                                                  @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(userService.updateUser(request, authenticatedUser.getId()));
    }

    @GetMapping("/quantity")
    public ResponseEntity<Integer> countAllEnabledUsers() {
        return ResponseEntity.ok(userService.countAllEnabledUsers());
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponse> getUserById(@AuthenticationPrincipal User authenticateUser) {
        User user = userService.getUserById(authenticateUser.getId());
        return ResponseEntity.ok(userService.maptoUserResponse(user));
    }

    @PutMapping ("/changePassword")
    public ResponseEntity<AppResponse> changePassword(@RequestBody ChangePasswordRequest request,
                                                      Principal authenticatedUser) {
        return ResponseEntity.ok(userService.changePassword(request, authenticatedUser));
    }
}
