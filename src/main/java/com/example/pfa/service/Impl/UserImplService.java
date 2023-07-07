package com.example.pfa.service.Impl;

import com.example.pfa.config.JwtUtils;
import com.example.pfa.dto.AuthenticationRequest;
import com.example.pfa.dto.AuthenticationResponse;
import com.example.pfa.entities.Annonce;
import com.example.pfa.entities.Role;
import com.example.pfa.entities.User;
import com.example.pfa.repos.AnnonceRepos;
import com.example.pfa.repos.RoleRepos;
import com.example.pfa.repos.UserRepos;
import com.example.pfa.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class UserImplService implements UserService {

    private final UserRepos userRepos;
    private final AnnonceRepos annonceRepos;
    private static final String ROLE_USER = "ROLE_USER";
    private final PasswordEncoder passwordEncoder;
    private final RoleRepos roleRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authManager;

    @Override
    public User loadUserById(Long userId) {
        return userRepos.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with ID" + userId + " not found"));
    }

    @Override
    public Page<User> loadUsersByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> usersPage = userRepos.findUserByName(name, pageRequest);
        return new PageImpl<>(usersPage.getContent().stream().collect(Collectors.toList()), pageRequest, usersPage.getTotalElements());
    }

    @Override
    public User loadUserByEmail(String email) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return userRepos.save(user);
    }

    @Override
    public User updateUser(User user) {
        User userLoad=loadUserById(user.getUserId());
        user.setAnnonce(userLoad.getAnnonce());
        user.setAnnonceFavorites(userLoad.getAnnonceFavorites());
        user.setRole(
                userLoad.getRole()
        );
        user.setPassword(userLoad.getPassword());
        return userRepos.save(user);
    }

    @Override
    public void removeUser(Long userId) {
        userRepos.deleteById(userId);
    }

    @Override
    public User findUserId(Long userId) {
        return (this.userRepos.findById(userId).isPresent())?userRepos.findById(userId).get():null;
    }

    @Override
    public void seveFavorie(Long userId, Long annonceId) {
        User u = findUserId(userId);
        Annonce a = this.annonceRepos.findById(annonceId).get();
        u.getAnnonceFavorites().add(a);
        userRepos.save(u);
    }

    @Override
    public void deleteFavorie(Long userId, Long annonceId) {
        User u = findUserId(userId);
        Annonce a = this.annonceRepos.findById(annonceId).get();
        u.getAnnonceFavorites().remove(a);
        userRepos.save(u);
    }

    @Override
    public AuthenticationResponse register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(
                findOrCreateRole(ROLE_USER)
        );
        var savedUser = userRepos.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getUserId());
        claims.put("fullName", savedUser.getNom() + " " + savedUser.getPrenom());
        String token = jwtUtils.generateToken(savedUser,claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final User user = userRepos.findByEmail(request.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("name", user.getNom()+" "+user.getPrenom());
        String token = jwtUtils.generateToken(user,claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    private Role findOrCreateRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElse(null);
        if (role == null) {
            return roleRepository.save(
                    Role.builder()
                            .name(roleName)
                            .build()
            );
        }
        return role;
    }

}
