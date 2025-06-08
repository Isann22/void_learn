package com.example.voidlearn;

import com.example.voidlearn.config.StorageProperties;
import com.example.voidlearn.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class VoidLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoidLearnApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
