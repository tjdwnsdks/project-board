package com.bitstudy.app.repository;

import com.bitstudy.app.config.JpaConfig;
import com.bitstudy.app.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

// @ActiveProfiles("testdb") /* 이건 당장 하지 말고, 다 끝난다음 application.yaml 파일의 맨 아래 testdb 부분 보고 와서 하기 */
@DisplayName("JPA 연결 테스트") // 현재 테스트 이름- 이게 원래 나와야 하는데 버그가 있어서 현재 제트브레인에서 고치고 있다고 함. 다른 메서드 부분에서는 제대로 나옴
@Import(JpaConfig.class)
@DataJpaTest // 슬라이스 테스트를 할거다(지난번 TDD 때 각 메서드들은 다 남남으로 각각 테스트한 결과를 못보게 만들어야 한다.). 그런데 이거는 우리가 만들었던 config 패키지의 JpaConfig 파일을 읽어오지 못한다. (우리가 따로 만들거라서 그렇다.)
        // 그래서 따로 import 해줘야 한다. 안하면 config 안에 명시해놨던 JpaAuditing 기능이 안먹는다.
        // 그리고 테스트 범위의 메소드 들은 테스트가 끝나면 롤백으로 원래 기본값으로 돌아가게 한다.
        // (이부분은 아래 update 관련 메소드 에서 한번 더 말해야함)
class Ex04_JpaRepositoryTest {

    private final Ex04_ArticleRepository articleRepository;
    private final Ex05_ArticleCommentRepository articleCommentRepository;

    /*
        원래는 각각 앞에 @Autowired 를 붙여야 하는데, JUnit5 버전과 최신버전 스프링부트를 이용하면 Test 에서도 생성자 주입패턴을 사용할 수 있다.
        맨 위에 @DataJpaTest 어노테이션에 마우스 호버해서 확장해보면, 그 안에 @ExtendWith 가 있는데
        (junit4 인 경우 RunWith 였음)

            @Autowired private Ex04_ArticleRepository articleRepository;
            @Autowired private Ex05_ArticleCommentRepository articleCommentRepository; */

    // 생성자 만들기. 여기선 다른 파일에서 매개변수로 보내주는거를 받는거라서 위에랑 상관 없이 @Autowired 붙여야함
    public Ex04_JpaRepositoryTest(@Autowired Ex04_ArticleRepository articleRepository, @Autowired Ex05_ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }


    /* select 테스트 */
    @DisplayName("select 테스트") // 이 메소드의 테스트 이름은 'select 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 selecting 할때 잘 동작하는 경우
    void selectTest() {
        /**
         given_when_then 이름으로 메서드 만들고 테스트 해볼거다. 이 패턴은 따로 지정되어 있는게 아니고 내가 세트를 만들거다
         Setting(Ctrl + Alt + S) 에서 live template 검색해보자.
         단축코드 만들어주는건데 대표적으로 'Java' 에 sout 을 찾아서 눌러보면 System.out ~ 이 구문을 볼 수 있다.
         우측 상단 끝에 + 버튼 > Template Group 으로 그룹 생성하고, Live Template 로 안쪽에 단축키를 설정한다.
         Abbreviation 에 단축코드 넣고 (gw 라고 넣기), Description 에 설명 써놓고 (Given - When & Then)
         Tamplate text 에는
         // Given

         // When & Then
         이라고 넣자. 그리고 맨 아래 'Applicable 어쩌구'의 change 눌러서 Java 부분 열어보면 그 안에
         comment, consumer function , Expression, Statement 체크하고 저장하자.
         그럼 앞으로 gw 라고 치면 주석을 자동완성기능으로 쓸 수 있다.

         gwt 로도 만들어보자
         // Given

         // When

         // Then 이렇게 되게.

         * */

        // Given

        // When
        /* 셀렉팅을 할거니까 articleRepository 를 기준으로 테스트 할거고 findAll() 을 사용해서 모든 컬럼을 조회할거다.
         *
         *  - 트랜잭션시 사용하는 메서드
         *   사용법: repository명.findAll(Sort.by(Sort.Direction.DESC, "기준컬럼명"));
         *       1) findAll() - 모든 컬럼을 조회할때 사용. 페이징(pageable) 가능
         *                       당연히 select 작업을 하지만, 잠깐 사이에 해당 테이블에 어떤 변화가 있었는지 알 수 없기 때문에
         *                       select 전에 먼저 최신 데이터를 잡기 위해서 update 를 한다.
         *                       순서: update -> select
         *       2) findById() - 한 건에 대한 데이터 조회시 사용
         *                       primary key로 레코드 한건 찾기
         *       3) save() - 레코드 저장할떄 사용 (insert, update)
         *       4) count() - 레코드 개수 뽑을때 사용
         *       5) delete() - 레코드 삭제
         * */
        List<Article> articles = articleRepository.findAll();

        // Then
        /* assertJ 를 이용해서 테스트를 한다.
            articles가 isNotNull 이고 사이즈가 0개 면 테스트 통과다 */
        // assertThat(articles).isNotNull().hasSize(0);
        /** 이건 테스트니까 그냥 Run 에서 돌리자(Ctrl + Shift + F10) 에서 스크롤 맨 아래에 결과가 Completed initialization  라고 나오면 된거고
         스크롤 조금만 위로 올려보면 Hibernate 가 select 문들 만들어준걸 볼 수 있다.

         이제 확인 했으니까 Test 용 데이터를 넣어볼거다.
         테스트 데이터를 대량으로 만들어볼건데 mockaroo 라는 사이트 가서 대용량 데이터를 뽑아보자
         !! 여기서부터는 word 파일 "mockaroo 사용법" 부분 보면 됨

         어쨌든 갔다오면
         1) resource 폴더에 data.sql 파일 만들고
         2) 다운받은 sql 파일 내용 복붙하기
         3) assertThat(articles).isNotNull().hasSize(0); 에서 hasSize(0) 을 hasSize(100) 으로 바꾸기
         (데이터 100개 가져올거니께)
         */
        assertThat(articles).isNotNull().hasSize(100); // 게시글은 100개
    }


