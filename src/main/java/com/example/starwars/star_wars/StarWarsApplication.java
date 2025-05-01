package com.example.starwars.star_wars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class StarWarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsApplication.class, args);
	}

}
