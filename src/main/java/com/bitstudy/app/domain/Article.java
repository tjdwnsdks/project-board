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

// 1) 엔티티 등록
@Getter
@ToString(callSuper = true) // 저 아래 '유저 정보 (ID)' 부분을 새로 넣었고, 상속받는 AuditingFields의 toString까지도 출력할 수 있도록 callSuper 넣음

/** @Index - 엔티티와 매핑할 테이블을 지정.데이터베이스 인덱스는 추가 쓰기 및 저장 공간을 희생 하여 테이블에 대한 데이터 검색 작업의 속도를 향상시키는 데이터 구조
 *   밑에 @Entity 와 셋트로 사용 */
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy") /** AuditingFields.java 사용시 아래 본분에 있는 createdAt 랑 createdBy 를 다 보내버리는데 Index꺼는 보내지 않는다. 못보내는건 아닌데 그러려면 세팅을 너무 많이 해줘야 해서 비효율적` */
})

// @EntityListeners(AuditingEntityListener.class) /* AuditingFields.java 로 옮겨감 */
@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    /* 이거 삭제*///@Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보 (ID)
    /* 새로 삽입 */ @Setter @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount; // 조인 컬럼은 외래 키를 매핑할 때 사용
    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문

    @Setter private String hashtag; // 해시태그


    /** 양방향 바인딩 */
    @ToString.Exclude /** lazy loaded 를 막고, circular reference(순환참조) 해결하는거 */
    @OrderBy("createdAt desc") /** 양방향 바인딩으로 게시글에 연관된 댓글리스트 뽑으려고 만든건데 우선 시간순으로 정렬되도록 이거로 바꿈 */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    protected Article() {}

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) { // 동등성 비교
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Article article = (Article) o;
//        return id!= null &&  id.equals(article.id); // 이건 혹시라도 id가 null 이 아니면 해라. 라는 뜻

        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() { // 동일성 비교
        return Objects.hash(id);
    }
}

/* 이거 다 바꾸고 ArticleComment.java 로 이동 */









