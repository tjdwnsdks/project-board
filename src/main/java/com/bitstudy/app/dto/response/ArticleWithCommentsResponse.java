package com.bitstudy.app.dto.response;


import com.bitstudy.app.dto.ArticleWithCommentsDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname,

        /*새로 추가 - 이거 바꾸면 아래 of 메서드에서도 바꿔야함*/
        String userId,

        Set<ArticleCommentResponse> articleCommentsResponse
) implements Serializable {

    public static ArticleWithCommentsResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname, /**/String userId/**/, Set<ArticleCommentResponse> articleCommentResponses) {
        /*추가*/       return new ArticleWithCommentsResponse(id, title, content, hashtag, createdAt, email, nickname, /**/userId/**/,  articleCommentResponses);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                /*추가*/                /**/dto.userAccountDto().userId(),/**/
                dto.articleCommentDtos().stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

}
