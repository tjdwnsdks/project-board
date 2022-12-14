package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
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
public class ArticleComment extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article; // 게시글 (ID)

    /* 이거 삭제 *///@Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보 (ID)
    /* 새로 삽입 */@Setter @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount; // 유저 정보 (ID)
    @Setter @Column(nullable = false, length = 500) private String content; // 본문



    protected ArticleComment() {}

    private ArticleComment(Article article, UserAccount userAccount, String content) { // 내가 필요한 본문 관련 정보만 가진 생성자 만들자 (여기서는 사용자가 입력하는 값)
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content) {
        return new ArticleComment(article, userAccount, content);
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ArticleComment that = (ArticleComment) o;
//        return id.equals(that.id);

        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* 이거 다 바꾸고 data.sql 로 이동
     * */
}





















