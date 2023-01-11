package com.bitstudy.app.service;


import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.dto.ArticleWithCommentsDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.repository.ArticleRepository;
import com.bitstudy.app.repository.UserAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


/** 할일: ("게시글의 ID를 입력하면, 게시글을 삭제한다") 부분에 userId 관련 추가  * */


@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class Ex38_10_ArticleServiceTest_업데이트에_유저정보_추가 {

    /** Mock을 주입하는 거에다가 @InjectMocks 를 달아줘야 한다. 그 외의  Mock은 @Mock 을 달아준다. */
    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository articleRepository;

    @Mock private UserAccountRepository userAccountRepository;

    /**  1. 검색  */
    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void returnArticlesAll() {
        // Given - 페이징 기능을 넣어볼거다.
        Pageable pageable = Pageable.ofSize(20); // 한페이지에 몇개 가져올건지 결정.
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        // When - 입력인자가 없는경우(null) 를 테스트 하는거
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);

    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }




    /**  2. 각 게시글 페이지로 이동  */
    @DisplayName("게시글 ID로 조회하면, 게시글을(하나) 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given
        Long articleId = 1L;  /** 아이디 하나 (1번) 넣었을때 */
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleDto dto = sut.getArticle(articleId);

        // Then
        assertThat(dto)  /** 게시글을 하나 반환할건데, 그때 필드는 제목, 본문, 해시태그가 있을거다. */
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글 ID로 조회하면, 댓글 달긴 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleWithComments_thenReturnsArticleWithComments() {
        // Given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }


    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
        // Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = Assertions.catchThrowable(() -> sut.getArticle(articleId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        // Given
        ArticleDto dto = createArticleDto(); // 게시글 정보를 입력하면 게시글을 생성한다
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());

        // When
        sut.saveArticle(dto);

        // Then
        then(articleRepository).should().save(any(Article.class));
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
            // 게시글정보 입력시 게시글 생성 여부 테스트
    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
/*새로추가*/ given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(dto.userAccountDto().toEntity());
/*해석: userAccountRepository에서 getReferenceById를 통해 사용자의 Id값을 가져오고,
        userAccountDto의 toEntity로 데이터를 전송할 것이다.*/
        // When
        sut.updateArticle(dto.id(), dto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(articleRepository).should().getReferenceById(dto.id());
/*새로추가*/ then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        //sut.updateArticle(dto);
        sut.updateArticle(dto.id(), dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
/* 새로 추가*/ String userId = "bitstudy";

/* 이거 삭제 *///willDoNothing().given(articleRepository).deleteById(articleId, userId);
/* 변경 */ willDoNothing().given(articleRepository).deleteByIdAndUserAccount_UserId(articleId, userId);

        // When
/* 이거 삭제 */// sut.deleteArticle(1L);
/* 변경 */ sut.deleteArticle(1L, userId);
        // Then
/* 이거 삭제 *///then(articleRepository).should().deleteById(articleId, userId);
/* 변경 */ then(articleRepository).should().deleteByIdAndUserAccount_UserId(articleId, userId);


/* 다 했으면 ArticleControllerTest 로 돌아가기*/

    }


    @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다")
    @Test
    void givenNothing_whenCountingArticles_thenReturnsArticleCount() {
        // Given
        long expected = 0L;
        given(articleRepository.count()).willReturn(expected);

        // When
        long actual = sut.getArticleCount();

        // Then
        assertThat(actual).isEqualTo(expected);
        then(articleRepository).should().count();
    }
    /////////////////////


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

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                LocalDateTime.now(),
                "bitstudy",
                LocalDateTime.now(),
                "bitstudy");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
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

}
