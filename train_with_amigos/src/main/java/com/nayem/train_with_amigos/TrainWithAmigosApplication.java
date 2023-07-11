package com.nayem.train_with_amigos;

import com.nayem.train_with_amigos.student.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class TrainWithAmigosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainWithAmigosApplication.class, args);
	}

	@GetMapping
	public List<Student> hello() {
//		return List.of("hello", "world");
//		List<String> tmp = new ArrayList<>();
//		tmp.add("hello");
//		tmp.add("world");
//		return tmp;
		return List.of(
				new Student(
						1L,
						"Nayem",
						"legend3073@gmail.com",
						LocalDate.of(1995, Month.MAY, 30),
						21

				)
		);
	}
}
