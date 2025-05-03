package com.example.starwars;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",  // dummy/default
    "spring.kafka.consumer.group-id=test-group",       // dummy/default
    "spring.kafka.consumer.auto-offset-reset=earliest"
})
class StarWarsApplicationTests {

	@Test
	void contextLoads() {
	}

}
