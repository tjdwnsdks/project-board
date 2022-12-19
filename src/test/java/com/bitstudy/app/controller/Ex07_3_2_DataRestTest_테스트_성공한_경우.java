package com.bitstudy.app.controller;

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

/* 이거 없애고 아래 두개로 해야함. @SpringBootTest , @AutoConfigureMockMvc */
//@WebMvcTest
@SpringBootTest /* 이거 넣기. 그런데 이거만 있으면 MockMvc 를 알아볼 수가 없어서 아래 @AutoConfigureMockMvc 추가 */
@AutoConfigureMockMvc
/* 이거 두개 넣으면 테스트 통과함
* 혹시 테스트 결과 하나하나를 보고 싶으면 맨 아래  mvc.perfome 부분에 "andDo(print()));" 추가해봐도 됨. 그럼 HAL 에서 본거랑 같은거 나올거임 */

@DisplayName("Data REST - API 테스트") /* 테스트 이름 주기 */
@Transactional /* 테스트를 돌리면 콘솔창에 Hibernate 부분에 select 쿼리문도 나온다. 이 말은 실제 repository 까지 실행시켰다는 건데. 한마디로 DB 를 건드릴수도 있다는 말이다.
 그래서 이건 테스트이기 때문에 트랜젠션으로 묶어서 롤백까지 되도록 한다. 이렇게 하면 콘솔에서 Hibernate 부분 select 쿼리 위 아래로 "began trasction" , "Rolled back transaction" 이라고 나올거다. 그럼 할거 해서 보여주고 원래대로 돌아갔다라는 뜻. 테스트만 하고 원상복귀!!  */
public class Ex07_3_2_DataRestTest_테스트_성공한_경우 {

    /* 빈 준비 */
    private final MockMvc mvc; // 실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체를 만들어서 애플리케이션 서버에 배포하지 않고도 스프링 MVC 동작을 재현할 수 있는 클래스를 의미


    /* MockMvc 를 생성자 방식으로 주입 받게 해볼거임 */
    public Ex07_3_2_DataRestTest_테스트_성공한_경우(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void test() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk()) // 현재 상태가 200 인가 (존재하는가?)
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // 마지막껀 외우기
                //.andDo(print());
                /* 여기도 import 방식으로 해야함.
                * 방법: ctrl + space 해서 MockMvcResultHanders.print 선택하고
                        alt + Enter */
    }
}













