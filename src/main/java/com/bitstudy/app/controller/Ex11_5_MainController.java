package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class Ex11_5_MainController {

//    @GetMapping("/")
    public String root() {
        return "redirect:/articles";
        /* 여기까지 하고 TDD 만들기
        *
            test > controller > Ex11_6_MainControllerTest  ㄱㄱ  */
    }



    /* 여기까지 했으면 test > controller > Ex11_6_MainControllerTest.java 테스트 하고 ,  워드로 돌아가기 */
}
