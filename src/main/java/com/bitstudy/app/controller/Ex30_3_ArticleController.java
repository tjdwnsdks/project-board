package com.bitstudy.app.controller;

import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.response.ArticleResponse;
import com.bitstudy.app.dto.response.ArticleWithCommentsResponse;
import com.bitstudy.app.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/* ArticleControllerTest 하고 와야함 */

// 이 파일은 test > controller > Ex30_4_ArticleControllerTest 랑 세트임

/** 뷰 엔드포인트 관련 컨트롤러
 * 엑셀 api 에 보면 우리가 정의해놓은 URL 부분들 있다. 그거 보면서 하면 됨(아래 있는거)
     /articles	                GET	게시판 페이지
     /articles/{article-id}	    GET	게시글(상세)페이지
     /articles/search	        GET	게시판 검색 전용 페이지
     /articles/search-hashtag	GET	게시판 해시태그 검색 전용 페이지

 * 모든 경로들은 /articles 들어가니까 클래스 레벨에 1차로 @RequestMapping("/articles") 걸어놓자
 * */

@RequiredArgsConstructor // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
        //@RequiredArgsConstructor는 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
@Controller
@RequestMapping("/articles30") // 모든 경로들은 /articles 들어가니까 클래스 레벨에 1차로 @RequestMapping("/articles") 걸어놓자
public class Ex30_3_ArticleController {

    /* 새로 추가 - @RequiredArgsConstructor 로 만들어진 생성자(여기선 articlaService)를 사용할거다.
                 쉽게 말하면 @RequiredArgsConstructor 로 만들어진 생성자를 얘가 읽어서 정보의 전달을 할 수 있게 해준다.
                 (articleService 에 생성자를 통해 정보들이 다 들어가게 된다.) */
    private final ArticleService articleService;
    
    /* @RequiredArgsConstructor 가 없다면 아래 코드를 사용해야함*/
/**     @Autowired
        public ArticleController(ArticleService articleService) {
            this.articleService = articleService;
        } */

 
    /* ArticleControllerTest 에서 첫번째꺼 '게시글 리스트 (게시판) 페이지' 테스트 관련*/
    // ("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상호출") 관련
    @GetMapping
    public String articles(
        /**검색어를 받아야 한다. @RequestParam 를 이용해서 getParameter 를 불러올거고, 얘네들을 반드시 있지 않아도 된다. 없으면 게시글 전체 조회하면 되니까 required = false 해서 null 들어올 수 있게 걸어놓고, 검색어를 입력하면 검색기능으로 이어지게 만들어볼거다.*/
/* 새로 생성*/ @RequestParam(required = false) SearchType searchType,
/* 새로 생성*/ @RequestParam(required = false) String searchValue,
/* 새로 생성*/ @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                // size = 10 : 게시판 한 페이지당 10개씩 보여주겠다는 뜻
                // sort = 뭘 기준으로 하겠다.
                // direction = 내림차순, 오름차순 선택
            ModelMap map
    ) {
/* 이거 없앰*/ // map.addAttribute("articles", List.of()); // 가짜로 비어있는 리스트 넣어줬었는데
/* 새로 생성*/ map.addAttribute("articles", articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from)); // 이젠 진짜로 넣어줘야 하니까 ArticleService.java 안에 만들어놓은 searchArticles() 메서드에 값 넣어주면 됨. 그런데 searchArticles 의 반환타입은 DTO 인데 dto 는 모든 엔티티의 데이터를 다 다루고 있어서 그걸 한번 더 가공해서 필요한것만 쓸건데 그래서 게시글 내용만 가지고 있는 ArticleResponse 사용한다.

        return "articles/index";

        /* 여기까지 하고 ArticleControllerTest 의 
         ("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상호출")
         테스트 하러 가기 */
    }

    
    /* 게시글 상세 페이지    (단건) 조회*/
    // ("[view][GET] 게시글 상세 페이지 - 정상호출") 관련
    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
/* 이거 삭제 */// map.addAttribute("article", "article"); // TODO: 구현할 때 여기에 실제 데이터를 넣어줘야 한다

             // ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
/* 새로 생성 */ ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));
/* 새로 생성 */ map.addAttribute("article", article); // 이건 상세페이지용 이기 때문에 아티클이랑 코멘트까지 다 있는 dto를 가져다 쓸거다. 그래서 ArticleWithCommentsResponse 를 쓴다.

/* 이거 삭제 */ // map.addAttribute("articleComments", List.of());
/* 새로 생성 */ map.addAttribute("articleComments", article.articleCommentsResponse());
                // article.articleCommentsResponse() 해설: 현재 article 에 ArticleWithCommentsResponse 의 정보가 들어있으니까 article 안에 있는 articleComments를 꺼내면 된다.

        return "articles/detail";
    }

        /* 여기까지 하고 ArticleControllerTest 의
         ("[view][GET] 게시글 상세 페이지 - 정상호출")
         테스트 하러 가기 */
}

/* 테스트 성공하면, 컨트롤러에서 실질적인 데이터를 올려주는 부분까지가 된거다.*/