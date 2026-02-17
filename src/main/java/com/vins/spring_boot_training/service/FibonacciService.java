package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.exception.errors.FibonacciErrors;
import com.vins.spring_boot_training.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class FibonacciService {
  public int getFibonacci(int n) {
    if (n < 0) {
      throw new CustomException(FibonacciErrors.NEGATIVE_NUMBER);
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
