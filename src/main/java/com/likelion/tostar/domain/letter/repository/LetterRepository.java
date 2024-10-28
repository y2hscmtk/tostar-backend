package com.likelion.tostar.domain.letter.repository;

import com.likelion.tostar.domain.letter.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {
}

