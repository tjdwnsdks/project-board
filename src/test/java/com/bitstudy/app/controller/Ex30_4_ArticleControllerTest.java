package com.bitstudy.app.controller;

import com.bitstudy.app.config.SecurityConfig;
import com.bitstudy.app.dto.ArticleWithCommentsDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 이 파일은 test > controller > Ex30_3_ArticleController 랑 세트임

/**  현재 이 컨트롤러 테스트에서는 기본적인 입출력은 완성된 상태다.
 * 다만 서비스코드에 대한 표현이 안되어 있기 때문에, 이제 서비스코드 관련된것들을 테스트에 반영해볼거다.
 * 그렇다고 실제 서비스코드 (ArticleService) 에 있는 로직을 반영할게 아니고, 그 파일에서 만들어놓은 상황들
 * 예를들어 searchKeyword 없을때, searchKeyword 있을때 같이 이런 상황들에 대한것들을 테스트코드에 추가해볼거다. */


@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
@DisplayName("view 컨트롤러 - 게시글")
class Ex30_4_ArticleControllerTest {

    private final MockMvc mvc;

/* 새로 추가*/ @MockBean private ArticleService articleService;
/** WebMvcTest 에 컨트롤러 단에 의존하는 의존성들은 ArticleController 에 실제로 넣어줘야 한다.
    @MockBean 은 테스트 할때 테스트에 필요한 객체를 기존 객체 대신에 bean 으로 등록시켜서 사용할수 있게 만들어줌.

 ArticleController 에 있는 실제 "private final ArticleService articleService;" 부분의 articleService 를 배제하기 위해서 @MockBean 을 사용했다. 배재하는 이유는 이 테스트에서 MockMvc 가 api 의 입출력만 보게 하기 위해서 서비스 로직을 끊어줘야 하는데 이때 @MockBean 애너테이션을 사용한다.*/

    public Ex30_4_ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


//    @Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상호출") // 뷰 테스트를 get 방식으로 할건데 정상호출만 들어오는 경우로 테스트를 만들거임
    public void articlesAll() throws Exception {
        // Given
/* 새로 추가 - searchKeyword 없을때. 검색어 없이 올거라서 SearchType 과 SearchKeyword 는 null 이고, pageable 은 아무거나 들어가게 any() 를 넣어줄거다. 근데 지금은 아무거나 있던말던 이 아니라 정확히 'null' 이라는게 딱 들어가야 하니까 eq(equal 이란 뜻) 를 써준다.
* return 은 데이터 검증하려는게 아니기 때문에 그냥 empty 페이지 리턴해주면 된다. (Page.empty())  */
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));

/* 새로 추가 */
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        // then: 어떤 mock 이 호출할건지 명시하는 부분. 지금은 articleService mock 을 사용할거다 라는 뜻
        // should() 는 1회 호출했다 라는 뜻.
        // 무슨 일을 한건가 라는 뜻으로 should 다음에 given 에서 설정했던걸 한번 더 써준다.( searchArticles(eq(null), eq(null), any(Pageable.class));)
        // 한마디로 articleService 가 searchArticles() 메서드를 한번 호출했냐 나는 뜻.

    }

/*아직 실제 ArticleController 에 구현 안했으니까 테스트 해봤자 에러난다.
    아래 테스트까지 하고 ArticleController 가서 구현부 만들고 테스트 해보자.*/


    //@Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출")
    public void articlesOne() throws Exception {
        // Given
/* 새로 추가 - 상세페이지니까 기본 게시글 번호 1번 으로 줄거다. */
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
                                                // willReturn( 검사를 위한 Dto 를 넣어줘야함)
                                                // dto 를 만들어야 해서 createArticleWithCommentsDto() 메서드를 만들었다.

        // 원래는 위에 given 은 이거여야함
        // given(articleService.getArticleWithComments(articleId)).willReturn(createArticleWithCommentsDto());

        // When & Then
        mvc.perform(get("/articles/" + articleId)) /* 테스트니까 그냥 1번글 가져와라 할거임 */
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail")) // 이건 해당 뷰 파일명이 detail 인지 확인
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments")); // 상세페이지에는 댓글들도 여러개 있을수도 있으니까 모델 어트리뷰트에 articleComments 라는 키값으로 된게 있냐 라고 물어보는거
/* 새로 생성*/ then(articleService).should().getArticle(articleId);
            // articleService 가 한번 호출해야 한다 getArticle(articleId) 를.
    }

/* '게시글 검색 전용 페이지' 와  '해시태그'는 아직 계획이 분명하진 않으니까 넘어간다.
   이제 ArticleController 가서 구현부 만들고 테스트 해보자.*/


/**************************************************************/
/**  다 하면 뷰 파일로 넘어가자.
    main > resource > templates > articles > Ex30_5_index.html 랑 Ex30_6_index.th.xml 켜자
*/
/**************************************************************/

/*/////////////  '게시글 검색 전용 페이지' 와  '해시태그'는 아직 계획이 분명하진 않으니까 넘어간다.     ////////////////*/
/*/////////////  '게시글 검색 전용 페이지' 와  '해시태그'는 아직 계획이 분명하진 않으니까 넘어간다.     ////////////////*/
//    @Disabled("구현 중")
//    @Test
//    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상호출")
//    public void articlesSearch() throws Exception {
//        // Given
//
//        // When & Then
//        mvc.perform(get("/articles/search"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
///* 새로 생성*/   .andExpect(model().attributeExists("articles/search")); // 이건 해당 뷰 파일명이 search 인지 확인
//
//        /* 이 테스트는 검색 페이지만 뿌려주면 되는거라서 아직 데이터를 받아오지 않았을 상태라서 이건 필요 없음 */
//        // .andExpect(model().attributeExists("articles"));
//    }
//
//    @Disabled("구현 중")
//    @Test
//    @DisplayName("[view][GET] 게시판 해시태그 검색 전용 페이지 - 정상호출")
//    public void articlesSearchHashtag() throws Exception {
//        // Given
//
//        // When & Then
//        mvc.perform(get("/articles/search-hashtag"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//
//                /* 이 테스트는 검색 페이지만 뿌려주면 되는거라서 아직 데이터를 받아오지 않았을 상태라서 이건 필요 없음 */
//                // .andExpect(model().attributeExists("articles"));
//
///* 새로 생성*/   .andExpect(model().attributeExists("articles/search-hashtag")); // 이건 해당 뷰 파일명이 search-hashtag 인지 확인
//    }
/*/////////////  '게시글 검색 전용 페이지' 와  '해시태그'는 아직 계획이 분명하진 않으니까 넘어간다.     ////////////////*/

    /* 아티클 코멘트 만드는 메서드 */
    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "bitstudy",
                LocalDateTime.now(),
                "bitstudy"
        );
    }

    /* 유져 어카운트 DTO 만드는 메서드 */
    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(1L,
                "bitstudy",
                "pw",
                "bitstudy@email.com",
                "bitstudy",
                "memo",
                LocalDateTime.now(),
                "bitstudy",
                LocalDateTime.now(),
                "bitstudy"
        );
    }

}














