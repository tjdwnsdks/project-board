package com.bitstudy.app.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*  *참고 - 이 메세지가 위에 있으면 테스트 에러가 가끔 남. 원래는 이 메세지가 맨 위에 있어야 하는데 에러니까 클래스 안쪽으로 피신시킬것!

이 테스트는 평범한 방식으로는 컨트롤러 테스트 하듯이 할 수 없었다.
@WebMvcTest 로 슬라이스 테스트를 하려면 그냥은 안되고 별도의 복잡한 방법을 썼어야 했기 때문에, 약간의 편법으로
data rest 와 관련된 AutoConfigration을 불러올수 있게끔 @SpringBootTest 를 이용해서 통합테스트 형태로 진행 해서
모든 bean 들을 읽은 상태에서 진행했었다. 그래서 좀 무겁고 DB 쪽에 Mocking 을 할 수 없는 상태로 만들다보니까
어쩔수 없이 실제 DB까지 접근을 하는 테스트를 만들어버렸었다. */

/* 다 하면 워드로 돌아가고 ('검색기능 추가하기' 부분임) , 깃크라켄에 커밋하기 */

@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴") // 클래스 레벨에 이걸 붙이면 해당 테스트 클래스의 밑에 있는 모든 메서드 들은 실행되지 않게 될거다.
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Data REST - API 테스트 4")
@Transactional

public class Ex07_3_4_DataRestTest_해당_테스트_안읽히게_해보기 {


    private final MockMvc mvc;

    public Ex07_3_4_DataRestTest_해당_테스트_안읽히게_해보기(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void articleAll() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk()) // 현재 상태가 200 인가 (존재하는가?)
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }
    /////////////////////////////////////////////////////

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void articleOne() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1")) /* 테스트 데이터가 있다고 가정하고 하는거임. 1번글 하나 가져와라  */
                .andExpect(status().isOk()) // 현재 상태가 200 인가 (존재하는가?)
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 게시글 댓글 리스트 조회")
    @Test
    void articleCommentAllByArticle() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments")) /* 1번글의 모든 댓글들 가져와라  */
                .andExpect(status().isOk()) // 현재 상태가 200 인가 (존재하는가?)
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 댓글 리스트 전체 조회")
    @Test
    void articleCommentAll() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments")) /* 모든 댓글들 가져와라  */
                .andExpect(status().isOk()) // 현재 상태가 200 인가 (존재하는가?)
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void articleCommentOne() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1")) /* 1번 댓글 가져와라  */
                .andExpect(status().isOk()) // 현재 상태가 200 인가 (존재하는가?)
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

}













