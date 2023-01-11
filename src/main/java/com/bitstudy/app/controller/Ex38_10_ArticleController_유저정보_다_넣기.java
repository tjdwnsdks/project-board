package com.bitstudy.app.controller;

import com.bitstudy.app.domain.type.FormStatus;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.request.ArticleRequest;
import com.bitstudy.app.dto.response.ArticleResponse;
import com.bitstudy.app.dto.response.ArticleWithCommentsResponse;
import com.bitstudy.app.dto.serucity.BoardPrincipal;
import com.bitstudy.app.service.ArticleService;
import com.bitstudy.app.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* 할일 : TODO 로 인증정보 넣으라는 부분들 다 달기 */

@RequiredArgsConstructor // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
//@RequiredArgsConstructor는 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
@Controller
@RequestMapping("/articles") // 모든 경로들은 /articles 들어가니까 클래스 레벨에 1차로 @RequestMapping("/articles") 걸어놓자
public class Ex38_10_ArticleController_유저정보_다_넣기 {

    private final ArticleService articleService;

    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {

        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);

        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

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
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        map.addAttribute("totalCount", articleService.getArticleCount());

        return "articles/detail";
    }

    // 주소창에 /form 이라고 친 경우
    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }

    /** 게시글 등록 (글쓰기)*/
    @PostMapping("/form")
    public String postNewArticle(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            ArticleRequest articleRequest
    ) {
        // TODO: 인증 정보를 넣어줘야 한다.
/*삭제*///articleService.saveArticle(articleRequest.toDto(UserAccountDto.of(
        //       "bitstudy", "asdf1234", "bitstudy@email.com", "Uno", "memo", null, null, null, null
        //)));

/*추가*/ articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/articles";
    }

    /** 게시글 수정화면 띄우기만 하기 */
    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }


    /* 게시글 수정한거 업데이트(저장)하기 */
/* 새로 추가 - 게시글 수정할때도 사용자 인증을 받아야 한다.
            유저 정보를 UserAccountDto.of를 통해서 임의로 만들어진 데이터를 사용했다.
            그러나 이제 사용정보를 받아올수 있으니 boardPrincipal을 이용해서 정의할수 있다  */
    @PostMapping("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId,
                                ArticleRequest articleRequest,
/*새로추가*/                     @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ){
/*삭제*/ //articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDto.of(
        //        "bitstudy", "asdf1234", "bitstudy@email.com", "Uno", "memo", null, null, null, null
        //)));
        //return "redirect:/articles/" + articleId;

/*추가*/ articleService.updateArticle(articleId, articleRequest.toDto(boardPrincipal.toDto()));
            /* boardPrincipal.toDto() 가 userAccountDto 를 반환해준다.
            *   반환된 유저dto 를 articleRequest로 아티클dto로 또 바꿔서  updateArticle 로 보낸다.
            * */
        return "redirect:/articles";
    }

    /** 게시글 삭제하기 */
    @PostMapping ("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal /* 인증정보 가져온다.*/
    ) {
        articleService.deleteArticle(articleId, boardPrincipal.getUsername());

        return "redirect:/articles";
    }
    
    /* 여기까지 하고 ArticleControllerTest 가서 테스트 하면 '삭제하기' 통과함 
    * 
    * 가서 그냥 전체 테스트 돌리면 다 통과함*/



}