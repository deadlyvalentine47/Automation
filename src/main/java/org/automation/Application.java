package org.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients(basePackages = {"org.automation"})
@EntityScan("org.automation")
@EnableAsync
public class Application {
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
}