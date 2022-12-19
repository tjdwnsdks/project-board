package com.bitstudy.app.repository;



import com.bitstudy.app.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/* 워드 "API 테스트 정의" 부분 끝내고 시작하는 코드.
    클래스 맨 위에 @RepositoryRestResource 이거삽입 */
@RepositoryRestResource
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> { // JpaRepository 를 확장(상속)해야 한다.
    
}
