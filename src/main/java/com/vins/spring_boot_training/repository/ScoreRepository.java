package com.vins.spring_boot_training.repository;

import com.vins.spring_boot_training.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
