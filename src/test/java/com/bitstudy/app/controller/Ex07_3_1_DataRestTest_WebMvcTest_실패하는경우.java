package com.bitstudy.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/** 컨트롤러에 대한 테스트니까 슬라이스 테스트를 해볼거다.
 * 슬라이스 테스트란: 말 그대로 레이어별로 잘라서, 특정 부분만 테스트 할 수 있는 테스트를 슬라이스 테스트라고 한다(레이어별로 자른다고도 한다).

  - 대표적인 통합 테스트 어노테이션
        1) @SpringBootTest - 스프링이 관리하는 모든 빈을 등록시켜서 테스트하기 때문에 무겁다.
                            *테스트 할때 가볍게 하기 위해서 @WebMvcTest 를 사용해 web 레이어 관련 빈들만 등록한 상태로 테스트 할 수 있어서 가볍다.
                            * 단, web레이어 관련 빈들만 등록되므로 Service는 등록되지 않기 때문에 @MockBean 어노테이션을 이용해서 가짜로 만들어줄 필요가 있다
  - 대표적인 슬라이스 테스트 어노테이션
        1) @WebMvcTest - Contorller를 테스트할 수 있도록 관련 설정을 제공한다.
                         @WebMvcTest를 선언하면 Web과 관련된 Bean만 주입이 되고, MockMvc 를 알아볼수 있게 된다.

                        * MockMvc는 웹 어플리케이션을 애플리케이션 서버에 배포하지 않고 테스트용 MVC환경을 만들어 요청 및 전송, 응답기능을 제공해주는 유틸리티 클래스다.  풀어서 말하면 내가 컨트롤러 테스트를 하고싶을 때 실제 서버에 구현한 애플리케이션을 올리지 않고(실제 서블릿 컨테이너를 사용하지 않고) 테스트용으로 시뮬레이션하여 MVC가 되도록 도와주는 클래스다! (참고 - https://velog.io/@jkijki12/Spring-MockMvc)

                         *그냥 컨트롤러 테스트 한다 하면 @WebMvcTest 랑 MockMvc 쓰면 된다.

        2) @DataJpaTest - JPA 레포지토리를 테스트하기 위해 사용합니다.
                         기본적으로 @Entity가 있는 엔티티 클래스들을 스캔하며 테스트를 위한 TestEntityManager를 사용해 JPA 레포지들을 설정해줍니다.
                         @Component나 @ConfigurationProperties Bean들은 스캔되지 않습니다.

        3) @RestClientTest
                         클라이언트 입장에서의 API 연동 테스트입니다.
                         테스트 코드 내에서 Mock 서버를 띄울 수 있게 합니다. (이 Mock 서버는 request에 대한 검증, response에 대한 사전정의가 가능합니다.)
                         스프링 컨텍스트 전체를 사용하지 않기 때문에 테스트에 사용되는 클래스를 value에 전달하여 Bean으로 등록해야 합니다.

 * */

@WebMvcTest // @WebMvcTest 는 슬라이스 테스트 이다. 컨트롤러 외의 빈들을 로드하지 않는다. 얘는 컨트롤러와 연관된 내용만 최소한으로 로드한다. 그러니까 data rest 의 autoConfigration 을 읽지 않은거다. 그거를 따로 어떻게든 넣는 방법이 있겠지만 굉장히 번거롭고 처리해야 할게 많기 때문에 그래서 이 테스트를 약간 틀어서 다른 어노테이션으로 돌려보자.
/* 이거 맨 밑까지 다 하면 Ex07_3_2 번으로 넘어가기 */
public class Ex07_3_1_DataRestTest_WebMvcTest_실패하는경우 {

    /** MockMvc 테스트 방법.  참고 - https://velog.io/@jkijki12/Spring-MockMvc
        1. MockMvc를 생성한다. (빈 준비)
        2. MockMvc에게 요청에 대한 정보를 입력한다.
        3. 요청에 대한 응답값을 Expect를 이용하여 테스트한다.
        4. Expect가 모두 통과하면 테스트 통과
        5. Expect가 1개라도 실패하면 테스트 실패 */

    private final MockMvc mvc; // 빈 준비


    /* MockMvc 를 생성자 방식으로 주입 받게 해볼거임 */
    public Ex07_3_1_DataRestTest_WebMvcTest_실패하는경우(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void test() throws Exception {
        // Given

        // When & Then  // () 안에 뭐 들어갈지 모르겠으면, 마우스 호버 해보거나, 그냥 Ctrl + B 해봐라
        /* 일단 이 아래 코드 작성해서 테스트 돌리면 나오는 콘솔창에 404 에러 떠있을거다. 이유는 해당 api 를 찾을수 없기 떄문인건데
        * 콘솔창 어딘가에 있는 MockHttpServletRequest 부분 보면 URI = /api/articles 있을거다 그거 브라우저 가서 돌려보면 실행될거다.
        * 실행법: ProjectBoradApplication.java 파일 Run 하고  >>>  localhost:8080/api/articles 돌려보면 잘 나옴
        * 
        * 그럼 왜 저기선 제대로 동작 하는데 여기선 안되냐면, @WebMvcTest는 슬라이스 테스트 이기 때문에 controller 외의 빈들은 로드 하지 않기 때문이다.
        * 컨트롤러와 연관된 최소한만 로드하기 때문에 data rest의 autoConfigration 을 읽지 않은거다.
        *
        * 그래서 이런경우에는 @WebMvcTest 대신 통합테스트 어노테이션인 @SpringBootTest 를 사용할거다.
        * */
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk()) // 현재 상태가 200 인가 (존재하는가?)
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // 마지막껀 외우기

    /** 여기 좀 특별하게 해야함
        1) perfome() 안에 get 이라고 친다. => mvc.perform(get);
        2) 그러면 추천에 getClass() 라는거 하나만 나오는데 거기서 Ctrl + Space
        3) 그럼 다른 방식의 추천들이 엄청 나오는데 그중에
           MockMveRequestBuilders.get 이라는거 선택할건데 그냥 안함
           static import 를 하기 위해서 그거 키보드로 호버하고
           윈도우: Alt + Enter  /  맥: 옵션 + 엔터
           하면 get() <= get 메서드만 가져온다
        * Static import란 필드나 메서드를 클래스를 지정하지 않고도 코드에서 사용할 수 있도록 하는 기능이다.
          해당 기능은 버전 5부터 도입되었다.
        4) 그러면 맨 위에 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; 이거 생긴다.

        5) .andExpect(status()) 부분에서 status도 코트추천(컨트롤 + 스페이스) 두세번 하면 
            MockMveRequestMatchers.status() 라는거 있다.
            그거 alt + Enter 로 static import 하자
        6) 그럼 맨 위에 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; 이거생긴다.

        7) content() 은 그냥 일반적으로 하면 됨.(어짜피 하나밖에 안나옴)
           content 검사는 contentType 으로 볼건데 이때 MediaType을 사용할거다
           근데 그 안에(valueOf 안) 들어갈 content-type 은 아까 HAL Explorer 의 Response Headers 에 있는 content-type 에 있는걸 써줘야 한다.(근데 그냥 외우면 됨)
    * */



/**
- MockMvc 를 사용해서 GET, POST 테스트 하기 - 참고: https://shinsunyoung.tistory.com/52
    MockMvc란?
    실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체를 만들어서 애플리케이션 서버에 배포하지 않고도 스프링 MVC 동작을 재현할 수 있는 클래스를 의미합니다.
 
*/
    }
}













