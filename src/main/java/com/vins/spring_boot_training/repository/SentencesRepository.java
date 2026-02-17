package com.vins.spring_boot_training.repository;

import com.vins.spring_boot_training.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentencesRepository extends JpaRepository<Sentence, Long> {
}
