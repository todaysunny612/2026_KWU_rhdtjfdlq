package com.rhdtjfdlq_1.Cartalk.repository;

import com.rhdtjfdlq_1.Cartalk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email); //이메일 중복 체크용
}
