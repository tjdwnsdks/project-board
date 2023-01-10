package com.bitstudy.app.dto;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.UserAccount;

import java.time.LocalDateTime;

/**

 * */


public record Ex35_2_ArticleDto(
        Long id,
        UserAccountDto userAccountDto, /** 회원정보는 꼭 가지고 있어서 억지로 땡겨와서 넣음 */
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {


/* 새로 생성 */
    public static Ex35_2_ArticleDto of(UserAccountDto userAccountDto, String title, String content, String hashtag) {
        return new Ex35_2_ArticleDto(null, userAccountDto, title, content, hashtag, null, null, null, null);
    }

    public static Ex35_2_ArticleDto of(Long id,
                                       UserAccountDto userAccountDto,
                                       String title,
                                       String content,
                                       String hashtag,
                                       LocalDateTime createdAt,
                                       String createdBy,
                                       LocalDateTime modifiedAt,
                                       String modifiedBy) {
        return new Ex35_2_ArticleDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    
    /** entity 를 매개변수로 입력하면 ArticleDto 로 변환해주는 메서드. */
    public static Ex35_2_ArticleDto from(Article entity) {
        return new Ex35_2_ArticleDto( // 이게 저 위에 record ArticleDto 부르는거
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

    /** 위에꺼랑 반대로, dto 가 있다면 dto 로부터 엔티티를 생성하는 메서드. */
/* 여기 바꿈 toEntity() -> toEntity() 로 바꿈*/
    public Article toEntity(UserAccount userAccount) {
        return Article.of(
                userAccount,
                title,
                content,
                hashtag
        );
    }
//    public Article toEntity() { // DTO 정보로 부터 엔티티를 하나 만들어서 세이브 하는 코드임
//        return Article.of(
//                userAccountDto.toEntity(),
//                title,
//                content,
//                hashtag
//        );
//    }

    /* 이것과 비슷한 전략으로 dto > response > ArticleCommentRespose 같은거다. */
}
