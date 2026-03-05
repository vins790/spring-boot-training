package com.vins.spring_boot_training.domain.archive.repository;

import com.vins.spring_boot_training.domain.archive.entity.ArchivalLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaderboardArchiveRepository extends JpaRepository<ArchivalLeaderboard, Long> {

  Optional<ArchivalLeaderboard> findTopByOrderByTimestampDesc();

}
