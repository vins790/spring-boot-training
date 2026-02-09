package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.service.FibonacciServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fib")
public class FibonacciController {

  FibonacciServiceImpl fibonacciService;

  @Autowired
  public FibonacciController(FibonacciServiceImpl fibonacciService) {
    this.fibonacciService = fibonacciService;
  }

  @GetMapping("/{n}")
  public ResponseEntity<?> fibonacci(@PathVariable int n) {
    try {
      int result = fibonacciService.getFibonacci(n);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
