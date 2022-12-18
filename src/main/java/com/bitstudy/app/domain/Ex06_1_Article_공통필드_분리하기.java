package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
    Article 과 ArticleComment 에 있는 공통 필드들이 있는데 이것들을 별도로 빼서 관리할거다.
    이유는 앞으로 Article 과 ArticleComment 처럼 fk 같은거로 엮여있는 테이블들을 만들경우, 모든 domain 파일들에 많은 중복 코드들이 들어가게 된다.
    예를들어 지금 같은 경우에도 id, 생성일시, 생성자, 수정일시, 수정자 들이 중복되고 있다.
    그래서 별도의 파일에 공통되는것들을 다 몰아넣고 사용하는걸 해볼거다.

    그런데 하나 알고갈거가 있는데, 공통 필드를 따로 빼는건 팀마다 다르다.
    방금말한 중복코드를 싫어하는 사람들도 있고,
    중복은 상관없이 '1엔티티 = 1데이터베이스' 로 맞추는걸 선호하는 사람들도 있다.

    중복코드를 싫어하는 사람들 입장에서는 코드가 간결해지기 때문에, 한 파일에 몰아서 해버리는걸 선호하는거고
    중복 신경쓰지 않는 사람들은, 한 파일에 모든 컬럼들이 다 나와있게 해서, 변경시 유리하게 코딩하는걸 선호하는거다.
    예를들어, 추후에 ArticleComment 에서 수정일시나 수정자를 빼고 싶으면 거기 가서 직접 코드를 없애면 된다.

    우리는 중복부분 추출해서 해볼거다.

    추출은 두가지 방법으로 할 수 있다.
    1) @Embedded - 공통되는 필드들(id, 생성일시, 생성자, 수정일시, 수정자) 을 하나의 클래스로 만들어서 @Embedded 있는 곳에서 치환하는방식
                저 밑에 createdAt 필드들 있는데 가보면 예시 있음
                ex) class Tmp {
                        여기에 생성일시, 생성자, 수정일시, 수정자 코드 그대로 다 넣고
                    }                  
 
                    @Embedded Tmp tmp; 이렇게 하면 이 코드가 있는 자리에 저 Tmp 클래스 안에 있는것들이 있는것처럼 치환됨

    2) @MappedSuperclass - 우리는 이거로 할거임 (요즘 실무에서 사용)
                            @MappedSuperclass 어노테이션이 붙은
                            1. domain > AuditingFields 파일 만들기
                            2. 생성일시, 생성자, 수정일시, 수정자 코드들 다 잘라서 AuditingFields 로 옮기기
                                 @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
                                 @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
                                 @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
                                 @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자
  * */

/* !! 아래 빨간 부분 다 하면 ArticleComment.java 에서 도 똑같이 뺄거 빼자 !! */

// 1) 엔티티 등록
@Getter
@ToString
/** @Index - 엔티티와 매핑할 테이블을 지정.데이터베이스 인덱스는 추가 쓰기 및 저장 공간을 희생 하여 테이블에 대한 데이터 검색 작업의 속도를 향상시키는 데이터 구조
*   밑에 @Entity 와 셋트로 사용 */
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy") /* AuditingFields.java 사용시 아래 본분에 있는 createdAt 랑 createdBy 를 다 보내버리는데 Index꺼는 보내지 않는다. 못보내는건 아닌데 그러려면 세팅을 너무 많이 해줘야 해서 비효율적` */
})

// @EntityListeners(AuditingEntityListener.class) /* AuditingFields.java 로 옮겨감 */
@Entity
public class Ex06_1_Article_공통필드_분리하기 extends AuditingFields { /* AuditingFields 파일 다 만들면 Article 에서는 그걸 상속받아서 연결하면 됨. 이렇게 하면 4개의 필드(속성)가 Article 에 연결이 됨. 아까 Embedded 와 비슷한 방식으로 필드 4개가 추가가 된거다.

  (!! 안해도 됨!! )둘의 차이: 별반 다르진 않지만 @Embedded 방식으로 하게 되면 필드 하나가 추가되는 거고, 우리가 할건 아니지만 영속성 컨텍스를 통해서 데이터를 넘겨 받아서 애플리케이션에서 열었을 때에는 어짜피 AuditingFields 로 보인다. 중간에 한단계가 더 생긴다는 뜻.
  @MappedSuperclass 는 표준 JPA 에서 제공해주는 클래스이고 중간단계 없이 바로 감.  */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000) private String content;
    @Setter private String hashtag; // 해시태그

    /** 양방향 바인딩 */
    @OrderBy("id") // 양방향 바인딩을 할건데 정렬 기준은 id로 하겠다 라는 뜻
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude /** lazy loaded 를 막고, circular reference(순환참조) 해결하는거 */

    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    /* AuditingFields.java 로 옮겨감 */
//    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
//    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
//    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
//    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

/* - Embedded 방식 */
//    class Tmp {
//        @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
//        @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
//        @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
//        @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자
//    }
//    @Embedded Tmp tmp; //이렇게 하면 이 코드가 있는 자리에 저 Tmp 클래스 안에 있는것들이 있는것처럼 치환됨

    protected Ex06_1_Article_공통필드_분리하기() {}
    private Ex06_1_Article_공통필드_분리하기(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
    public static Ex06_1_Article_공통필드_분리하기 of(String title, String content, String hashtag) {
        return new Ex06_1_Article_공통필드_분리하기(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) { // 동등성 비교
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ex06_1_Article_공통필드_분리하기 article = (Ex06_1_Article_공통필드_분리하기) o;
        return id!= null &&  id.equals(article.id); // 이건 혹시라도 id가 null 이 아니면 해라. 라는 뜻
    }

    @Override
    public int hashCode() { // 동일성 비교
        return Objects.hash(id);
    }
}











