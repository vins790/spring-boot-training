package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.dto.ScoreDto;
import com.vins.spring_boot_training.service.ScoreService;
import com.vins.spring_boot_training.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/score")
public class ScoreController {

  private ScoreService scoreService;
  private UserService userService;

  @GetMapping()
  public Long getScore(@AuthenticationPrincipal UserDetails user) {
    return scoreService.getUserScore(userService.getIdByUserDetails(user));
  }

  @GetMapping("/{userId}")
  public Long getScore(@PathVariable Long userId) {
    return scoreService.getUserScore(userId);
  }

  @GetMapping("/leaderboard")
  public List<ScoreDto> getLeaderboard() {
    return scoreService.getLeaderboard(10L);
  }

  @PostMapping("/recalculateScores")
  public void recalculateScores() {
    scoreService.scheduledScoreCalculation();
  }

  @GetMapping("/calculate/{userId}")
  public Long calculateScore(@PathVariable Long userId) {
    return scoreService.calculateUserScore(userId);
  }
}
