package com.bitstudy.app.dto;

import com.bitstudy.app.domain.Article;

import java.io.Serializable;
import java.time.LocalDateTime;

/** https://velog.io/@power0080/java%EC%9E%90%EB%B0%94-record%EB%A5%BC-entity%EB%A1%9C

 - 레코드란?
    자바 16버전에서 새로 나온거.
 DTO 와 비슷하지만 DTO 를 구현하려면 getter, setter, equals, hashCode, toString 같은 데이터 처리 혹은 특정 연산을 수행하기 위해 오버라이드된 메소드를 반복해서 작성해야 한다. 이런것들을 보일러 플레이트 코드(여러곳에서 재사용되는 반복적으로 비슷한 형태를 가지고 있는 코드) 라고 하는데, 롬복을 이용해서 이런 중복으로 발생하는 코드의 양을 줄일수 있다. 그러나 근본적인 한계를 해결하지 못한다.
 그래서 특정 데이터와 관련 있는 필드들만 묶어 놓은 자료 구조로 record 라는게 생겼다.
 *주의: record 는 entity 로 쓸수는 없다. DTO 로만 사용 가능하다.
        이유는 쿼리 결과를 매핑할 때 객체를 인스턴스화 할 수 있도록 매개변수가 없는 생성자가 필요하지만, record는 매개변수가 없는 생성자(기본생성자)를 제공하지 않는다. 또한 setter를 사용할 수 없다. (이로 인해 모든 필드의 값을 입력한 후에 생성할 수 있다.)

 - 사용법
    레코드명(헤더), {바디} 의 구조를 가지는데 헤더에 나열되는 필드를 컴포넌트라고 부른다.
    레코드는 접근자와 생성자, toString, equals, hashCode를 선언하지 않아도 이에 대한 구현을 자동으로 제공한다.
    단, getter를 사용할 때 getName() 으로 쓰는 게 아니라 그냥 대놓고 필드명을 사용하면 된다.


 * */


public record ArticleDto( /* 우선 엔티티가 가지고 있는 모든 정보들을 dto 도 가지고 있게 해서, 나중에 응답할때 어떤걸 보내줄지 선택해서 가공하게 할거임 */
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {


    public static ArticleDto of(Long id,
                                UserAccountDto userAccountDto,
                                String title,
                                String content,
                                String hashtag,
                                LocalDateTime createdAt,
                                String createdBy,
                                LocalDateTime modifiedAt,
                                String modifiedBy) {
        return new ArticleDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    
    /* entity 를 매개변수로 입력하면 ArticleDto 로 변환해주는 메서드.
    * entity 를 받아서 new 한다음에 인스턴스에다가 entity. 해가면서 맵핑시켜서 return 하고 있는거.
    * 맵퍼처럼 사용할 수 있게 만든거. */
    public static ArticleDto from(Article entity) {
        return new ArticleDto( // 이게 저 위에 record ArticleDto 부르는거
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    /* 위에꺼랑 반대로, dto 가 있다면 dto 로부터 엔티티를 생성하는 메서드.
    * 이걸 쓰면 좋은점은 이렇게 되면 Article.java 는 DTO의 존재를 몰라도 되게 된다.
    * 실제로 Article.java 가서 맨 위에 import 부분 보면 Dto 에 관련된게 없다. 딱 domain에 관련된 코드만 가지고 있다.
    * 이 파일에서만 연관관계 맵핑을 하기 위해서 Article.java 의 존재를 알고 있다.
    * (맨 위에 import 에 Article 관련 있음)
    * 그래서 domain 안에 있는 Article 이 바뀌면 이 ArticleDto 는 영향을 받겠지만, 반대의 경우에는 도메인코드(Article) 는 영향을 받지 않는다. 왜 이렇게 짜냐면 domain 에 있는 코드들은 다 DB에 영향을 주는 코드들이라서 다른 애들한테 영향을 안받게 짜주는게 좋다. */
    public Article toEntity() {
        return Article.of(
                userAccountDto.toEntity(),
                title,
                content,
                hashtag
        );
    }

    /* 이것과 비슷한 전략으로 dto > response > ArticleCommentRespose 같은거다. */
}
