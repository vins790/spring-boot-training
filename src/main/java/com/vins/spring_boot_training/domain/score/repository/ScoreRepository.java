package com.vins.spring_boot_training.domain.score.repository;

import com.vins.spring_boot_training.domain.score.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
