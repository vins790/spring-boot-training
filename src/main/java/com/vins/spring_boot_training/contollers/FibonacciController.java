package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.exception.FibonacciNotNullException;
import com.vins.spring_boot_training.service.FibonacciService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/fib")
public class FibonacciController {

  private final FibonacciService fibonacciService;

  public FibonacciController(FibonacciService fibonacciService) {
    this.fibonacciService = fibonacciService;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{n}")
  public ResponseEntity<Integer> fibonacci(@PathVariable int n) {
      int result = fibonacciService.getFibonacci(n);
      return ok(result);
  }
}
