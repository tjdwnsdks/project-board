 package com.bitstudy.app.service;

import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

 /** 기능 관련된 로직을 다 여기다가 짤건데 엄청 왔다갔다 할거라서 헷갈릴수 있다. 잘 보기 */


 @Service // 이렇게 하면 서비스 빈으로 등록되어서 사용할 수 있게 된다.
 @RequiredArgsConstructor  // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
 @Transactional // 이 클래스 동작할때 하나라도 잘못되면 다시 롤백 시켜라 라는말
 public class Ex11_1_ArticleService {
     /* 이거까지만 하고 TDD 만들자.
         1) 우클릭 > Go To > Test
          또는
         2) Ctrl + Shift + T
     */
 // *************************************************************************** //


     /* Test > Ex11_2_ArticleServiceTest 하고 오기 */
     private final ArticleRepository articleRepository; // 아티클 서비스는 당연히 ArticleRepository 쓸거다.

     // 댓글도 여기 넣으면 좋겠지만, 댓글은 게시글 자체에서 다루게 하는게 더 좋을것 같아서 여기서는 취급하지 않는다.
     /* 다시 TDD (Ex11_2_ArticleServiceTest) 로 돌아가기 */


     /** Test > Ex11_2_ArticleServiceTest.java 에서 '게시글 리스트 검색' selectAll() 메서드 관련   */
     /* 이거 하고 저 위에 @Transactional 달기 */
     /** TDD 에서 selectAll() 설명할때 쓸거임.  근데 TDD에서 페이지네이션 관련 설명할때는 아래꺼로 바꿔야함.

     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
     public List<ArticleDto> searchArticles(SearchType title, String search_keyword) {
         return List.of(); // 원래는 articleRepository.findAll() 이런식으로 다 가져와라 해야하는데 일단 테스트만 할거니까 간단히 List.of() 로 에러만 안나게 리턴한다.
     } */

     /** TDD Ex11_2_ArticleServiceTest 에서 '1.검색' 이랑 '3.페이지네이션' 때 사용하는 메서드 */
     @Transactional(readOnly = true)
     public Page<ArticleDto> searchArticles(SearchType title, String search_keyword) {
         return Page.empty();

     }

     /** TDD Ex11_2_ArticleServiceTest 에서 '2. 각 게시글 페이지로 이동' 때 사용하는 메서드 */
     @Transactional(readOnly = true) // 읽기만 하면 되니까 여기도 readonly
     public ArticleDto searchArticle(long l) {
         return null; // 아직 service 로직 구현 안했으니까 일단 테스트 실패하게 null 넣을거임.
     }
 }
