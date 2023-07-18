package com.Nayem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping
    public String fnc() {
        return "url working here, Alhamdulillah";
    }

    @GetMapping("/getUser")
    public String getUser() {
        return "Mohammad Nayem Mehedi";
    }
}
