package com.bitstudy.app.domain;

import java.time.LocalDateTime;

public class ArticleComment {
    private Long id;
    private Article article;// 내용
                            // article_id 로 해야하는데 그게 Article 의 ID 와 연결되어 있어서
                            // Article 과 관계를 맺고 있는걸 객체지향적으로 표현하고 있다.
    private String content; // 본문

    // 메타데이터
    private LocalDateTime createdAt; // 생성일시
    private String createdBy; // 생성자
    private LocalDateTime modifiedAt; // 수정일시
    private String modifiedBy; // 수정자
}
