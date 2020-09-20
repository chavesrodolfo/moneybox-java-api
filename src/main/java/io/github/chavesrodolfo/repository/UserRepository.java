package io.github.chavesrodolfo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.chavesrodolfo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u where u.active = true") 
    Page<User> findActiveUsers(Pageable pageable);

	Optional<User> findByUuid(String uuid);
    
}