    /* insert 테스트 */
    @DisplayName("insert 테스트") // 이 메소드의 테스트 이름은 'select 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 inserting 할때 잘 동작하는 경우
    void insertTest() {
        /** 기존의 개수를 센 다음에 insert 하고 기존거보다 하나 더 늘었다. 라는 시나리오로 테스트 할거임 */

        // Given
        /* 기존카운트 구하고 */
        long previousCount = articleRepository.count();

        /* Article 에 정보 넣고 */
        Article article = Article.of("new article", "new content", "#spring");

        // When - 테스트 해야 하는 내용
        /* 윗줄에서 셋팅한 article 을 insert(save) 해라 */
        Article savedArticle = articleRepository.save(article);

        // Then
        /** 여기서 할 일은 윗줄에서 save를 했으니 맨 처음 구한 previousCount 보다 1개 더 커야 한다.
         현재 카운트와 기존 카운트를 비교해서 1 차이나는지 확인해보자
         isEqualTo(숫자) => '숫자 개수의 결과가 있냐' 라는 뜻임
         */
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
        // 카운트를 새로 구했더니 기존 카운트에 1 더해진거랑 같냐? 라는 말임.

        /* !!주의: 이상태로 테스트 돌리면 createdAt 이거 못찾는다고 에러남.
         * 이유: jpaConfig 파일에 auditing 을 쓰겠다고 셋업을 해놨는데
         *      해당 엔티티(지금은 Article.java)에서도 auditing 을 쓴다고 명시해줘야 한다.
         *       Article.java 가서 클래스 맨 위에 어노테이션
         *           @EntityListeners(AuditingEntityListener.class) 이거 넣자
         *  */
    }

