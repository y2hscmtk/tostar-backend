package com.likelion.tostar.domain.letter.repository;

import com.likelion.tostar.domain.letter.entity.Letter;
import com.likelion.tostar.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    // 해당 회원이 쓴 편지 조회(최신순)
    List<Letter> findByUserOrderByCreatedAtDesc(User user);

}

