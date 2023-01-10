package com.bitstudy.app.controller;

import com.bitstudy.app.domain.type.FormStatus;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.dto.request.ArticleRequest;
import com.bitstudy.app.dto.response.ArticleResponse;
import com.bitstudy.app.dto.response.ArticleWithCommentsResponse;
import com.bitstudy.app.service.ArticleService;
import com.bitstudy.app.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
        //@RequiredArgsConstructor는 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
@Controller
@RequestMapping("/articles") // 모든 경로들은 /articles 들어가니까 클래스 레벨에 1차로 @RequestMapping("/articles") 걸어놓자
public class Ex36_5_ArticleController_글쓰기구현 {

    /** @RequiredArgsConstructor 로 만들어진 생성자(여기선 articlaService)를 사용할거다.
         쉽게 말하면 @RequiredArgsConstructor 로 만들어진 생성자를 얘가 읽어서 정보의 전달을 할 수 있게 해준다.
         (articleService 에 생성자를 통해 정보들이 다 들어가게 된다.) */
    private final ArticleService articleService;

    /** 페이징 기능 달꺼니까 PaginationService 주입받자 */
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
        /**검색어를 받아야 한다. @RequestParam 를 이용해서 getParameter 를 불러올거고, 얘네들을 반드시 있지 않아도 된다. 없으면 게시글 전체 조회하면 되니까 required = false 해서 null 들어올 수 있게 걸어놓고, 검색어를 입력하면 검색기능으로 이어지게 만들어볼거다.*/
                @RequestParam(required = false) SearchType searchType,
                @RequestParam(required = false) String searchValue,
                @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                // size = 10 : 게시판 한 페이지당 10개씩 보여주겠다는 뜻
                // sort = 뭘 기준으로 하겠다.
                // direction = 내림차순, 오름차순 선택
            ModelMap map
    ) {
        
        /**어짜피 이 아래 map 이나 List 에서 Page 정보가 똑같이 필요하기 때문에 그냥 원래 map 안에 있던 Page 정보를 밖으로 빼서 변수에 담아놓은것뿐임.   */
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);

        /**게시판 페이지에 attribute 를 넣어줘야한다. */
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        /** Page: 전체 데이터 건수를 조회하는 count  쿼리 결과를 포함 하는 페이징
                 데이터 다 가져오기 때문에 getTotalElements() 를 이용해서 개수를뽑거나,
                 getTotalPages() 메서드에 별도의 size 를 줘서 총 페이지 개수를 구할수도 있다.
                 getNumber() 를 이용해서 가져온 페이지의 번호를 뽑을수도 있다.
         Pageable : 페이징 기능.
                    Spring JPA에서 DB 쿼리에 limit 쿼리를 날려서 데이터를 가져온다.*/


        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        /** values() 를 이용해서 enum 인 요소들을 배열로 넘겨준다. */

        return "articles/index";

    }

    
    /** 게시글 상세 페이지    (단건) 조회*/
    // ("[view][GET] 게시글 상세 페이지 - 정상호출") 관련
    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
/*이거 삭제*/ //ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
/*새로 생성*/ ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));
        map.addAttribute("article", article); // 이건 상세페이지용 이기 때문에 아티클이랑 코멘트까지 다 있는 dto를 가져다 쓸거다. 그래서 ArticleWithCommentsResponse 를 쓴다.

        map.addAttribute("articleComments", article.articleCommentsResponse());
                // article.articleCommentsResponse() 해설: 현재 article 에 ArticleWithCommentsResponse 의 정보가 들어있으니까 article 안에 있는 articleComments를 꺼내면 된다.

        /** 뷰 파일에 보낼 모델에 totalCount 라는거 넣어준다. */
        map.addAttribute("totalCount", articleService.getArticleCount());


        return "articles/detail";
    }

/* ------------------------------------------------------ */
/* 새로 추가 - 글쓰기 관련임. 여기서부터 저 아래까지 다 새로 추가함 */
/* ------------------------------------------------------- */
    
    // 주소창에 /form 이라고 친 경우
    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }

    /* 게시글 등록 (글쓰기)*/
    @PostMapping("/form")
    public String postNewArticle(ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.saveArticle(articleRequest.toDto(UserAccountDto.of(
                "bitstudy", "asdf1234", "bitstudy@email.com", "Uno", "memo", null, null, null, null
        )));

        return "redirect:/articles";
    }

    /* 게시글 수정화면 띄우기만 하기 */
    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    /* 게시글 수정한거 업데이트(저장)하기 */
    @PostMapping ("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDto.of(
                "bitstudy", "asdf1234", "bitstudy@email.com", "Uno", "memo", null, null, null, null
        )));

        return "redirect:/articles/" + articleId;
    }

    /* 게시글 삭제하기 */
    /* 원래 delete 는 REST 방식에서는 delete 라고 했었지만 form 태그 쓸때는 get 과 Post만 허용되기 떄문에 어쩔수 없음. */
    @PostMapping ("/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.deleteArticle(articleId);

        return "redirect:/articles";
    }



}