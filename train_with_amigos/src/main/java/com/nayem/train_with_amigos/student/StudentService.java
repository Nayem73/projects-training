package com.nayem.train_with_amigos.student;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class StudentService {
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
