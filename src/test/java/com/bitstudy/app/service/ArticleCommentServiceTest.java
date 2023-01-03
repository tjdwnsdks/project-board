package com.bitstudy.app.service;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.ArticleComment;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.repository.ArticleCommentRepository;
import com.bitstudy.app.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

//import static org.junit.jupiter.api.Assertions.*;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

/** 간단하게 댓글의 CRUD 관련된 테스트만 만들어볼거다 */


@DisplayName("비지니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;

    @Mock private ArticleRepository articleRepository; // 우리가 양방향 바인딩으로 연결해놨기 때문에 댓글이 어떤 게시글꺼인지도 알아낼 수 있다.
    @Mock private ArticleCommentRepository articleCommentRepository;


    /** 댓글 리스트 조회 */
    @DisplayName("게시글 ID 로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void commentsAll() {

        // Given
        /* 특정 게시글(1L) 을 기준으로, 이 내용들을 가지고 검색을 할건데 그 게시글의 댓글들이 있으면 일단 다 리턴해라 라는 뜻 */
/* 이거 삭제 */// given(articleRepository.findById(1L)).willReturn(Article.of("title","content","#hashtag"));
        // 그냥 이렇게만 쓰면 편한다. findById 는 Optional 로 포장해야한다.
        Long articleId = 1L;
/* 새로 삽입*/ ArticleComment expected = createArticleComment("content"); // 저 밑에 테스트용 데이터 셋팅해놨음.
/* 새로 삽입*/ given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
            /** given()은 어떤 메서드가 실행되었을 때(!!!!!)의 테스트를 위한 상황을설정할 수 있다. */

        // When
        // 받아오는 댓글들을 List에 담을거라서 List<ArticleComment>에 저장함.
        // ArticleComment 를 검색할때는 아티클 id 로부터 검색을 해서 댓글을 가져올거다.
        /* 1) List<ArticleCommentDto> 때문에 DTO 만들고 오기.  ArticleCommentDto 생성
         *  2) main > service > ArticleCommentService 에 searchArticleComment() 메서드 만들고 오기
         *  */
/* 이거 삭제 */// List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
/* 새로 삽입*/ List<ArticleCommentDto> actual = sut.searchArticleComment(articleId);


        // Then
/* 이거 삭제 */// assertThat(articleComments).isNotNull();
/* 이거 삭제 *///then(articleRepository).should().findById(articleId);

/* 새로 삽입*/ assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content",expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }



    /** 댓글 저장 */
    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void createComment() {
        // Given
/* 새로 삽입*/ ArticleCommentDto dto = createArticleCommentDto("댓글");
/* 새로 삽입*/ given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

/* 이거 삭제 *///sut.saveArticleComment(ArticleCommentDto.of(LocalDateTime.now(), "bitstudy",LocalDateTime.now(),"bitstudy", "comment"));
/* 새로 삽입*/ sut.saveArticleComment(dto);

/* 새로 삽입*/ then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }


/* 이 아래로 다 새로 삽입*/
    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);

        // When
        sut.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
    @Test
    void updateArticleComment() {
        // Given
        String oldContent = "content";
        String updatedContent = "댓글";
        ArticleComment articleComment = createArticleComment(oldContent);
        ArticleCommentDto dto = createArticleCommentDto(updatedContent);
        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);

        // When
        sut.updateArticleComment(dto); /* 업데이트 시도했을때 */ 

        // Then
        assertThat(articleComment.getContent()) /* oldContent랑 다르고 updatedContent 랑 같으면 테스트 통과 */
                .isNotEqualTo(oldContent) 
                .isEqualTo(updatedContent);
        then(articleCommentRepository).should().getReferenceById(dto.id()); // mock 이 제대로 불러와 졌는지 확인하는 용도
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무 것도 안 한다.")
    @Test
    void givenNonexistentArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticleComment(dto);

        // Then
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);

        // When
        sut.deleteArticleComment(articleCommentId);

        // Then
        then(articleCommentRepository).should().deleteById(articleCommentId);
    }


    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
// 단순히 테스트용 데이터 세팅한거. 픽스쳐 라고 함.
// 테스트코드 내내 반복적으로 활용될 가능성이 큰 경우 별도로 이렇게 메서드나 클래스로 두고 불러다 쓰는 방식인데
// 이 방식을 쓰면 테스트 코드의 부피를 줄일수 있고, 반복을 피할 수 있다.
// 우리 강의에서는 그렇게까지 일일히는 하지 않지만 리팩토링의 여지를 남겨두기 위해서 이렇게 한다.
// 실무 가면 하게 될거임.

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
                LocalDateTime.now(),
                "bitstudy",
                LocalDateTime.now(),
                "bitstudy"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "bitstudy",
                "password",
                "bitstudy@email.com",
                "bitstudy",
                "This is memo",
                LocalDateTime.now(),
                "bitstudy",
                LocalDateTime.now(),
                "bitstudy"
        );
    }

    private ArticleComment createArticleComment(String content) {
        return ArticleComment.of(
                Article.of(createUserAccount(), "title", "content", "hashtag"),
                createUserAccount(),
                content
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "bitstudy",
                "password",
                "bitstudy@email.com",
                "bitstudy",
                null
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

}
