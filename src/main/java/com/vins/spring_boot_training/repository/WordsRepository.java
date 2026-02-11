package com.vins.spring_boot_training.repository;

import com.vins.spring_boot_training.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordsRepository extends JpaRepository<Word, Long> {
  Optional<Word> findByWord(String word);
}
