package com.bitstudy.app.dto.response;

import com.bitstudy.app.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/** 댓글 요청하는 컨트롤러에 응답으로 내보내는 전용 DTO 임*/

/** Dto 같은건데 정보의 일부만 들고 있거나 가공된 형태로 정보를 가지고 있다. */

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleCommentResponse(id, content, createdAt, email, nickname);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) { /* ArticleCommentDto 을 받아서*/
        String nickname = dto.userAccountDto().nickname(); /* nickname 을 받아오는데*/
        if (nickname == null || nickname.isBlank()) { /* 만약에 nickname 이 없으면 */
            nickname = dto.userAccountDto().userId(); /* userId 를 대신 불러와서 nickname 변수에 넣어라. */
            // 어찌됐든 nickname 을 채워넣겠다는 코드임
        }

        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }


}
