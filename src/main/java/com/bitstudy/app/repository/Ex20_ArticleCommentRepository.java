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


@RepositoryRestResource
public interface Ex20_ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>
        , QuerydslBinderCustomizer<QArticleComment> // 이부분 처음에는 주석처리 하고 아래 빨간 '설명' 부분 설명하기
{

/* 새로 삽입*/    
    List<ArticleComment> findByArticle_Id(Long articleId);
    /* 엄청 중요!!!
     *  findByArticle_Id 이건 아직 자동완성기능은 안되지만 findById 같은건데 _(언더바) 의 의미가 따로 있는거다. 무작정 붙이는게 아니다. 타고 들어가야할때 사용한다.
     *
     * 게시글로 댓글을 검색해야 하는데, 이런 경우에 사용하는 방법이다.
     * ArticleComment 안에는 Article 이랑 UserAccount 가 있는데, 그 안에 있는 객체 이름인 article을 써주고 _(언더바)로 내려가면 그 객체 안으로 들어간다. 한마디로 Article의 ID를 조회하겠다 라는 의미가 된다. 이떄 이 ID 는 댓글의 연관관계 맵핑이 되어있는 게시글의 ID가 된다.
     *
     * findByArticle_title 이런식으로도 할 수도 있다.
     * */

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true); // 선택적으로 검색 할 수 있게 true 로 바꿈
        bindings.including(root.content, root.createdAt, root.createdBy); // 이건 원하는 필드를 추가하는 부분. 엑셀 api 명세서 가서 보기. 안봐도 root. 까지 치면 자동완성으로 나옴.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // 대소문자 상관 없이 검색
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 대소문자 상관 없이 검색
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // 대소문자 상관 없이 검색
    }
}
