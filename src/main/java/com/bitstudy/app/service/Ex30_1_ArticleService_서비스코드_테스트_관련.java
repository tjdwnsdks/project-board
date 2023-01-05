 package com.bitstudy.app.service;


 import com.bitstudy.app.domain.Article;
 import com.bitstudy.app.domain.type.SearchType;
 import com.bitstudy.app.dto.ArticleDto;
 import com.bitstudy.app.dto.ArticleWithCommentsDto;
 import com.bitstudy.app.repository.ArticleRepository;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import javax.persistence.EntityNotFoundException;

 /* ArticleDto 랑 ArticleRepository.java  미리 만들고 와야함!!!*/
 
 /*!!  ArticleService.java  랑  ArticleServiceTest.java 파일 양쪽에 나눠서 열고 보기  !!*/

 /** 기능 관련된 로직을 다 여기다가 짤건데 엄청 왔다갔다 할거라서 헷갈릴수 있다. 잘 보기
  *
  * 우선 칸반보드의 '뷰 기능 구현' > '게시판 페이지 기능 구현' 을 해볼거다. */

 /* 메서드 하나하나 만들면서 ArticleServiceTest 에 있는 메서드들 하나씩 테스트 해볼거다.
 *  
 * */

 @Slf4j /* 맨 아래쪽에 updateArticle() 메서드의 catch 에서 log() 메서드 쓸때 필요 */
 @Service // 이렇게 하면 서비스 빈으로 등록되어서 사용할 수 있게 된다.
 @RequiredArgsConstructor  // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
 @Transactional // 이 클래스 동작할때 하나라도 잘못되면 다시 롤백 시켜라 라는말
 public class Ex30_1_ArticleService_서비스코드_테스트_관련 {

     private final ArticleRepository articleRepository; // 아티클 서비스는 당연히 ArticleRepository 쓸거다.

     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
     public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

         /** ArticleServiceTest 의 첫번째 메서드때 만들었던 ("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.") 부분을 구현할거다.
            When 부분에서 searchArticles() 메서드에 보면 searchType, searchKeyword, pageable 이 매개변수로 전달될건데, 사실 검색 관련된 부분이기 때문에 검색어인 searchKeyword 를 가지고 코드를 짜볼거다.

          searchKeyword 가 있을때가 있을거고, 없을때도 있을건데

          우선은 없을때꺼를 짜볼거다.
            */

         /* searchKeyword 없을때 */
         // searchKeyword 가 비어있거나 빈칸으로만 구성되서 넘어올때는 그냥 검색을 안하면 된다. 그래서 굳이 검색쿼리를 내보낼게 아니라 그냥 전체 페이지 긁어오는 paging 쿼리 한번 더 날려주면 된다. findAll(pageable) 부분이 그거임.
         if (searchKeyword == null || searchKeyword.isBlank()) {
             return articleRepository.findAll(pageable).map(ArticleDto::from); // Page 클래스 맨 아래에 map이 있는데, Page 안의 내용물을 형변환 해서 다시 페이지로 새로 만들어준다. map(ArticleDto::from); 부분이 그건데 article 을 articleDto 로 바꿔서 리턴해주는거다.
             // 안해도됨:   이렇게 해서 서비스는 도메인코드와 articleDto 까지만 아는거다. 컨트롤러에서 실제로 무슨 데이터를 보여줄지는 모른다.
         }

         /* searchKeyword 있을때 */
         // searchType 이 enum 으로 되어있다. (domain > type > SearchType.java)
         // enum 을 주제로 서로 다른 쿼리를 만들거다.
         // title 검색 할때는 제목 검색 쿼리, id 검색 할때는 id 검색 쿼리를 반환하게 할거다.
         return switch (searchType) {
             /* 원래 자바에서 switch case문 짤때
             case TITLE:
                 코드
                 break;
             이렇게 짰었는데 이제 switch가 '->' 이 키워드를 이용해서 스스로 리턴도 할 수 있게 됐다.*/

             // findByTitleContaining 은 ArticleRepository 에 만들어놨고, searchKeyword 랑 pageable 는 매개변수로 받았으니까 그냥 넘겨주면 된다. 그리고 그냥 내보내는게 아니라 매핑 해서 dto 로 넘겨주면 된다(map(ArticleDto::from) 이거).
             // 이걸 각각 제목, 내용, 아이디, 닉네임, 해시태그 용으로 만들어주면 된다.
             case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
             case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
             case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
             case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
             case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
             // 해시태그 검색할때 사용자들이 일일히 # 을 넣어줘야 하는데 그럼 귀찮으니까 우리가 #을 기본으로 넣어주자.
             // 이렇게 하면 사용자가 혹시라도 #을 넣으면 ## 으로 되는 문제가 생길수도 있다. 이건 그냥 취향에 따라 넣어주던가 말던가 하자.
         };

         //return Page.empty();

         /* 여기까지 하고 ArticleServiceTest 가서
         * (("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")) 이거랑
         * (("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다."))  이거 테스트 돌리면 성공한다.
         *  */
     }


     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
     public ArticleWithCommentsDto getArticle(Long articleId) {

         /* 이건 그냥 게시글 id 값만 받아오면 되니까 딴거 할거 없으니, 바로 return 에 걸자 */
         return articleRepository.findById(articleId)// 하나 잡을거니까 findById 메서드에 articleId 값 보내고
                 .map(ArticleWithCommentsDto::from)  // Dto 로 변환해줘야 해서 map() 을 이용할건데, ArticleWithCommentsDto 를 써볼거다. (이거 설명은 ArticleWithCommentsDto 파일에 있음)
                 .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId)); // orElseThrow() : 만약 문제가 생겼을 경우 Throw 를 하겠다. 라는 뜻
                    // EntityNotFoundException: 엔티티가 없으면 어떻게 하겠다. 라는 뜻.
                    // 에러나면 로그를 보기 위해서 '+ articleId' 이거 넣었음. 보안을 위한거라면 이부분은 나중에 지울것

         /* 여기까지 하고 ArticleServiceTest 에서
            ("게시글을 조회하면, 게시글을(하나) 반환한다.") 랑
            ("없는 게시글을 조회하면, 예외를 던진다.")
            테스트 하기*/
     }

     public void saveArticle(ArticleDto dto) {
         /** ArticleServiceTest 에서 ("게시글 정보를 입력하면, 게시글을 생성한다.") 부분 관련 */

         /* void 라서 return 이 없다. */

         articleRepository.save(dto.toEntity()); // toEntity 를 이용해서 매개변수로 받은 dto 정보로 부터 엔티티를 하나 만들어서 세이브 하는 코드임
         
         /* 여기까지 하고 
         * ("게시글 정보를 입력하면, 게시글을 생성한다.")
         * 테스트 하기
         * */
     }

     public void updateArticle(ArticleDto dto) {
        try { // 수정 정보를 넣었을때 게시글이 없을때의 대비가 필요하다.
             Article article = articleRepository.getReferenceById(dto.id());
             if (dto.title() != null) { article.setTitle(dto.title()); } /* title 은 notNull 필드이기 때문에 만약(if) null 이 아닐때만 동작하게 해줄거다. 아래 content 도 동일 */

             if (dto.content() != null) { article.setContent(dto.content()); }

             article.setHashtag(dto.hashtag()); /* 해시태그는 null 가능 필드라서 그냥 입력받은 dto에 있는 값을 쓰면 된다.  */

         } catch (EntityNotFoundException e) {
            /* log 는 맨 위 클래스레벨에 @Slf4j 애너테이션 붙이면 됨*/
            //log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: " + dto); // 이렇게 써도 되고
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto); // 이렇게 써도 된다.
            // {} 를 쓰게 되면 warn 로그를 찍지 않아도 될때 dto 안에 있는 로직을 실행하거나 메모리를 잡지 않는다.
         }


         /* 여기까지 하고
          * ("게시글의 수정 정보를 입력하면, 게시글을 수정한다.") 랑
          * ("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
          * 테스트 하기
          *  */

     }

     public void deleteArticle(long articleId) {
         /* 삭제는 save 때 처럼 딱히 할게 없다. id 로 그냥 지우면 된다. */
         articleRepository.deleteById(articleId);


         /* 여기까지 하고
          * ("게시글의 ID를 입력하면, 게시글을 삭제한다")
          * 테스트 하기
          *  */
     }

    /* 여기까지 한거면 일단 서비스로직 까지 구현이 완료 된거고, 이제 이걸 컨트롤러로 올리면 된다.
        ArticleController.java
        ArticleControllerTest.java 열자
    *  */
 }
