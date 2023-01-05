package com.bitstudy.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;




@DisplayName("View 컨트롤러 - 인증")
@WebMvcTest
public class AuthControllerTest {


    private final MockMvc mvc;

    public AuthControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    //    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 로그인 페이지 - 정상호출") // 로그인 뷰 페이지 호출하는 메서드
    public void articlesAll() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

                /* login 페이지는 자동 생성이기 때문에 우리가 별도로 뷰 이름(view()name()) 을 검사할 필요가 없다. */
                // .andExpect(view().name("articles/index"))

                /* 그리고 따로 모델로 보낼 속성도 없으니까 이것도 주석 */
                // .andExpect(model().attributeExists("articles"));
    }

}
