package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

// 1) 엔티티 등록
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity /* 테이블과의 매핑. @Entity가 붙은 클래스는 JPA가 관리하게 되며, 엔티티 라고 불린다.
            @Entity 를 쓰게되면 PK 를 만들어줘야 한다. */
public class Ex02_ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article;// 내용. 연관관계 매핑임.
            // 연관관계를 주지 않는다고 하면 그냥
            // private Long articleId 이런식으로 관계형데이터베이스 스타일로 하면 된다.
            // 그런데 우리는 Article 과 관계를 맺고 있는걸 객체지향적으로 표현하려고 이렇게 사용해볼거다.
            // 그러기 위해 필요한건 단방향(N:1) 이라는 뜻의 @ManyToOne 애너테이션이고, 이건 필수값이다 라는 뜻으로 (optional = false)
            // 댓글을 여러개:게시글은 1개 이기 떄문에 단방향을 썼던거고
            // 저쪽(Article) 에서는 양방향 바인딩을 해줘야 한다.
    @Setter @Column(nullable = false, length = 500) private String content; // 본문

    // 메타데이터 - Article꺼랑 어짜피 똑같으니까 걍 복사해오자
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자


//    그리고 Article 때처럼 똑같이 constructor 만들자


    public Ex02_ArticleComment() {}

    // 내가 필요한 본문 관련 정보만 가진 생성자 만들자 (여기서는 사용자가 입력하는 값)
    private Ex02_ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public Ex02_ArticleComment of(Article article, String content) {
        return new Ex02_ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ex02_ArticleComment that = (Ex02_ArticleComment) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /* 여기까지 했으면 이제 어플리케이션 구동시켜보자
    * projectBoardApplication.java 파일에서 실행할수 있는데 그럼 Run 탭에서 동작한다(우리가 여태까지 했듯이)
    * 그런데 그걸 Service 탭에서도 실행시킬수 있다.
    * Service 탭 열어보면 + 버튼에 Add Service (Alt+Insert) 있는데 > Run Configuration type > Spring Boot 선택하면 안에 projectBoardApplication 파일이 있는걸 볼 수 있다. 그거 더블클릭하면 해당 파일이 열린다. 그럼 연결 잘 된거임.
    * 이렇게 하면 Run 탭에서 빌드작업을 하거나 테스트를 하거나 그런 서비스 실행 로그와 분리해서 볼 수 있다는 이득이 있고,
    * 우리는 단일모듈로 수업을 진행하지만, 멀티모듈 프로젝트로 구성해서 이 인텔리제이 윈도우 안에서 여러개의 부트 프로젝트를 관리할때 그때는 모든 부트 프로젝트를 Service 에서 관리할 수 있는 장점이 있다.
    * Service탭에 초록 화살표 버튼 누르면 실행된다.
    *
    * 실행 다 되면 콘솔(로그)창 확대시켜서 열어보자. 그럼 각종 SQL 문들 다 만들어져 있다.
    *
    * 이 다음 Database탭(우측 벽면)을 열어보면 테이블들이 만들어져 있는걸 볼 수 있다.
    *
    * */
}





















