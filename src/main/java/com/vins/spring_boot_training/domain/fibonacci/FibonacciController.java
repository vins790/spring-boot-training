package com.vins.spring_boot_training.domain.fibonacci;

import org.springframework.http.HttpStatus;
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
  public Long fibonacci(@PathVariable long n) {
    return fibonacciService.getFibonacci(n);
  }
}
