package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.RegisterRequest;
import com.bbm.employeeservice.model.dto.UserResponse;
import com.bbm.employeeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
             new EntityNotFoundException("Usuário não foi encontrado"));
    }

    public AppResponse updateUser(RegisterRequest request, Long userId) {
        User user = getUserById(userId);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        userRepository.save(user);

        return AppResponse.builder()
                .responseCode("200")
                .responseMessage("Perfil actualizado com sucesso!")
                .name(request.getFirstname())
                .build();
    }

    public Integer countAllEnabledUsers() {
        return userRepository.countAllEnabledUser();
    }

    public UserResponse maptoUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .isEnabled(user.isEnabled())
                .role(user.getRole())
                .build();
    }
}
