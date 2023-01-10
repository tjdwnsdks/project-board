package com.bitstudy.app.service;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.ArticleComment;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.repository.ArticleCommentRepository;
import com.bitstudy.app.repository.ArticleRepository;
import com.bitstudy.app.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


/*  service > Ex37_6_ArticleCommentService.java    랑 같이 볼것
*
* 다 하면 Ex37_8_detail_댓글Form  ㄱㄱ */

/** 간단하게 댓글의 CRUD 관련된 테스트만 만들어볼거다 */


@DisplayName("비지니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class Ex37_7_ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;

    @Mock private ArticleRepository articleRepository; // 우리가 양방향 바인딩으로 연결해놨기 때문에 댓글이 어떤 게시글꺼인지도 알아낼 수 있다.
    @Mock private ArticleCommentRepository articleCommentRepository;

    /* 새로 생성 */ @Mock private UserAccountRepository userAccountRepository;


    /** 댓글 리스트 조회 */
    @DisplayName("게시글 ID 로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void commentsAll() {

        // Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content"); // 저 밑에 테스트용 데이터 셋팅해놨음.
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
        /** given()은 어떤 메서드가 실행되었을 때(!!!!!)의 테스트를 위한 상황을설정할 수 있다. */

        // When
        List<ArticleCommentDto> actual = sut.searchArticleComment(articleId);


        // Then
        assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content",expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }



    /** 댓글 저장 */
    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void createComment() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        /* 새로 생성 - userAccountRepository 한테 요 위에서 생성한 dto 에서 만들었던 userAccountDto 의 userId 를 참조하게 하면 아무렇게나 유저어카운트 만들어서 넘겨주면 된다. 실제 유저의 내용이 뭔지는 중요하지 않기 떄문임. */
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());


        sut.saveArticleComment(dto);

        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
        /* 새로 생성 */then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
    }


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
/* 새로 생성 -  shouldHaveNoInteractions: 이건 아무 인터랙션이 없었다. (아무것도 동작 안했다는 뜻)
         저장 시도했는데 게시글이 없으니까 딱히 given 으로 할 일이 없기 때문에 articleCommentRepository 이게 동작하지 않았어야 정상인거다. */
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
        sut.updateArticleComment(dto); /** 업데이트 시도했을때 */

        // Then
        assertThat(articleComment.getContent()) /** oldContent랑 다르고 updatedContent 랑 같으면 테스트 통과 */
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
                /*이거 삭제 */// 1L,
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
