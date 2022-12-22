package com.bitstudy.app.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** 하기 전에 알아둘것: 이 테스트 코드를 작성하고 돌리면 404 에러난다.
 * 이유는 아직 ArticleController 에 작성된 내용이 없고, dao 같은것도 없기 때문에 그렇다. 일단 다 작성하고 워드파일로 돌아간다.
 * 우선 칸반보드의 순서에 맞춰서 개발을 한다. 실제 회사에서도 구현체를 만들지 않고 테스트 단위를 개발하는걸 먼저 하기도 한다.
 *
 * 끝날때쯤 @Disabled 어노테이션이 뭔지 설명한다.
 *
 * 바로 다음에 할 뷰 구성 단계에서 구현하러 갈거니까 괜찮다.
 * */



/* autoConfigration 을 가져올 필요가 없는 테스트라서 WebMvcTest 쓸 수 있다. */
//@WebMvcTest // 이렇게만 쓰면 모든 컨트롤러를 다 읽어들인다. 직금은 컨트롤러가 하나뿐이라 상관 없긴 하다.. 그래서 테스트 대상이 되는 컨트롤러만 bean 으로 읽어들이는게 가능하다. (아래처럼) 해당 컨트롤러의 클래스만 넣어주면 됨.
@WebMvcTest(ArticleController.class)

@DisplayName("view 컨트롤러 - 게시글")
class Ex08_2_ArticleControllerTest_게시판_뷰_테스트 {

    private final MockMvc mvc;

    public Ex08_2_ArticleControllerTest_게시판_뷰_테스트(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }
    
    /* 테스트를 만들건데 엑셀 api 에 있는 순서대로 만들거다. 1) 게시판 페이지 2)게시글 상세 페이지 3) 게시판 검색 페이지 4) 해시태그 검색 페이지 */

//    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상호출") // 뷰 테스트를 get 방식으로 할건데 정상호출만 들어오는 경우로 테스트를 만들거임
    public void articlesAll() throws Exception {

        /* 사용법: mvc.perform(get(경로쓰기));
                 1. get만 쓰고 ctrl + space 해서 딥다이브 한 다음에
                 2. MockMbcRequestBuilder.get 그거 찾아서 import(Alt + Enter 하면 됨)
                 3. 그럼 맨 위에 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 생김 */
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 200 ok 나오고 있는지 체크. 여기도 ctrl+스페이스바 해서 MockMvcResultMatchers.status() 를 import 방식으로 하기.

                //.andExpect(content().contentType(MediaType.TEXT_HTML)) // contentType은 exact match 라서 미디어타입이 즨짜 똭 text/html 으로 나오는것만 허용한다. 그래서 아래줄에 있는 contentTypeCompatibleWith 를 이용해서 호환되는 타입까지 맞다고 쳐준다.
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // 뷰 만들고 있으니까 html 로 코드를 짜고 있을거다. /articles 로 받아온 데이터가 미디어 타입이 html 타입으로 되어 있는 애인지 확인.
                .andExpect(view().name("articles/index")) // 이건 해당 뷰 파일명이 index 인지 확인
                .andExpect(model().attributeExists("articles"));// 이 뷰에서는 게시글들 목록들이 쫙 떠야 하는데, 그 얘기는 서버에서 게시글 데이터들을 가져왔다는 말이다. 그러면 모델 어트리뷰트로 데이터를 밀어넣어줬다는 말인데, 그게 있는지 없는지 검사할 수 있다. 사용법은 아래.
        // model().attributeExists("articles") <- 여기서 "articles 는 내가 임의로 걸어주는 키값. 맵에 articles 라는 키가 있는지 검사해라 라는 뜻
        // 모델에     "articles" 라는 attribute 가 있는지 확인 하라는 뜻
    }

    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출")
    public void articlesOne() throws Exception {
        mvc.perform(get("/articles/1")) /* 테스트니까 그냥 1번글 가져와라 할거임 */
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail")) // 이건 해당 뷰 파일명이 detail 인지 확인
                .andExpect(model().attributeExists("articles"))
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

/* 이상태로 테스트를 돌려보면 해당 내용들이 DB에 없기 때문에 404 에러 나야한다.
* 일단 테스트는 작성 되었다. 데이터는 깃크라켄 가서 커밋 하고 오자 -  워드파일 돌아가기*/


















