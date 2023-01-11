package com.bitstudy.app.dto.response;

import com.bitstudy.app.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/** 댓글 요청하는 컨트롤러에 응답으로 내보내는 전용 DTO 임*/

/* Dto와 동작원리는 비슷하지만, 다른점은 정보의 일부만 들고 있거나 가공된 형태로 정보를 가지고 있다.
 * 예를들어 nickname 의 경우 댓글테이블에 있는게 아니라 user_account 테이블이 가지고 있는건데
 * 저 아래 ArticleCommentResponse from() 메서드 를 이용해서 가공한 다음 셋팅하는거임. */

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname,

        /*새로 추가 - 이거 바꾸면 아래 of 메서드에서도 바꿔야함*/
        String userId
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname, /**/String userId/**/) {
        /*추가*/  return new ArticleCommentResponse(id, content, createdAt, email, nickname, /**/userId/**/);
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
                nickname,
                /*추가*/         /**/dto.userAccountDto().userId()/**/
        );
    }


}
