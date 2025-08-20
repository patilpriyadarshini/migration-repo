package com.modernized.repositories;

import com.modernized.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
    
    @Query("SELECT u FROM User u WHERE u.secUsrId LIKE %:userId%")
    Page<User> findBySecUsrIdContaining(@Param("userId") String userId, Pageable pageable);
}
