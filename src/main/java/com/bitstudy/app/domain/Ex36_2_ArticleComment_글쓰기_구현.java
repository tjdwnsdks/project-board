package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

// 1) 엔티티 등록
@Getter
@ToString(callSuper = true) // 저 아래 '유저 정보' 부분을 새로 넣었고, 상속받는 AuditingFields의 toString까지도 출력할 수 있도록 callSuper 넣음
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Ex36_2_ArticleComment_글쓰기_구현 extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article; // 게시글 (ID)

    /* 이거 삭제 *///@Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보 (ID)
    /* 새로 삽입 */@Setter @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount; // 유저 정보 (ID)
    @Setter @Column(nullable = false, length = 500) private String content; // 본문



    protected Ex36_2_ArticleComment_글쓰기_구현() {}

    private Ex36_2_ArticleComment_글쓰기_구현(Article article, UserAccount userAccount, String content) { // 내가 필요한 본문 관련 정보만 가진 생성자 만들자 (여기서는 사용자가 입력하는 값)
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static Ex36_2_ArticleComment_글쓰기_구현 of(Article article, UserAccount userAccount, String content) {
        return new Ex36_2_ArticleComment_글쓰기_구현(article, userAccount, content);
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ArticleComment that = (ArticleComment) o;
//        return id.equals(that.id);

        if (this == o) return true;
        if (!(o instanceof Ex36_2_ArticleComment_글쓰기_구현 that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* 이거 다 바꾸고 data.sql 로 이동
     * */
}





















