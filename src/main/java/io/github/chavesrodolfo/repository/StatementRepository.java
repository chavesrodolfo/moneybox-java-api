package io.github.chavesrodolfo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.chavesrodolfo.model.Statement;
import io.github.chavesrodolfo.model.Target;
import io.github.chavesrodolfo.model.User;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    
    @Query("SELECT s FROM Statement s where s.target = :target and s.user = :user") 
    Page<Statement> findStatementByTargetAndUser(Pageable pageable, Target target, User user);

    @Query("SELECT s FROM Statement s where s.user = :user") 
    Page<Statement> findStatementByUser(Pageable pageable, User user);
    
    @Query("SELECT count(*) FROM Statement s where s.target = :target and s.user = :user") 
    Long countByTargetAndUser(Target target, User user);

    @Query("SELECT s FROM Statement s where s.uuid = :uuid and s.user = :user") 
	Optional<Statement> findStatementByUuidAndUser(String uuid, User user);
    
}
