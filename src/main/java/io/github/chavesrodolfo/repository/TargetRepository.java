package io.github.chavesrodolfo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.chavesrodolfo.model.Target;
import io.github.chavesrodolfo.model.User;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {
    
    @Query("SELECT t FROM Target t where t.user = :user") 
    Page<Target> findTargetsByUser(Pageable pageable, User user);

    @Query("SELECT t FROM Target t where t.user = :user and t.status= 'ACTIVE'") 
    Page<Target> findTargetsActiveOnlyByUser(Pageable pageable, User user);

    @Query("SELECT t FROM Target t where t.uuid = :uuid and t.user = :user") 
	Optional<Target> findTargetByUuidAndUser(String uuid, User user);
    
}