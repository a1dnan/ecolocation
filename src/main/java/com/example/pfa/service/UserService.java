package com.example.pfa.service;

import com.example.pfa.dto.AuthenticationRequest;
import com.example.pfa.dto.AuthenticationResponse;
import com.example.pfa.entities.User;
import org.springframework.data.domain.Page;

public interface UserService {

    User loadUserById(Long userId);

    Page<User> loadUsersByName(String name, int page, int size);

    User loadUserByEmail(String email);

    User createUser(User user);

    User updateUser(User user);

    void removeUser(Long userId);

    User findUserId(Long userId);

    void seveFavorie(Long userId, Long annonceId);

    void deleteFavorie(Long userId, Long annonceId);

    AuthenticationResponse register(User user);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
