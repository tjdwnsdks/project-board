package com.bitstudy.app.controller;

import com.bitstudy.app.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* 'Ex10_1_ArticleControllerTest_스프링시큐리티_적용'  하고 온거임*/


@Import(SecurityConfig.class) /* ArticleControllerTest 와 동일한 환경에서 테스트 해야 하니까 이거 넣기 */

@DisplayName("View 컨트롤러 - 인증")
@WebMvcTest
public class Ex10_2_AuthControllerTest_스프링시큐리티_적용 {


    private final MockMvc mvc;

    public Ex10_2_AuthControllerTest_스프링시큐리티_적용(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    //    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 로그인 페이지 - 정상호출") // 로그인 뷰 페이지 호출하는 메서드
    public void articlesAll() throws Exception {

        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

                /* login 페이지는 자동 생성이기 때문에 우리가 별도로 뷰 이름(view()name()) 을 검사할 필요가 없다. */
                // .andExpect(view().name("articles/index"))

                /* 그리고 따로 모델로 보낼 속성도 없으니까 이것도 주석 */
                // .andExpect(model().attributeExists("articles"));
    }

    /* 다 썼으면 테스트 돌려보기 */
    /** 우리는 로그인 컨트롤러를 별도로 만든것도 아니고, 그냥 @WebMvcTest 를 했을뿐인데 실제 테스트가 되고 통과까지 됐다.
     * 이 말은 스프링 시큐리티에 의해서 테스트가 가능한거구나~ 라고 생각 할 수 있는거임.
     * */
    /* 다 했으면 워드로 돌아가기 */
}
