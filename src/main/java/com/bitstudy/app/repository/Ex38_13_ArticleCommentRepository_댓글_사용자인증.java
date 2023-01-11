package com.bitstudy.app.repository;

import com.bitstudy.app.domain.ArticleComment;
import com.bitstudy.app.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/** 할일: ArticleCommentServiceTest 에서 댓글ID 와 해당 댓글 작성자ID 를 수정, 삭제 에 보내서 동작하도록 한다. */
/*  service > Ex38_12_ArticleCommentServiceTest.java  랑  Ex38_14_ArticleCommentService.java  랑 같이 볼것*/

@RepositoryRestResource
public interface Ex38_13_ArticleCommentRepository_댓글_사용자인증 extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>
        , QuerydslBinderCustomizer<QArticleComment> // 이부분 처음에는 주석처리 하고 아래 빨간 '설명' 부분 설명하기
{

    List<ArticleComment> findByArticle_Id(Long articleId);


/*추가*/void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true); // 선택적으로 검색 할 수 있게 true 로 바꿈
        bindings.including(root.content, root.createdAt, root.createdBy); // 이건 원하는 필드를 추가하는 부분. 엑셀 api 명세서 가서 보기. 안봐도 root. 까지 치면 자동완성으로 나옴.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // 대소문자 상관 없이 검색
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 대소문자 상관 없이 검색
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // 대소문자 상관 없이 검색
    }
}
