package com.bitstudy.app.controller;

import com.bitstudy.app.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** !! config > SerucityConfig 파일 하고 오기 !!

 Spring Security 처리 하고 와서 하는 테스트임. */

/*
 원래의 테스트 코드로 테스트를 돌리면 콘솔창에 401 이라는 에러코드가 뜰거다.
 401 은 파일은 찾긴 찾았는데 인증을 못받아서 못들어간다 라는 뜻이다.
 이건 기본 웹 시큐리티를 불러와서 그런거고, 우리는 설정해놓은 SecurityConfig 를 읽고 오게만 하면 된다.
 @Import(SecurityConfig.class) 를 넣어서 SecurityConfig 에서 설정한 내용이 이 test 코드에서도 읽히게끔 하면 된다.
 */
@Import(SecurityConfig.class)
/* 이거 하고 'Ex10_2_AuthControllerTest_스프링시큐리티_적용' 파일 만들기 */

@WebMvcTest(ArticleController.class)

@DisplayName("view 컨트롤러 - 게시글")
class Ex10_1_ArticleControllerTest_스프링시큐리티_적용 {

    private final MockMvc mvc;

    public Ex10_1_ArticleControllerTest_스프링시큐리티_적용(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


//    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상호출") // 뷰 테스트를 get 방식으로 할건데 정상호출만 들어오는 경우로 테스트를 만들거임
    public void articlesAll() throws Exception {

        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));
    }

    //@Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출")
    public void articlesOne() throws Exception {
        mvc.perform(get("/articles/1")) /* 테스트니까 그냥 1번글 가져와라 할거임 */
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail")) // 이건 해당 뷰 파일명이 detail 인지 확인
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments")); // 상세페이지에는 댓글들도 여러개 있을수도 있으니까 모델 어트리뷰트에 articleComments 라는 키값으로 된게 있냐 라고 물어보는거
    }

    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상호출")
    public void articlesSearch() throws Exception {
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search")); // 이건 해당 뷰 파일명이 search 인지 확인

        /* 이 테스트는 검색 페이지만 뿌려주면 되는거라서 아직 데이터를 받아오지 않았을 상태라서 이건 필요 없음 */
        // .andExpect(model().attributeExists("articles"));
    }

    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시판 해시태그 검색 전용 페이지 - 정상호출")
    public void articlesSearchHashtag() throws Exception {
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))

                /* 이 테스트는 검색 페이지만 뿌려주면 되는거라서 아직 데이터를 받아오지 않았을 상태라서 이건 필요 없음 */
                // .andExpect(model().attributeExists("articles"));

                .andExpect(view().name("articles/search-hashtag")); // 이건 해당 뷰 파일명이 search-hashtag 인지 확인
    }
}



/* 이거 하고 'Ex10_2_AuthControllerTest_스프링시큐리티_적용' 파일 만들기 */















