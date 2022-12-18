package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

/**  공통되는 필드(속성)들 빼기 -
 *  Ex06_Article_공통필드_분리하기 처럼
 * 1) 맨 아래쪽에 있는 4개 필드 빼고 (createdAt , createdBy, modifiedAt, modifiedBy)
 * 2) @EntityListeners 빼고
 * 3) AuditingFields 상속받기
 * 
 * 4) 다 되면 TDD >  Ex04_JpaRepositoryTest 파일 다시 돌려보기
* */

// 1) 엔티티 등록
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})

/* 2) @EntityListeners 빼고 */
// @EntityListeners(AuditingEntityListener.class) /** 이건 TDD Ex04 때 할거임. 지금 하지 말것 */
@Entity /** 테이블과의 매핑. @Entity가 붙은 클래스는 JPA가 관리하게 되며, 엔티티 라고 불린다.
            @Entity 를 쓰게되면 PK 를 만들어줘야 한다. */
/* 3) AuditingFields 상속받기 */
public class Ex06_3_ArticleComment_공통필드_분리하기 extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article;
    @Setter @Column(nullable = false, length = 500) private String content; // 본문

    /* 1) 맨 아래쪽에 있는 4개 필드 빼고 (createdAt , createdBy, modifiedAt, modifiedBy) */
    /* 여기 4개 필드 다 빼기 - 이미 Ex06_2_AuditingFiles 에 다 뺴놨음 */
//    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
//    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
//    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
//    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자



    public Ex06_3_ArticleComment_공통필드_분리하기() {}

    // 내가 필요한 본문 관련 정보만 가진 생성자 만들자 (여기서는 사용자가 입력하는 값)
    private Ex06_3_ArticleComment_공통필드_분리하기(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public Ex06_3_ArticleComment_공통필드_분리하기 of(Article article, String content) {
        return new Ex06_3_ArticleComment_공통필드_분리하기(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ex06_3_ArticleComment_공통필드_분리하기 that = (Ex06_3_ArticleComment_공통필드_분리하기) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /*   */
}





















