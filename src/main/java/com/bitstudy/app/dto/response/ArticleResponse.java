package com.bitstudy.app.dto.response;


import com.bitstudy.app.dto.ArticleDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname
) implements Serializable {

    public static ArticleResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, hashtag, createdAt, email, nickname);
    }


    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname(); /* 게시판에 보여줄때 작성자가 닉네임으로 나왔으면 좋겠어서 nickname 을 받아온다. 그런데 닉네임은 null 이 가능한 필드이기 때문에  */
        if (nickname == null || nickname.isBlank()) { /* if 문에서 null 이거나 빈칸으로만 되어 있으면 userId를 가져와서 nickname 으로 쓴다. */
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleResponse(
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
