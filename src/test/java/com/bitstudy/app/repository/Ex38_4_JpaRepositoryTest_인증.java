package com.bitstudy.app.repository;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/* 할일: 전체 테스트 돌려보면 insert() 메서드만 오류난다.
    오류 내용은 createdBy가 null 이라는건데,
    securityconfig의 UserDetailsService 에서 이제 userAccountRepository를 건드리게 되는데
    이 녀석이 제대로 빈으로 등록 되어있지 않거나, 인증 정보 혹은 사용자의 정보가 들어있지 않으면 테스트가 실패하게 되는것이다.
    따라서 서큐리티 전용 설정을 작성해서 추가해주는 것이 좋다.
    
    맨 아래 TestJpaConfig 클래스 만들어서 테스트에서만 돌아갈 수 있는 전용 config 파일을 만들자
    
*  */

// @ActiveProfiles("testdb") /* 이건 당장 하지 말고, 다 끝난다음 application.yaml 파일의 맨 아래 testdb 부분 보고 와서 하기 */
@DisplayName("JPA 연결 테스트") // 현재 테스트 이름- 이게 원래 나와야 하는데 버그가 있어서 현재 제트브레인에서 고치고 있다고 함. 다른 메서드 부분에서는 제대로 나옴
//@Import(JpaConfig.class)
@Import(Ex38_4_JpaRepositoryTest_인증.TestJpaConfig.class)
@DataJpaTest // 슬라이스 테스트를 할거다(지난번 TDD 때 각 메서드들은 다 남남으로 각각 테스트한 결과를 못보게 만들어야 한다.). 그런데 이거는 우리가 만들었던 config 패키지의 JpaConfig 파일을 읽어오지 못한다. (우리가 따로 만들거라서 그렇다.)
        // 그래서 따로 import 해줘야 한다. 안하면 config 안에 명시해놨던 JpaAuditing 기능이 안먹는다.
        // 그리고 테스트 범위의 메소드 들은 테스트가 끝나면 롤백으로 원래 기본값으로 돌아가게 한다.
        // (이부분은 아래 update 관련 메소드 에서 한번 더 말해야함)
class Ex38_4_JpaRepositoryTest_인증 {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;


    // 생성자 만들기. 여기선 다른 파일에서 매개변수로 보내주는거를 받는거라서 위에랑 상관 없이 @Autowired 붙여야함
    public Ex38_4_JpaRepositoryTest_인증(
                /**@Autowired Ex04_ArticleRepository articleRepository,
                @Autowired Ex05_ArticleCommentRepository articleCommentRepository,*/
                @Autowired ArticleRepository articleRepository,
                @Autowired ArticleCommentRepository articleCommentRepository,
                @Autowired UserAccountRepository userAccountRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }


    /** select 테스트 */
    @DisplayName("select 테스트") // 이 메소드의 테스트 이름은 'select 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 selecting 할때 잘 동작하는 경우
    void selectTest() {
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(100); // 게시글은 100개
    }


    /** insert 테스트 */
    @DisplayName("insert 테스트") // 이 메소드의 테스트 이름은 'select 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 inserting 할때 잘 동작하는 경우
    void insertTest() {

        // Given
        /** 기존카운트 구하고 */
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("new_bitstudy","asdf",null,null,null));

        Article article = Article.of(userAccount,"new article", "new content", "#spring");

        // When - 테스트 해야 하는 내용
        articleRepository.save(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    /** update 테스트 */
    @DisplayName("update 테스트") // 이 메소드의 테스트 이름은 'update 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 updateing 할때 잘 동작하는 경우
    void updateTest() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtag = "#springboot";
        article.setHashtag(updateHashtag);

        // When - 테스트 해야 하는 내용
        Article savedArticle = articleRepository.saveAndFlush(article);

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashtag);

    }
    /** delete 테스트 */
    @DisplayName("delete 테스트") // 이 메소드의 테스트 이름은 'delete 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 deleteing 할때 잘 동작하는 경우
    void deleteTest() {
        Article article = articleRepository.findById(1L).orElseThrow();

        long previousArticleCount =  articleRepository.count();
        long previousArticleCommentCount =  articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // When - 테스트 해야 하는 내용
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }


/* 새로 생성 - auditing을 자동으로 넣는 부분을 테스트때만 시큐리티를 무시하게 만들기 */
    @EnableJpaAuditing
    @TestConfiguration // 테스트 할떄만 Configuration 로 동작.
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("bitstudy");
            // 우리가 시큐리티 적용하기 전에 썼던대로 쓰는거임.
        }
    }
    
    /* 여기까지 하면 맨 위에 가서 
        @Import(JpaRepositoryTest.TestJpaConfig.class)넣고
        전체 테스트 돌리기 
        
        그럼 다 통과함
    */
}












