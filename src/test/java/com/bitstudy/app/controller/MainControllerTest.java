package com.bitstudy.app.controller;

import com.bitstudy.app.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/*
 원래의 테스트 코드로 테스트를 돌리면 콘솔창에 401 이라는 에러코드가 뜰거다.
 401 은 파일은 찾긴 찾았는데 인증을 못받아서 못들어간다 라는 뜻이다.
 이건 기본 웹 시큐리티를 불러와서 그런거고, 우리는 설정해놓은 SecurityConfig 를 읽고 오게만 하면 된다.
 @Import(SecurityConfig.class) 를 넣어서 SecurityConfig 에서 설정한 내용이 이 test 코드에서도 읽히게끔 하면 된다.
 */
@Import(SecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {
    private final MockMvc mvc;

    public MainControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void rootPathTest() throws Exception {
        // Given

        // When & Then
/* 이거 없앰*/
        /** mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection()); // 상태코드300 - 리다이렉션이 제대로 동작하는지 확인*/

/* 새로 생성*/
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/articles"))
                .andExpect(forwardedUrl("/articles"))
                .andDo(MockMvcResultHandlers.print());

    }
}