package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.exception.errors.FibonacciErrors;
import com.vins.spring_boot_training.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class FibonacciService {
  public Long getFibonacci(long n) {
    if (n < 0) {
      throw new CustomException(FibonacciErrors.NEGATIVE_NUMBER);
    }

    if (n <= 1) return n;

    long a = 0;
    long b = 1;
    for (long i = 2; i <= n; i++) {
      long next = a + b;
      a = b;
      b = next;
    }
    return b;
  }
}
