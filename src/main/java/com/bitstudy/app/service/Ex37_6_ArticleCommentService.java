package com.bitstudy.app.service;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.ArticleComment;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.repository.ArticleCommentRepository;
import com.bitstudy.app.repository.ArticleRepository;
import com.bitstudy.app.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/*  test > service > Ex37_7_ArticleCommentServiceTest.java   랑 같이 볼것*/

/* 새로 추가*/@Slf4j
@Service // 이렇게 하면 서비스 빈으로 등록되어서 사용할 수 있게 된다.
@RequiredArgsConstructor  // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
@Transactional // 이 클래스 동작할때 하나라도 잘못되면 다시 롤백 시켜라 라는말
public class Ex37_6_ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    /* 새로 생성 - 테스트 코드 중 ("댓글 정보를 입력하면, 댓글을 저장한다.") 에서 유저 정보를 이용하기 때문에 여기 유저 정보 관련꺼 불러온다.  */
    private final UserAccountRepository userAccountRepository;


    @Transactional(readOnly = true) // 조회만 하는거니까 readonly 걸어주면 됨
    public List<ArticleCommentDto> searchArticleComment(long articleId) {
        //return List.of(); // 일단 리턴은 빈껍데기. 일단 TDD에서 에러남 괜찮음. 호출 제대로 되는지만 확인하는거임

        /* 새로 추가*/
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .toList();


    }

    public void saveArticleComment(ArticleCommentDto dto) {
        try {
/* 새로 생성 - 댓글을 썼던 해당 게시글에 대한 정보 불러온거임.  */
            Article article = articleRepository.getReferenceById(dto.articleId());
/* 새로 생성 - 내 댓글의 작성자 정보 가져온거임. */
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            /*엔티티를 만들때는 아티클 뿐만 아니라 회원정보도 넣어서 실제로 댓글 dto 가 엔티티로 바뀔때 댓글 작성자가 들어갈 수 있게 바꿔줘야 한다.
             * ArticleCommentDto.java 가서 toEntity() 메서드 변경하기*/
/* 이거 삭제 */ // articleCommentRepository.save(dto.toEntity(articleRepository.getReferenceById(dto.articleId())));
/* 새로 생성 - dto > Ex37_4_ArticleCommentDto 가서 toEntity 부분 수정하기*/
            articleCommentRepository.save(dto.toEntity(article, userAccount));

        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
            if (dto.content() != null) { articleComment.setContent(dto.content()); }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto: {}", dto);
        }
    }

    public void deleteArticleComment(Long articleCommentId) {
        articleCommentRepository.deleteById(articleCommentId);
    }
}
