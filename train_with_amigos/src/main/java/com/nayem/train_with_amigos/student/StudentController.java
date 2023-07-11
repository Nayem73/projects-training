package com.nayem.train_with_amigos.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {
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

                ),
                new Student(
                        2L,
                        "Jim",
                        "jim123@gmail.com",
                        LocalDate.of(2008,Month.AUGUST, 18),
                        13
                )
        );
    }
}
