package com.bitstudy.app.dto.response;


import com.bitstudy.app.dto.ArticleDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/* 게시글에 대한 내용만 따로 가지고 있는 dto  */
/* Dto와 동작원리는 비슷하지만, 다른점은 정보의 일부만 들고 있거나 가공된 형태로 정보를 가지고 있다.
 * 예를들어 nickname 의 경우 댓글테이블에 있는게 아니라 user_account 테이블이 가지고 있는건데
 * 저 아래 ArticleCommentResponse from() 메서드 를 이용해서 가공한 다음 셋팅하는거임. */

public record Ex20_ArticleResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname
) implements Serializable {

    public static Ex20_ArticleResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname) {
        return new Ex20_ArticleResponse(id, title, content, hashtag, createdAt, email, nickname);
    }

    public static Ex20_ArticleResponse from(ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new Ex20_ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}
