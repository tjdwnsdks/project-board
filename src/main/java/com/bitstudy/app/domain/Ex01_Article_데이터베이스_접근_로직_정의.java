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

/**
 *  이제 gradle 방식으로 코드를 짜볼건데, JPA 나 Lombok 같은 요즘 핫한 디펜던시들을 이용해서 코드를 짜볼거다.
 *  maven 때랑 같은 방식인 것들도 이름이 다르게 되어 있는 것들도 있으니 헷갈리지 않게 주의하자.
 *  그리고 애너테이션은 너무 많기 때문에 수업하면서 애너테이션에 대해서 일일이 설명할수가 없다.
 *  간단히 왜 쓰는지는 설명하겠지만 자세한건 복습하시면서 찾아보는것으로 하자.
 *  가능하면 그날그날 필요한 애너테이션을 정리해놓는걸 추천한다.


 *  JPA 에너테이션을 이용해서 이 클래스를 엔티티로 바꿔볼거다.
 *  * JPA 란 자바 진영의 ORM(Object Relational Mapping) 기술 표준이다.
 *      Entity 분석하고, create나 insert 같은 Sql 쿼리를 생성해주고, JDBC API를 사용해서 DB 접근도 해주고, 객체와 테이블을 매핑해준다.
 *      참고: https://ultrakain.gitbooks.io/jpa/content/chapter1/chapter1.3.html
 *  * 엔티티란 - 별거 없다. DB테이블에 대응하는 하나의 클래스.
 *
 *  - 순서 -
 * 1) 롬복을 이용해서 @Getter, @ToString, @Table, @Entity 을 사용할거다
 * 2) 매핑에 대해서 간단히 볼거임
 * 3) 동등성, 동일성 비교할 수 있는 코드도 넣어볼거임 (equals, hashcode)
 * */

@Getter
@ToString

/* document 폴더의 project-board-erd.svg 보면서 하자. */
/* @Table - 엔티티와 매핑할 테이블을 지정하고, 생략 시 매핑한 엔티티 이름을 테이블 이름으로 사용.
* ex) @Index(name = "원하는명칭", columnList = "사용할 테이블명")
* 
* @Index - 데이터베이스 인덱스는 추가 쓰기 및 저장 공간을 희생 하여 테이블에 대한 데이터 검색 작업의 속도를 향상시키는 데이터 구조
*          밑에 @Entity 와 셋트로 사용
* */
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity /* 테이블과의 매핑. @Entity가 붙은 클래스는 JPA가 관리하게 되며, 엔티티 라고 불린다.
            @Entity 를 쓰게되면 PK 를 만들어줘야 한다. */
public class Ex01_Article_데이터베이스_접근_로직_정의 {

    /* private Long id 의 경우 auto-increment 처럼 데이터베이스에 값을 저장하고 나서야 기본 키 값을 구할 수 있을 때 사용한다. 기본키 전략 이라고 한다.  */
    @Id /* 데이터베이스 테이블의 기본 키(PK)와 객체의 필드를 매핑시켜주는 어노테이션
            @Id만 사용할 경우 기본 키를 직접 할당해 주어야 합니다.
            기본 키를 직접 할당하는 대신 데이터베이스가 생성해주는 값을 사용하려면 @GeneratedValue를 사용해주면 됩니다.
            참고: https://ttl-blog.tistory.com/123 */
    @GeneratedValue(strategy = GenerationType.IDENTITY) /* 자동으로 auto_increment 를 이용하기 위해서 @GeneratedValue 사용.
                                                          mysql 의 auto-increment 는 IDENTITY 방식으로 만들어지기 때문에 GenerationType.IDENTITY 사용 */

    private Long id;

    /* @Setter 도 @Getter 처럼 클래스 단위로 걸수 있는데, 그렇게 하면 모든 필드에 접근이 가능해진다.
    * 그런데 id 는 내가 부여하는게 아니라 jpa 에서 자동으로 부여해주는 번호이다. 저 밑에 있는 메타데이터들도 자동으로 JPA 가 셋팅하게 만들어야 한다.
    * 왜냐면 사용자가 입력하는 값은 '제목, 본문, 해시태그' 만 하고, 나머지(생성일, 글쓴이 등)는 시스템에서 해줘야 하기 때문이다.
    * 그래서 여기서는 각 필드에 @Setter 를 따로 주자. 회사가면 이런건 각 팀의 전략에 따라서 달라진다. 이건 그냥 내 스타일임. */
    @Setter @Column(nullable = false) private String title; /** 해당 컬럼이 not null 인경우 @Column(nullable = false) 써준다.
                                                             기본값은 true 라서 @Culumn을 아예 안쓰면 알아서 true 로 적용된다.
                                                               참고: https://velog.io/@gillog/JPA-Column-Annotation */
    @Setter @Column(nullable = false, length = 10000) private String content; /** length 는 Column 속성 길이 설정. 기본값 255 */
    @Setter private String hashtag; // 해시태그

    /* jpa auditing : jpa 에서 자동으로 셋팅하게 해줄때 사용하는 기능
    * 이거 하려면 config 파일이 별도로 있어야 한다.
    * config 패키지 만들고 JpaConfig 클래스 만들자
    * */
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

/* 이렇게 애너테이션을 붙여주기만 해도 auditing 이 작동한다.
 @CreatedDate: 최초에 insert 할때 자동으로 한번 넣어준다.
 @CreatedBy: 최초에 insert 할때 자동으로 한번 넣어준다.
 @LastModifiedDate: 작성 당시의 시간을 실시간으로 넣어준다.
 @LastModifiedBy: 작성자의 이름을 실시간으로 넣어준다. */

