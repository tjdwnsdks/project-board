package com.bitstudy.app.repository;

import com.bitstudy.app.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;


/** TDD 를 위해서 일단 임시로 만들어보자!! (이걸로 DB에 접근할거다)
 *  지금 이 파일 만들자 마자 JPA Buddy 안쓰고 쌩으로 ArticleCommentRepository 도 만들자
 *
 * - 만드는 방법
 * 1) repository 패키지 우클릭 > New > Java Class > Interface 선택
 * 2) ArticleCommentRepository 이름 넣기
 * */

public interface Ex04_ArticleRepository extends JpaRepository<Article, Long> {
}

/* Ex05_ArticleCommentRepository 하고 와서 테스트 파일 만들기

 * 방법1) 우클릭 > GO TO > 맨 아래 Test
 * 방법2) 단축키: Ctrl + Shift + T
 *
 * - 창 뜨면
 *       JUnit5 버전 확인하고
 *       원래 이름은 ArticleRepositoryTest 일건데,
 *       지금은 Article에 대한걸 테스트 하려는게 아니라 JPA 가 제대로 도는지 확인하려는 거라서
 *       이름을 바꿔서 JpaRepositoryTest 로 바꿔서 하나 만들자.

 */