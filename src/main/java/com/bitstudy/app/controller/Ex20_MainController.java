package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class Ex20_MainController {

    @GetMapping("/")
    public String root() {
/* 이거 삭제 */return "redirect:/articles";
        /* 여기까지 하고 TDD 만들기 */

///* 새로 삽입*/ return "forword:/articles"; /** URL 에 localhost:8080/ 만 쳐도 articles 페이지가 알아서 잡히게 하는거 */
            /* 이거로 바꾸면 MainControllerTest 실패 뜸*/
    }

}

/* 다 했으면 test > service > ArticleCommentServiceTest.java 가기 */
