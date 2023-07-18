package com.Nayem.controller;

import com.Nayem.model.UserModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping
    public String fnc() {
        return "url working here, Alhamdulillah";
    }

    @GetMapping("/getUser")
    public UserModel getUser() {
        return new UserModel(
                "Nayem",
                "Mehedi",
                180122
        );
    }
}
