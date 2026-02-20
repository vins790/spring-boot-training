package com.vins.spring_boot_training.domain.word.repository;

import com.vins.spring_boot_training.domain.word.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
  Optional<Word> findByWord(String word);
}
