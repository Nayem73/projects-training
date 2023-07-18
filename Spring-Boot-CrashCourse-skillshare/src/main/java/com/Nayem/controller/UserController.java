package com.Nayem.controller;

import com.Nayem.model.UserModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @GetMapping
    public String fnc() {
        return "url working here, Alhamdulillah";
    }

    public Map<String, UserModel> map = new HashMap<>();
    map.put("Nayem", new UserModel("Nayem", "Mehedi", 180122));

    @GetMapping("/getUser")
    public UserModel getUser() {
        return new UserModel(
                "Nayem",
                "Mehedi",
                180122
        );
    }
}
