package com.bitstudy.app.domain;

import com.bitstudy.app.config.JpaConfig;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 *  이제 gradle 방식으로 코드를 짜볼건데, JPA 나 Lombok 같은 요즘 핫한 디펜던시들을 이용해서 코드를 짜볼거다.
 *  maven 때랑 같은 방식인 것들도 이름이 다르게 되어 있는 것들도 있으니 헷갈리지 않게 주의하자.
 *  그리고 애너테이션은 너무 많기 때문에 수업하면서 그때그때 필요한 애너테이션을 정리해놓는걸 추천한다.


 *  JPA 에너테이션을 이용해서 이 클래스를 엔티티로 바꿔볼거다.
 *  * JPA 란 자바 진영의 ORM(Object Relational Mapping) 기술 표준이다.
 *      Entity 분석하고, create나 insert 같은 Sql 쿼리를 생성해주고, JDBC API를 사용해서 DB 접근도 해주고, 객체와 테이블을 매핑해준다.
 *      참고: https://ultrakain.gitbooks.io/jpa/content/chapter1/chapter1.3.html
 *  * 엔티티란 - 별거 없다. DB테이블에 대응하는 하나의 클래스.
 *
 *  - 순서 -
 * 1) 엔티티 등록 - 롬복을 이용해서 @Getter, @ToString, @Table, @Entity 을 사용할거다
 * 2) 동등성, 동일성 비교할 수 있는 코드도 넣어볼거임 (equals, hashcode)
 * */

// 1) 엔티티 등록
@Getter
@ToString

/* @Index - 엔티티와 매핑할 테이블을 지정.데이터베이스 인덱스는 추가 쓰기 및 저장 공간을 희생 하여 테이블에 대한 데이터 검색 작업의 속도를 향상시키는 데이터 구조
*          밑에 @Entity 와 셋트로 사용 */
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})

@EntityListeners(AuditingEntityListener.class) /* 이건 TDD Ex04 때 할거임. 지금 하지 말것. */
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000) private String content;
    @Setter private String hashtag; // 해시태그

    /*  양방향 바인딩 */
    @OrderBy("id") // 양방향 바인딩을 할건데 정렬 기준은 id로 하겠다 라는 뜻
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude /** 이건 중요. 맨 위에 @ToString 이 있는데 마우스 올려보면 '@ToString includes lazy loaded 어쩌고' 가 있다.
                      이건 퍼포먼스나 메모리 저하를 일으킬수 있어서 성능적으로 안좋은 영향을 주니까 해당 필드를 가려주세요 라는 뜻.
                       위에 @ToString 가서 두번째줄의 'Exclude all 어쩌고 클릭하면 이거 생긴다. */
    /* 이건 더 중요: 이걸 안해주면 circular reference(순환참조) 이슈가 생길수 있다.
    * 여기서 ToString이 모든 원소들(id, title, content, hashtag 등)을 다 찍고 (요 밑에 있는) Set<ArticleComment> 찍으려고 하는데
    * 그럼 ArticleComment.java 파일 가서도 거기 있는 @ToString이 원소들 다 찍으려고 하면서 원소들 중에 Article 이라는 걸 보는 순간
    * 다시 Article의 @ToString이 동작하면서 모든 원소들을 또 찍고, 그 와중에 방금 말한것처럼 또 Set<ArticleComment> 찾아가고 이런식으로 돌고 돈다.
    * 그렇게 되면 메모리가 터져서 시스템이 중단될수 있다. 그래서 이런경우 @ToString을 끊어줘야 하는데 보통은 Article 같은 부분에서 끊어준다.
    * 그게 @ToString.Exclude 이다. 왜냐면 댓글로부터 글을 참조하는 경우는 정상적인 경우인데, 글로 댓글을 참조하는건 일반적인 경우는 아니기 떄문
    * (이 댓글이 어느 글 꺼냐? 는 말이 되지만 이 글이 어느 댓글꺼냐 라는건 그 글에 댓글이 많을텐데 특정 댓글만 잡아서 글과 연결 시키면 나머지 댓글들은??)
    * */

    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();
    // 이 아티클에 연동되어 있는 커멘트는 중복을 허용하지 않고 다 여기서 컬렉션으로 보겠다 는뜻
    // 게시글 하나:댓글여러개 이기 떄문에 @OneToMany 하용함
    // (mappedBy = "article" ) 아티클 테이블로부터 온거다 라는 뜻
    // cascade = CascadeType.ALL 는 모든 경우에 대해서 양방향을 하겠다 라는 뜻
    // cascade로 양방향바인딩의
    // 실무에서는 사실 cascade 를 쓰지 않는 경우도 종종 있다. 이유는 cascading에 의해서 서로가 강하게 연결되어 있으면 데이터를 편집할때 불편함이 많다.
    // 예를들어 댓글이 삭제되면 그 댓글의 리뷰들도 삭제 되어야 하는게 맞는데. 운영상으로 보면 그 댓글 기록들은 백업 목적으로 남길수도 있다.
    // 그런데 양방향으로 걸면 글을 지우면 댓글도 같이 삭제된다. 그래서 이런건 회사 내규나 팀 내규에 따라서 달라진다.

    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

    /* 이건 에러나는데 만든적이 없다. 걍 지우자*/
//    public void setArticleComments(Set<ArticleComment> articleComments) {
//        this.articleComments = articleComments;
//    }

    /* 이거 하고 다시 Service탭 가서 다시 실행해보자
    * 맨 아래 'Completed initialization' 생기면 성공한거임
    * 여태까지는 JPA 기능으로 엔티티를 정의한거고, 이걸 토대로 Repository 코드를 작성할거다. 
    *--------------------------------------------------------------------------------------
    * - 리포지토리 만들기 (JPA 기능을 이용해서 자동 생성 해보기)
    * 1) com.bitstudy.app 에서 우클릭
    * 2) New > 중간쯤에 Spring Data Repository 선택
    * 3) 창 나오면 맨 위에 'Entity: ' 에 Article 선택하고
    *            맨 아래 'Package: ' 에 com.bitstudy.app.repository' 라고 넣기
    *           (.repository 에서 빨간줄 생겨도 괜찮음. 해당 패키지가 없어서 그럼)
    * */

    protected Article() {}
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) { // 동등성 비교
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id!= null &&  id.equals(article.id); // 이건 혹시라도 id가 null 이 아니면 해라. 라는 뜻
    }

    @Override
    public int hashCode() { // 동일성 비교
        return Objects.hash(id);
    }
}











