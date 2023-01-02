package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "redirect:/articles";
        /* 여기까지 하고 TDD 만들기 */
    }

}
