package com.bitstudy.app.dto;

import com.bitstudy.app.domain.Article;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/* !! 얘를 만드는 이유 !!
*
*  ArticleDto.java 는 실제 Article 엔티티의 내용을 담고 있다. (ArticleDto 에 보면 userAccountDto 는 꼭 필요해서 넣었음)
*  그런데 우리가 게시판 페이지 관련 도메인들 만들면서 양방향 바인딩 하려고, @OneToMany 나 @ManyToOne 같은 작업들을 해놨었고,
*  실제로 게시글 들어가게(불러오게) 되면 자연스럽게 그 게시글의 댓글들까지 불러오게 만들었었다.
*  그런데 우리가 만들어놓은 ArticleDto 에는 그런 기능을 사용할수가 없다. 할수는 있는데 게시글과 댓글을 모두 담을수 있는 dto가 필요할때가 있을거고, 아니면 게시글만 담을 dto 가 필요할때가 있을거라서 ArticleDto 와 ArticleWithCommentsDto 로 구분해서 쓸거다.
*
* 예전 maven 때 User 관련된거 했을때에는 회원가입과 로그인을 하나의 dto 로 썼었는데, 사실 그렇게 하는것보다 지금처럼 필요에 따라 분리해서 만드는걸 추천한다. 이유는 만약 ArticleDto 가 다 감당하게 만들어놓고 게시글만 담을경우 댓글필드들은 다 null 이 될건데. 그럼 데이터의 유효성이 깨진거다.
* dto 데이터의 필드가 nullable 한 설계를 했다는건, 이 데이터를 참조하는 모든 서비스 로직에서 null 인 경우를 대비한 별도의 판단로직이란걸 만들어야 하는데 그럼 일이 더 커지기도 하고, 객체지향 설계원칙(solid)에서 권고하는 설계원칙중 OCP(개방 폐쇄 원칙) 설계 원칙을 해치게 되기 때문이다.

   그래서 두개로 분리했다.

*
* = solid 원칙 =
    SPR(Single Responsibility Principle) : 단일 책임 원칙 - 어떤 클래스를 변경해야 하는 이유는 오직 하나 뿐이어야 한다.
    OCP(Open Closed Principle) : 개방 폐쇄 원칙 - 엔티티는 확장에 대해서는 열려 있어야 하지만 변경에 대해서는 닫혀 있어야 한다.
    LSP(Liskov Substitution Principle) : 리스코프 치환 원칙 - 하위 클래스의 인스턴스는 상위형 객체 참조 변수에 대입해 상위 클래스의 인스턴스 역할을 수행하는 데 문제가 없어야 한다
    ISP(Interface Segregation Principle) : 인터페이스 분리 원칙 - 클라이언트는 자신이 사용하지 않는 메소드에 의존 관계를 맺으면 안된다.
    DIP(Dependency Inversion Principle) : 의존 역전 원칙 - 고차원 모듈은 저차원 모듈에 의존하면 안된다.
* */


public record ArticleWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<ArticleCommentDto> articleCommentDtos, /* 이거만 추가되고 나머지는 ArticleDto 랑 같음 */
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleWithCommentsDto of(Long id, UserAccountDto userAccountDto, Set<ArticleCommentDto> articleCommentDtos, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleWithCommentsDto(id, userAccountDto, articleCommentDtos, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleWithCommentsDto from(Article entity) {
        return new ArticleWithCommentsDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getArticleComments().stream() // getArticleComments : entity(아티클) 에서 Comment 들을 불러오고
                        .map(ArticleCommentDto::from) // map() : Dto 로 다 변환 해서
                        .collect(Collectors.toCollection(LinkedHashSet::new)), // LinkedHashSet: 똑같은 순서로 넣어주는거(collect)
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

}