package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.exception.FibonacciNotNullException;
import org.springframework.stereotype.Component;

@Component
public class FibonacciService {
  public int getFibonacci(int n) {
    if (n < 0) {
      throw new FibonacciNotNullException("n must be non-negative");
    }

    if (n <= 1) return n;

    int a = 0;
    int b = 1;
    for (int i = 2; i <= n; i++) {
      int next = a + b;
      a = b;
      b = next;
    }
    return b;
  }
}
