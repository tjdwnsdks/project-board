package com.bitstudy.app.repository;

import com.bitstudy.app.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;


/* extends 까지 다 쓰면 ArticleRepository 의 Test 파일 작성해보자
    (!!! 주의 현재 이 파일 말고 ArticleRepository 파일꺼 만들거임!!!)

* 방법1) 우클릭 > GO TO > 맨 아래 Test
* 방법2) 단축키: Ctrl + Shift + T
*
* - 창 뜨면
*       JUnit5 버전 확인하고
*       원래 이름은 ArticleRepositoryTest 일건데,
*       지금은 Article에 대한걸 테스트 하려는게 아니라 JPA 가 제대로 도는지 확인하려는 거라서
*       이름을 바꿔서 JpaRepositoryTest 로 바꿔서 하나 만들자.
*  */
public interface Ex05_ArticleCommentRepository extends JpaRepository<ArticleComment, Long> { // JpaRepository 를 확장(상속)해야 한다.
    
}
