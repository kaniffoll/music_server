package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.RoleRepository;
import com.kanifol.musicserver.repository.UserRepository;
import com.kanifol.musicserver.repository.model.Role;
import com.kanifol.musicserver.repository.model.User;
import com.kanifol.musicserver.service.dto.LoginRequest;
import com.kanifol.musicserver.service.dto.RegisterRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Authentication registerUser(RegisterRequest registerRequest) {
        String passwordHash = passwordEncoder.encode(registerRequest.password());
        User user = new User(
                registerRequest.username(),
                registerRequest.email(),
                passwordHash
        );
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role Not Found"));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                user
                        .getRoles()
                        .stream()
                        .map(rl -> new SimpleGrantedAuthority(rl.getName()))
                        .toList()
        );
    }

    @Transactional
    public Authentication login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("Username Not Found"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Wrong Password");
        }
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                user
                        .getRoles()
                        .stream()
                        .map(rl -> new SimpleGrantedAuthority(rl.getName()))
                        .toList()
        );
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username Not Found"));
    }
}
