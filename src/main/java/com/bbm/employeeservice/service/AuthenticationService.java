package com.bbm.employeeservice.service;

import com.bbm.employeeservice.config.JwtTokenService;
import com.bbm.employeeservice.exception.BusinessException;
import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.ConfirmationToken;
import com.bbm.employeeservice.model.Role;
import com.bbm.employeeservice.model.User;
import com.bbm.employeeservice.model.dto.AppResponse;
import com.bbm.employeeservice.model.dto.AuthenticateRequest;
import com.bbm.employeeservice.model.dto.AuthenticationResponse;
import com.bbm.employeeservice.model.dto.RegisterRequest;
import com.bbm.employeeservice.repository.ConfirmationTokenRepository;
import com.bbm.employeeservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenRepository tokenRepository;

    public AppResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um usuário cadastrado com esse email!");
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        var savedUser = userRepository.save(user);
        var token = jwtTokenService.generateToken(user);
        saveUserToken(savedUser, token);

        emailService.sendHtmlEmail(savedUser.getFirstname() + " " + savedUser.getLastname(),
                savedUser.getEmail(), token);

        return AppResponse.builder()
                .responseCode("201")
                .responseMessage("Usuário criado com Sucesso")
                .name(savedUser.getFirstname() + " " + savedUser.getLastname())
                .build();
    }

    public Boolean verifyToken(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token).orElseThrow(() ->
                new BusinessException("Token não foi encontrado"));
        User user = userRepository.findByEmail(confirmationToken.getUser().getEmail()).orElseThrow(() ->
                new EntityNotFoundException("Usuário não foi encontrado"));
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticateRequest.getEmail(),
                authenticateRequest.getPassword()
        ));
        var user = userRepository.findByEmail(authenticateRequest
                .getEmail()).orElseThrow();
        var token = jwtTokenService.generateToken(user);
        var refreshToken = jwtTokenService.generateRefreshToken(user);
        saveUserToken(user, token);

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtTokenService.extractUserEmail(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtTokenService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtTokenService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(
                        response.getOutputStream(), authResponse
                );
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = ConfirmationToken.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .createdDate(LocalDateTime.now())
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }
}
