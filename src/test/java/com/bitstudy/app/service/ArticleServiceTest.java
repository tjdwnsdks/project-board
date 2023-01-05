package com.bitstudy.app.service;


import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.dto.ArticleWithCommentsDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Disabled;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


/** 서비스 비지니스 로직은 스프링 부트의 슬라이스 테스트 기능을 사용하지 않고 작성해볼거다.
 그렇게 함으로서 스프링부트 애플리케이션 컨텍스트가 뜨는데 걸리는 시간을 없애버리려고 한다.
 가능한한 가볍게 만들거고, 불필요한 스프링부트 어플리케이션을 띄우는 과정을 생략하는 대신에 디펜던시가 추가되어야 하는 부분이 있다면 Mocking 을 하는 방식으로 해볼거다.
 그래서 많이 사용하는 라이브러리가 mokito 라는게 있다. 스프링 테스트 패키지에 이미 포함되어 있어서 따로 설치할 필요는 없다.
 @ExtendWith(MockitoExtension.class) 쓰면 된다.

  * 이번에는 junit 안쓰고 assertJ 써볼거다.
 import 부분 수정하자
  * */


@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    /* Mock을 주입하는 거에다가 @InjectMocks 를 달아줘야 한다. 그 외의 모든 Mock은 @Mock 을 달아준다. */
    @InjectMocks private ArticleService sut; // sut - system under test 라고 해서. 실무에서 테스트 짤대 사용하는 이름중 하나다. 이게 테스트 대상이다 라는뜻임.
    // Mock 을 주입하는 대상에는 @InjectMocks 에너테이션을 주고 , 그 외 나머지 모든 Mock 은 @Mock 애너테이션을 준다.

    @Mock private ArticleRepository articleRepository; // 여기 의존하는 걸 가져와야함. 테스트 중간에 mocking 할때 필요함.


    /** - 테스트 할 기능들 리스트(칸반보드에서 가져옴)
     1. 검색
     2. 각 게시글 페이지로 이동
     3. 페이지네이션
     4. 홈버튼 클릭시 -> 게시판 페이지로 리다이렉션   <= 이건 버튼 누르면 페이지 이동하는거라서 컨트롤러에서 해야함.
     * */


    /*  1. 검색  */
/* 새로 삽입*/ @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
/* 이거 삭제 *///@DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void returnArticlesAll() {
        // Given - 페이징 기능을 넣어볼거다.
        Pageable pageable = Pageable.ofSize(20); // 한페이지에 몇개 가져올건지 결정.
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
        /** findAll 추천리스트 보면 'findAll(Pageable pageable)' 이라는게 있다. 이걸 PageRequest 라고 하는데, PageRequest는 Pageable 클래스를 implements한 AbstractPageReqeust 추상 클래스의 구현체이므로 findAll의 인자로 넣을 수 있다.
         * Repository의 findAll 메서드의 인자에 PageRequest를 넣어주면 된다.
         * 그러면 반환은 Page이 된다. */

        // When - 입력인자가 없는경우(null) 를 테스트 하는거
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);

        /* 위에거 끝나면 아래 3개 작업 하기
            1) main > dto 패키지 만들고, 그 안에 ArticleDto.java 만들기
            2) main > domain 패키지 않에 type 패키지 만들고 그 안에 (class 말고) enum 으로 SearchType.java 만들기
            3) main > ArticleService.java 에 searchArticles 메서드 만들기 */

        /* 여기까지 하고 selectAll() 메서드 테스트 해보기*/
    }

/* 새로 삽입*/
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


    /*  2. 각 게시글 페이지로 이동  */
    @DisplayName("게시글을 조회하면, 게시글을(하나) 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given
        Long articleId = 1L;  /* 아이디 하나 (1번) 넣었을때 */
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsDto dto = sut.getArticle(articleId);

        // Then
        assertThat(dto)  /* 게시글을 하나 반환할건데, 그때 필드는 제목, 본문, 해시태그가 있을거다. */
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

        // When
        sut.saveArticle(dto);

        // Then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
        /* getReferenceById: dto에 담겨있는(지금은 게시글) 레퍼런스(참조)를 가져온다
                            이건 findByID 랑 비슷한데 findByID는 단건조회 할때 사용하고, 조회시 필요 없는경우에도 무조건 엔티티 조회를 하는 쿼리를 날려야 한다. 무슨 말이냐면 findById(dto.id()) 같이 넣어주면 jpa 가 select 쿼리를 만들어서 특정 게시글을 조회 하고, 그 다음에 update 를 하던 뭘 하던 한다는건데, 지금처럼 수정에 관련된 작업을 한다는건 개발자 입장에서 해당 게시글이 존재한다 라는 가정하에 코드를 짜고 있는중이라서 select 까지 괜히 돌릴 필요가 없다.
                            혹시라도 해당 id가 존재하지 않으면 그부분은 별도로 코드를 짜면 된다.
                            그래서 getReferenceById 는 그런 불필요한 작업들 하지 말고 그냥 참조만 해라(지금 당장 불러오지는 말아라) 라는 작업을 할거다. 얘는 lazy loading 을 하는 애라서 필요할때 DB에 접근만 하고 만다. 별도의 쿼리는 날리지 않는다. (연결책 정도??)

                    요약하면 findByID 는 select 쿼리를 가져와서 내 주머니에 넣는거.
                            getReferenceById 는 내가 원하는게 어디 있는지만 알려줘서 나중에 그거 건드려야 할때 내가 거기에 가서 건드릴수 있게 해주는거
        * */

        // When
        sut.updateArticle(dto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleRepository).deleteById(articleId);

        // When
        sut.deleteArticle(1L);

        // Then
        then(articleRepository).should().deleteById(articleId);
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
        return ArticleDto.of(1L,
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

}
