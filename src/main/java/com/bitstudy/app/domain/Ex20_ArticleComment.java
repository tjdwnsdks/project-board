package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

// 1) 엔티티 등록
@Getter
/* 이거 없앱 */ // @ToString
/* 새로 삽입 */ @ToString(callSuper = true) // 저 아래 '유저 정보' 부분을 새로 넣었고, 상속받는 AuditingFields의 toString까지도 출력할 수 있도록 callSuper 넣음
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})

/** 2) @EntityListeners 빼고 */
// @EntityListeners(AuditingEntityListener.class) /** 이건 TDD Ex04 때 할거임. 지금 하지 말것 */
//@Entity /** 테이블과의 매핑. @Entity가 붙은 클래스는 JPA가 관리하게 되며, 엔티티 라고 불린다.
           // @Entity 를 쓰게되면 PK 를 만들어줘야 한다. */
/** 3) AuditingFields 상속받기 */
public class Ex20_ArticleComment extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article; // 게시글 (ID)
/* 새로 삽입 */ @Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보 (ID)
    @Setter @Column(nullable = false, length = 500) private String content; // 본문



    protected Ex20_ArticleComment() {}

    /* 이거 없앰 */
//    private ArticleComment(Article article, String content) { // 내가 필요한 본문 관련 정보만 가진 생성자 만들자 (여기서는 사용자가 입력하는 값)
//        this.article = article;
//        this.content = content;
//    }
    /* 새로 삽입 */
    private Ex20_ArticleComment(Article article, UserAccount userAccount, String content) { // 내가 필요한 본문 관련 정보만 가진 생성자 만들자 (여기서는 사용자가 입력하는 값)
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    /* 새로 삽입 */
    /* 위에 private Article 부분 바꼈으니까 여기도 바꿔야함*/
    public static Ex20_ArticleComment of(Article article, UserAccount userAccount, String content) {
        return new Ex20_ArticleComment(article, userAccount, content);
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ArticleComment that = (ArticleComment) o;
//        return id.equals(that.id);

        if (this == o) return true;
        if (!(o instanceof Ex20_ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* 이거 다 바꾸고 data.sql 로 이동
    *  jpaRepositoryTest.java 로 이동
    * */
}





















