package com.example.project;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MiniProjectApplication{

	public static void main(String[] args) {
		SpringApplication.run(MiniProjectApplication.class, args);
	}
}
