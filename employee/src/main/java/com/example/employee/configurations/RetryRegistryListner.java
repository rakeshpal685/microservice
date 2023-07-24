package com.example.employee.configurations;

import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

/*
Whenever we are using @Retry(name = "studentServiceRetry", fallbackMethod = "studentFallbackMethod"), a series
of event will happen, we can use this class to catch those events*/
@Component
public class RetryRegistryListner {
  private RetryRegistry registry;
  public void init() {
    registry.retry("studentServiceRetry").getEventPublisher().onRetry(e -> System.out.println(e));
  }
}
