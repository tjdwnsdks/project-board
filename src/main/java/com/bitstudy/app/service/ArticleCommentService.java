package com.bitstudy.app.service;

import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.repository.ArticleCommentRepository;
import com.bitstudy.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service // 이렇게 하면 서비스 빈으로 등록되어서 사용할 수 있게 된다.
@RequiredArgsConstructor  // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
@Transactional // 이 클래스 동작할때 하나라도 잘못되면 다시 롤백 시켜라 라는말
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;


    @Transactional(readOnly = true) // 조회만 하는거니까 readonly 걸어주면 됨
    public List<ArticleCommentDto> searchArticleComment(long l) {
        return List.of(); // 일단 리턴은 빈껍데기. 일단 TDD에서 에러남 괜찮음. 호출 제대로 되는지만 확인하는거임
    }

    public void saveArticleComment(ArticleCommentDto dto) {
    }

    public void updateArticleComment(ArticleCommentDto dto) {
    }

    public void deleteArticleComment(Long articleCommentId) {
    }
}
