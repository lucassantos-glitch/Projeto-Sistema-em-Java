package com.farmacia.farmacia_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.farmacia.config.EstoqueProperties;

@SpringBootApplication(scanBasePackages = {"com.farmacia"})
@EnableJpaRepositories(basePackages = {"com.farmacia.repository"})
@EntityScan(basePackages = {"com.farmacia.model"})
@EnableConfigurationProperties(EstoqueProperties.class)
public class FarmaciaApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FarmaciaApiApplication.class, args);
    }
}