package com.rhdtjfdlq_1.Cartalk_BE.repository;

import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email); //이메일 중복 체크용
    Optional<UserEntity> findByEmail(String email);
}