    /** 그런데 위에 4개중에 다른 생성일시 같은건 알아낼 수 있는데 최초 생성자는 어디서 인증받고 온것도 아니라서 따로 알아낼수가 없다.
     이때 아까 만든 config 패키지 > JpaConfig 파일을 사용한다.

     config 파일 설정 하고 오면 'createdBy' 나 'modifiedBy' 는 다 bitstudy 로 들어가게 될거다.
     */

    protected Ex01_Article_데이터베이스_접근_로직_정의() {} /* 이 클래스(Article) 는 Entity 로 사용되고 있기 때문에 무조건 기본 생성자가 필요하다.
        public 또는 protected 만 가능한데, 평생 아무데서나 안쓰게 하고 싶어서 protected 로 변경함 */


    /* 전부 다 생성자로 만들지 말고, 사용자가 입력하는 값만 받자. 나머지는 시스템이 알아서 하게 해줘야 한다. */
    private Ex01_Article_데이터베이스_접근_로직_정의(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // fatory method pattern 으로 만들자. '정적 팩토리 메서드' 라고 한다.
    //      참고: https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/
    // 정적 팩토리 메서드란 객체 생성의 역할을 하는 클래스 메서드 라는 뜻인데 of 메서드를 이용해 직접적으로 생성자를 사용해서 객체를 생성하거나 값을 전달하는것이 아닌 메서드를 통해서 객체를 생성하게 하는 방법이다. 이걸 정적 팩토리 메서드 라고 한다.
    // static 으로 놔야 한다.
    // 장점으로는 1) static 이기 때문에 new 를 이용해서 생성자를 만들지 않아도 된다.
    //          2) return 을 가지고 있기 때문에 상속을 사용할때 값을 확인할 수 있따.(한마디로 하위 자료형 객체를 반환할 수 있다.)
    //  **3)(중요) 객체 생성을 캡슐화 할 수 있다.
    // 도메인 Article 을 생성하고자 할때 어떤 값을 필요로 한다는걸 매개변수로 가이드 하는거임.
    public static Ex01_Article_데이터베이스_접근_로직_정의 of(String title, String content, String hashtag) {
        return new Ex01_Article_데이터베이스_접근_로직_정의(title, content, hashtag);
    }



    /* 주의: 여기서부터는 어려운 개념임. 잘 따라올것!!
    * 만약에 우리가 이 클래스를 이용해서 게시글들을 list 에 담아서 화면을 구성한다던가 하려면 Collection 을 이용해야 할거다.
    *   Collection : 자바가 제공하는 최상위 컬렉션(인터페이스임).
                     하이버네이트는 중복을 허용하고 순서를 보장하지 않는다고 가정
                     * 하이버네이트는 자바 언어를 위한 ORM 프레임워크. 맵핑 프레임워크임.
        Set : 중복을 허용하지 않는 컬렉션. 순서를 보장하지 않음
        List : 순서가 있는 컬렉션. 순서를 보장하고 중복을 허용
        Map : Key, Value 구조로 되어 있는 특수한 컬렉션
    * list 에 넣거나 혹은 list 에 있는 중복요소를 제거하거나 정렬을 해야할때 비교를 할 수 있어야 하기 때문에
    * 동일성 동등성 비교를 할 수 있는 equals 랑 hashcode 를 구현해야 한다.
    * 이걸 안하면 위에 명시한 모든 속성들을 다 검사하는데, 우리는 따로 커스텀 할 수 있다.
    * */


    /* equals and hashcode 넣을때 "java.util" 선택하는 창 나오는데 맨 아래꺼 (java.util.Objects.equels and hashCode (java 7+) 선택하면 됨
    * 동일성 검사를 위해서 모든 필드를 검사할 필요는 없다.
    * DB와 연동된 엔티티가 두개가 있는데 두개가 모두 동일하다 라는걸 어떻게 할수 있을까?
    * 모든 데이터들을 비교해도 되지만, 다 불러와서 비교하면 느려질수 있다.
    * 사실 그냥 id 만 같으면 두 엔티티가 같다는 뜻이니까 id만 가지고 구현하자
    * 체크박스 여러번 나올건데 id만 다 체크해서 만들자 */
    @Override
    public boolean equals(Object o) { // 동등성 비교
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ex01_Article_데이터베이스_접근_로직_정의 article = (Ex01_Article_데이터베이스_접근_로직_정의) o;
//        return id.equals(article.id); // 원래는 이거임
        return id!= null &&  id.equals(article.id); // 이건 혹시라도 id가 null 이 아니면 해라. 라는 뜻

//        자바 17 버전이면 아래 코드 가능
//        if (this == o) return true;
//        if (!(o instanceof Article article)) return false;
//        return id != null && id.equals(article.id);

    }

    @Override
    public int hashCode() { // 동일성 비교
        return Objects.hash(id);
    }

/* equals - 값이 같으면 걍 true (주소값이 어떻고 저쪄고 상관 없음!!)
            둘 다 null 이어도 true 나옴
   hashCode - 객체를 식별하는 Integer 값입니다.
              객체가 갖고 있는 데이터를 어떤 알고리즘에 적용하여 계산된 정수 값을 hashcode라고 할 수 있습니다.

              Hashcode를 사용하는 이유 중에 하나는, 객체를 비교할 때 드는 비용을 낮추기 위해서 입니다.
              자바에서 2개의 객체가 같은지 비교할 때 equals()를 사용하는데,
              여러 객체를 비교할 때 equals()를 사용하면 Integer를 비교하는 것에 비해 많은 시간이 소요됩니다
*/

}


/* 끝났으면 ArticleComment 로 이동 */











