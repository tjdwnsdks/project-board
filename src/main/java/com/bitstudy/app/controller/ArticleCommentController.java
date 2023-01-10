package com.bitstudy.app.controller;

import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.dto.request.ArticleCommentRequest;
import com.bitstudy.app.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** 할일: 어짜피 아티클 불러올때 에서 댓글을 불러오는 기능까지 다 동작 한다.
        (ArticleController 에서 게시글 상세페이지 조회 부분 보면 ArticleWithCommentsResponse 로 글 정보를 끌어오는데 ArticleWithCommentsResponse 안에는 Set<ArticleCommentResponse> articleCommentsResponse 로 댓글 정보까지 알아서 끌어오고 있다)

        그래서 여기서는 댓글의 작성과 삭제에 대해서만 테스트를 작성 할거임. */

/* Ex37_2_ArticleCommentControlelrTest_댓글_구현  랑 같이 켜고 보기*/

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;
    
    /** 댓글 쓰기*/
    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {
                                    // ArticleCommentRequest: 사용자가 작성한 댓글 관련 데이터만 모은 dto

        /* 아래 saveArticleComment() 없으면 테스트 에러난다.
            새롭게 댓글을 넣을때는 댓글 입력하는 사람 정보도 필요하다.
            다음에 할 '인증기능 구현' 부분에서 스프링 시큐리티를 이용해서 로그인 로그아웃이나 인증 여부기능을 구현한다면 이게 필요 없는데
            지금은 없으니깐 가짜로 만들어서 넣어줄거다.*/
        articleCommentService.saveArticleComment(articleCommentRequest.toDto( // saveArticleComment() 메서드는 ArticleCommentDto 형태만 받을수 있어서 articleCommentRequest 를 dto로 바꿔줘야 한다.
                UserAccountDto.of(
                    "bitstudy", "pw", "bitstudy@mail.com", null, null
                )
            ));

        /* 댓글을 쓰고난 이후 원래 있던 게시글 페이지에 머물러 있어야 하기 때문에 현재 아티클Id 가 필요하다.
        * 그렇지만 ArticleCommentRequest 에 이미 articleId 를 받고 있기 때문에 별도로 받을 필요는 없다.
        * 이 아래 '댓글 삭제' 에서는 별도의 매개변수로 받아야 한다. */
        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    /** 댓글 삭제 */
    @PostMapping ("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId) {
        /* 댓글이 삭제 된 다음 원래 있던 페이지가 리다이렉트 되야 하기 때문에 Long articleId 를 받아놔야 한다. */
        articleCommentService.deleteArticleComment(commentId);

        return "redirect:/articles/" + articleId;
    }


}
