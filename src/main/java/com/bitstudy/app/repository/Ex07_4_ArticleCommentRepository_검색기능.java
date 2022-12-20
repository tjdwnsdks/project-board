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

/* 워드 "API 테스트 정의" 부분 끝내고 시작하는 코드.
    클래스 맨 위에 @RepositoryRestResource 이거삽입 */
@RepositoryRestResource
public interface Ex07_4_ArticleCommentRepository_검색기능 extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>
        , QuerydslBinderCustomizer<QArticleComment> // 이부분 처음에는 주석처리 하고 아래 빨간 '설명' 부분 설명하기
{
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true); // 선택적으로 검색 할 수 있게 true 로 바꿈
        bindings.including(root.content, root.createdAt, root.createdBy); // 이건 원하는 필드를 추가하는 부분. 엑셀 api 명세서 가서 보기. 안봐도 root. 까지 치면 자동완성으로 나옴.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // 대소문자 상관 없이 검색
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 대소문자 상관 없이 검색
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // 대소문자 상관 없이 검색
    }
}

/* 다 했으면 
1) 빌드 (Ctrl + F9) 
2) HAL 가서 확인해보기
    ex) http://localhost:8080/api/articles?title=제목 부분적으로

3) 다 하면 워드파일 "검색기능 추가하기" 의 '다 하면' 부분으로 돌아가기

*/

