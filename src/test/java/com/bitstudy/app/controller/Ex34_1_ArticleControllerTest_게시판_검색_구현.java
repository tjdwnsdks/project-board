package com.bitstudy.app.controller;

import com.bitstudy.app.config.SecurityConfig;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleWithCommentsDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.service.ArticleService;
import com.bitstudy.app.service.PaginationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*  할일: 제목, 본문, 이름 들로 검색되게 해볼거다. */

@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
@DisplayName("view 컨트롤러 - 게시글")
class Ex34_1_ArticleControllerTest_게시판_검색_구현 {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;
    /** WebMvcTest 에 컨트롤러 단에 의존하는 의존성들은 ArticleController 에 실제로 넣어줘야 한다.
        @MockBean 은 테스트 할때 테스트에 필요한 객체를 기존 객체 대신에 bean 으로 등록시켜서 사용할수 있게 만들어줌.

     ArticleController 에 있는 실제 "private final ArticleService articleService;" 부분의 articleService 를 배제하기 위해서 @MockBean 을 사용했다. 배재하는 이유는 이 테스트에서 MockMvc 가 api 의 입출력만 보게 하기 위해서 서비스 로직을 끊어줘야 하는데 이때 @MockBean 애너테이션을 사용한다.*/

    /** 이제 페이징을 사용하는 부분에 모두 paginationService 를 이용하게 될거다. Page 를 리턴하는 곳엔 다 들어가야한다. */
    @MockBean private PaginationService paginationService;
    public Ex34_1_ArticleControllerTest_게시판_검색_구현(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @Test
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출") // 뷰 테스트를 get 방식으로 할건데 정상호출만 들어오는 경우로 테스트를 만들거임
    public void articlesAll() throws Exception {
        // Given
        /** searchKeyword 없을때. 검색어 없이 올거라서 SearchType 과 SearchKeyword 는 null 이고, pageable 은 아무거나 들어가게 any() 를 넣어줄거다.
            근데 지금은 아무거나 있던말던 이 아니라 정확히 'null' 이라는게 딱 들어가야 하니까 eq(equal 이란 뜻) 를 써준다.
            return 은 데이터 검증하려는게 아니기 때문에 그냥 empty 페이지 리턴해주면 된다. (Page.empty())  */
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());


        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
        /** 여기서는 페이징을 하는 기능 자체가 테스트 대상이 아니고 정상적인 호출이 되는지 보는거고
           매개변수 보내야 하는데 getPaginationBarNumbers(정수, 정수) 로 매개변수를 보내야 하는데
            그렇다고 any() 를 쓸 수는 없다. 왜냐면 null 을 허용하니까.
            그래서 아무 숫자를 의미하는 anyInt 를 넣어준다.
            그리고 예상하는 리턴도 아무 리스트 만들어서 보내면 된다.  */


        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
        // then: 어떤 mock 이 호출할건지 명시하는 부분. 지금은 articleService mock 을 사용할거다 라는 뜻
        // should() 는 1회 호출했다 라는 뜻.
        // 무슨 일을 한건가 라는 뜻으로 should 다음에 given 에서 설정했던걸 한번 더 써준다.( searchArticles(eq(null), eq(null), any(Pageable.class));)
        // 한마디로 articleService 가 searchArticles() 메서드를 한번 호출했냐 나는 뜻.
    }

/* 새로 생성 */
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE; /* 검색어가 있게 할건데 이번 테스트때는 제목 검색으로 테스트 한다.*/
        String searchValue = "title"; /* 키워드는 title*/
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        /* 여기서도 페이징은 테스트와 상관 없으니까 그냥 any(Pageable) 할거고, 결과도 페이지는 없어도 되니까 Page.empty  */
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchType.name()) /* get 파라미터 추가 - url에 /articles?searchType= 이런식 */
                        .queryParam("searchValue", searchValue)     
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes")); /* 검색 타임(유형)이 뭔지 뷰에 보내서 select option 부분도 현재 뭐로 검색된건지 바뀌게 하게*/
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
        
        /* 이상태로 테스트 돌리면 실패함. ArticleController 에서 구현해야 제대로 돌음
        *
        * Ex34_2_ArticleController.java  ㄱㄱ
        * */
    }




    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        /** sort 관련 */
        String sortName = "title";
        String direction = "desc";

        /** paging 관련 */
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5); /* 이번에는 검증 대상이 페이징 기능이니까 따로 페이지 리스트를 따로 빼서 만들었음 */

        /** 이건 위에 테스트꺼랑 똑같음 */
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());

        /** 이번엔 anyInt 대신 실제로 값을 보낼거임 */
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(get("/articles")
                        .queryParam("page", String.valueOf(pageNumber))
                        .queryParam("size", String.valueOf(pageSize))
                        .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
        

    }



    //@Disabled("구현 중")
    @Test
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상호출")
    public void articlesOne() throws Exception {
        // Given
        /**  상세페이지니까 기본 게시글 번호 1번 으로 줄거다. */
        Long articleId = 1L;

        /** 상세페이지니까 기본 게시글 개수 1개 있다고 가정할거다. */
        long totalCount = 1L; /* Long 은 null 사용 가능, long 은 불가.  ( int 와 Integer 와 같음) */

        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
                                                // willReturn( 검사를 위한 Dto 를 넣어줘야함)
                                                // dto 를 만들어야 해서 createArticleWithCommentsDto() 메서드를 만들었다.

/* 새로 입력 - 게시글 개수 구하기 */
        given(articleService.getArticleCount()).willReturn(totalCount);

        // When & Then
        mvc.perform(get("/articles/" + articleId)) /* 테스트니까 그냥 1번글 가져와라 할거임 */
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail")) // 이건 해당 뷰 파일명이 detail 인지 확인
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments")) // 상세페이지에는 댓글들도 여러개 있을수도 있으니까 모델 어트리뷰트에 articleComments 라는 키값으로 된게 있냐 라고 물어보는거
/* 새로 생성*/    .andExpect(model().attribute("totalCount", totalCount)); /* 뷰 파일에 totalCount 라는 이름으로 모델 넘길거임*/

        then(articleService).should().getArticle(articleId);

/* 새로 생성*/
        then(articleService).should().getArticleCount();

/* ArticleController 에 ("[view][GET] 게시글 상세 페이지 - 정상호출") 관련 부분에 새로 map.addAttribute("totalCount", articleService.getArticleCount()); 해줘야 함 */
    }




/**************************************************************/
/**  다 하면 뷰 파일로 넘어가자.
 main > resource > templates > articles > Ex30_5_index.html 랑 Ex30_6_index.th.xml 켜자
 */
    /**************************************************************/

    /** /////////////  '게시글 검색 전용 페이지' 와  '해시태그'는 아직 계획이 분명하진 않으니까 넘어간다.     ////////////////*/
    /** /////////////  '게시글 검색 전용 페이지' 와  '해시태그'는 아직 계획이 분명하진 않으니까 넘어간다.     ////////////////*/
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
/* 이상태로 테스트를 돌려보면 해당 내용들이 DB에 없기 때문에 404 에러 나야한다.
* 일단 테스트는 작성 되었다. 데이터는 깃크라켄 가서 커밋 하고 오자 -  워드파일 돌아가기*/


















