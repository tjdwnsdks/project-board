package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
//        return "redirect:/articles";
        /* 여기까지 하고 TDD 만들기 */


        return "forword:/articles"; /** URL 에 localhost:8080 만 쳐도 articles 페이지가 알아서 잡히게 하는거 */
    }

}
