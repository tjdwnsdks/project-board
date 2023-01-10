package com.bitstudy.app.dto.request;

import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.dto.UserAccountDto;

/* ArticleContoller 랑 ArticleContollerTest 에서 게시글 쓰기 관련해서 사용됨.

    사용자가 작성한 게시글 관련 데이터만 모은 Dto */

public record Ex35_6_ArticleRequest(
        String title,
        String content,
        String hashtag
) {

    public static Ex35_6_ArticleRequest of(String title, String content, String hashtag) {
        return new Ex35_6_ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtag
        );
    }
}
