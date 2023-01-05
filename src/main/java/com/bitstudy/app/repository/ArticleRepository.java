package com.bitstudy.app.repository;


/* 워드 "검색기능 추가하기" 에서 queryDSL 부분 끝내고 시작하는 코드.*/

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>
        ,QuerydslBinderCustomizer<QArticle> // 이부분 처음에는 주석처리 하고 아래 빨간 '설명' 부분 설명하기
{

/* 새로 삽입 - 제목으로 검색할때
   게시글을 제목으로 검색할때 사용 할거임. test > ArticleServiceTest.java 에서 ("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.") 이 부분에서 SearchType.TITLE 부분에서 쓰임.*/
    Page<Article> findByTitleContaining(String title, Pageable pageable); // findByContentContaining 을 써야 or검색 한다.

/* 새로 삽입 - 내용으로 검색할때 */
    Page<Article> findByContentContaining(String content, Pageable pageable);

/* 새로 삽입 - 유저아이디로 검색할때인데.. 지금 여기는 Article 관련 데이터들만 취급하는 곳이기 때문에 원래는 UserID 를 가지고 있지 않다.
   그런데 Article.java 가보면 @ManyToOne 부분에 UserAccount 를 들고 있다.(영속성) 그래서 그 UserAccount 를 타고 들어가서 그 안에있는 userId 를 가져올 수 있다. 그래서 findByUserAccount 하고 _(언더바) 로 연결한거임.  */
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);

/* 새로 삽입 - 닉네임으로 검색할때 */
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

/* 새로 삽입 - 해시태그로 검색할때. 해시태그는 정확한 이름으로만 검색 되게 할거라서 Contining 을 쓰지 않는다. */
    Page<Article> findByHashtag(String hashtag, Pageable pageable);


    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // 이건 like '%${문자열}%'  로 들어감 // 검색어에다가 % 를 자동으로 넣어줌
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 이건 날짜니까 DatetimeExpression 으로 하고, eq 는 equals 의 의미. 날짜 필드 는 완벽히 같은것만 검색되도록 할거임. 근데 이렇게 하면 시분초 를 다 0 으로 인식하기 때문에 완벽하진 않다. 시분초 건드리는 부분은 별도로 할거다. 지금은 일단 맞는다는 가정하에 돌릴거다.
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }
    /* 여기까지 했으면 댓글쪽도 가서 완성하기 */
}
