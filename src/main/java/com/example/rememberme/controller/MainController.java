package com.example.rememberme.controller;

import com.example.rememberme.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;
    @GetMapping("/")
    public String index() {
//        mainService.checkAuth(request);
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