    /* update 테스트 */
    @DisplayName("update 테스트") // 이 메소드의 테스트 이름은 'update 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 updateing 할때 잘 동작하는 경우
    void updateTest() {
        /** 기존의 데이터가 있어야 하고
         * 그걸 수정했을때를 관찰해야 한다.
         *
         * 1) 기존의 영속성 컨텍스트로부터 하나 엔티티 객체를 가져온다.
         * 2) 업데이트로 해시태그를 가공해보자(다른것도 할수있지만 만만한게 지금은 해시태그)
         * */

        // Given
        /* 순서 - 1) 기존의 영속성 컨텍스트로부터 하나 엔티티 객체를 가져온다.
            1. 기존의 영속성 컨텍스트로부터 하나 엔티티 객체를 가져올건데 => articleRepository.findById
            2. 글번호 1번은 보통 무조건 있으니까 => findById(1L)
            3. 없으면 throw 시켜서 일단 테스트가 끝나게 하자. => .orElseThrow();
        * */
        Article article = articleRepository.findById(1L).orElseThrow();

        /* 2) 업데이트로 해시태그를 가공해보자
            순서: 엔티티에 있는 setter 를 이용해서 변수 updateHashtag 에 있는 문자열로 업데이트 해보자
                1. 변수 updateHashtag 에 문자열 #springboot 를 넣고
                2. 엔티티(article)에 있는 setter 를 이용해서 변수 updateHashtag 에 있는 문자열로 업데이트 해보자
        * */
        String updateHashtag = "#springboot"; /* 1. 변수 updateHashtag 에 문자열 #springboot 를 넣고 */
        article.setHashtag(updateHashtag); /* 2. 엔티티(article)에 있는 setter 를 이용해서 변수 updateHashtag 에 있는 문자열로 업데이트 해보자 */

        // When - 테스트 해야 하는 내용
        /**  1번 방법 - 위에 setHashtag 로 수정을 가한 article 을 articleRepository 에 save() 하자
         !! 이걸로 먼저 테스트 돌려볼것 !!
         이렇게 하면 테스트 돌렸을때 콘솔(Run 탭) 을 보면 update 구문이 아니라 select 구문만 나오고 끝난다.
         이유는 영속성 컨텍스트로부터 가져온 데이터를 그냥 save만 하고 아무것도 하지 않고 끝내버리면
         어짜피 롤백 되니까 springBoot 는 다시 원래의 값으로 돌아가질거다.
         테스트 돌리면 콘솔(Run 탭)에 스크롤 마지막 부분에 select 구문이 뜬다.
         그래서 save 를 하고 flush 를 해줘야 한다. (아래 2번 방법으로 해보자)
         *
         * flush 란 push 같은거
         *  - flush 동작과정
         *  1. 변경점 감지하고
         *  2. 수정된 Entity 를 지연 sql 저장소에 등록
         *  3. 쓰기 지연 sql 저장소의 쿼리를 DB에 전송한다. (등록, 수정, 삭제 쿼리)
         */
        // Article savedArticle = articleRepository.save(article);

        // 2번 방법 - 이거로 하고 테스트 돌리면 콘솔(Run 탭)에 스크롤 마지막 부분에 update 구문 뜬다.
        Article savedArticle = articleRepository.saveAndFlush(article);


        // Then
        /**  savedArticle이 "hashtag" 필드를 가지고 있는데, 그 필드에 updateHashtag 값이 있는지 확인해봐라
         hasFieldOrPropertyWithValue("필드명(문자열)", 값)
         * */
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashtag);

    }
    /* delete 테스트 */
    @DisplayName("delete 테스트") // 이 메소드의 테스트 이름은 'delete 테스트'
    @Test // 테스트 데이터가 주어진 상태에서 deleteing 할때 잘 동작하는 경우
    void deleteTest() {
        /** 기존의 데이터가 있어야 하고
         * 값 하나 꺼내서 지워야 한다. 아마 이부분이 좀 까다로울거임.
         *
         * 1) 기존의 영속성 컨텍스트로부터 하나 엔티티 객체를 가져온다.
         * 2) 지우면 DB에서 하나가 사라지는거니까 count 를 구해놓고
         * 3) 하나 빼서
         * 4) 2번에서 구한거랑 지금 순간의 갯수 비교해서 1 차이나면 테스트 통과한거임.
         * */

        // Given
        /* 1) 기존의 영속성 컨텍스트로부터 하나 엔티티 객체를 가져온다. */
        /*  순서 -   1. 기존의 영속성 컨텍스트로부터 하나 엔티티 객체를 가져올건데 => articleRepository.findById
            2. 글번호 1번은 보통 무조건 있으니까 => findById(1L)
            3. 없으면 throw 시켜서 일단 테스트가 끝나게 하자. => .orElseThrow();
        * */
        Article article = articleRepository.findById(1L).orElseThrow();

        /* 2) 지우면 DB에서 하나가 사라지는거니까 count 를 구해놓고 */
        /** 게시글(articleRepository) 뿐만 아니라, 연관된 댓글(articleCommentRepository) 까지 삭제할거라서 두개의 개수를 다 알아낼거다.
         * 그래서 이 클래스 이름을 ArticleRepositoryTest 이라고 안하고 그냥 독립적인 JpaRepositoryTest 라고 이름을 지은거다.
         * 나중에 Repository 전용 테스트가 필요하면 그때 만들면 된다.
         * */
        long previousArticleCount =  articleRepository.count();
        long previousArticleCommentCount =  articleCommentRepository.count(); // count() 는 레코드 개수 구하는거. findAll, findById 같은 메소드임
        int deletedCommentsSize = article.getArticleComments().size(); // 얘는 자바 collection용. list의 길이 구할때 사용
        // size()에 마우스 올려보면 리턴타입이 int 라고 되어 있음 그래서 맨 앞에 int 라고 붙임

        // When - 테스트 해야 하는 내용
        /* 게시글(article) 삭제하기 */
        articleRepository.delete(article); // delete 에 마우스 올려보면 리턴타입이 void라고 나옴. 별도로 저장할 값 없어서 저장 안함


        // Then
        /* 2번에서 구한거랑 지금 순간의 갯수 비교해서 1 차이나면 테스트 통과한거임. */

        /** 현재 게시글(articleRepository) 의 개수(count()) 가 아까구한 previousArticleCount 보다 1 적으면 테스트 통과 라는 뜻 */
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        /** 현재 게시글에 대한 댓글(articleCommentRepository) 개수(count()) 가 아까 구한 previousArticleCommentCount */
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
        // 현재 남은 댓글개수 와 (지우기 전에 개수 - 지우고 난 개수)랑 같으면 테스트 통과
        // 원 게시글에 달려있는 댓글의 개수를 알 수 없기때문에 이렇게 따로따로 구해야 함
    }
}
/* delete 테스트 통과 하면 
    1) 맨 위에 가서 전체 테스트 돌려보기
    2) 다 돌려서 Run 패털에 @DisplayName 로 걸어놓은 이름 제대로나오는지 확인하기
*/

/* 다 했으면 워드파일에 ' 테스트 이후 강의 ' 부분으로 가서 진행하기 (끝난 코드들 다 깃크라켄에 올리기)
    깃크라켄으로 올리면서 설명하기
    3) application.yaml 파일 가서 맨 아래 testdb 부분 주석처리 했던거 풀면서 설명하기 */
















