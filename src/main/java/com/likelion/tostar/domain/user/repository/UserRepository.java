package com.likelion.tostar.domain.user.repository;


import com.likelion.tostar.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsUserByEmail(String email);
    Optional<User> findUserByEmail(String email);
    //  petName을 포함하는 User 페이지 조회
    Page<User> findByPetNameContaining(String petName, Pageable pageable);

}
