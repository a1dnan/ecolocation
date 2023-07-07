package com.example.pfa.repos;

import com.example.pfa.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepos extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String roleName);
}
