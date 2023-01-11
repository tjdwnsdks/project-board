 package com.bitstudy.app.service;


 import com.bitstudy.app.domain.Article;
 import com.bitstudy.app.domain.UserAccount;
 import com.bitstudy.app.domain.type.SearchType;
 import com.bitstudy.app.dto.ArticleDto;
 import com.bitstudy.app.dto.ArticleWithCommentsDto;
 import com.bitstudy.app.repository.ArticleRepository;
 import com.bitstudy.app.repository.UserAccountRepository;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import javax.persistence.EntityNotFoundException;

 /** 할일: 아까 deleteArticle 처럼 update쪽도 userId 를 넣어줘야한다. */

 @Slf4j
 @Service // 이렇게 하면 서비스 빈으로 등록되어서 사용할 수 있게 된다.
 @RequiredArgsConstructor  // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
 @Transactional // 이 클래스 동작할때 하나라도 잘못되면 다시 롤백 시켜라 라는말
 public class Ex38_11_ArticleService_업데이트에_유저정보_추가 {

     private final ArticleRepository articleRepository; // 아티클 서비스는 당연히 ArticleRepository 쓸거다.


    private final UserAccountRepository userAccountRepository; // 유저 정보 사용


     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
     public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

         if (searchKeyword == null || searchKeyword.isBlank()) {
             return articleRepository.findAll(pageable).map(ArticleDto::from);
         }

         /** searchKeyword 있을때 */
         return switch (searchType) {
             /** 원래 자바에서 switch case문 짤때
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

     }

     /** 댓글 달린 게시글 하나 호출 */
 /* 메서드 이름 변경함 getArticle  에서  getArticleWithComments  로  변경 */
     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
     public ArticleWithCommentsDto getArticleWithComments(Long articleId) {

         /** 이건 그냥 게시글 id 값만 받아오면 되니까 딴거 할거 없으니, 바로 return 에 걸자 */
         return articleRepository.findById(articleId)// 하나 잡을거니까 findById 메서드에 articleId 값 보내고
                 .map(ArticleWithCommentsDto::from)  // Dto 로 변환해줘야 해서 map() 을 이용할건데, ArticleWithCommentsDto 를 써볼거다. (이거 설명은 ArticleWithCommentsDto 파일에 있음)
                 .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId)); // orElseThrow() : 만약 문제가 생겼을 경우 Throw 를 하겠다. 라는 뜻
                    // EntityNotFoundException: 엔티티가 없으면 어떻게 하겠다. 라는 뜻.
                    // 에러나면 로그를 보기 위해서 '+ articleId' 이거 넣었음. 보안을 위한거라면 이부분은 나중에 지울것

         /** 여기까지 하고 ArticleServiceTest 에서
            ("게시글을 조회하면, 게시글을(하나) 반환한다.") 랑
            ("없는 게시글을 조회하면, 예외를 던진다.")
            테스트 하기*/
     }

     /** 게시글 하나 호출 */
/* 새로 생성 */
     @Transactional(readOnly = true)
     public ArticleDto getArticle(Long articleId) {
         return articleRepository.findById(articleId)
                 .map(ArticleDto::from)
                 .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
     }


     public void saveArticle(ArticleDto dto) {
         /** ArticleServiceTest 에서 ("게시글 정보를 입력하면, 게시글을 생성한다.") 부분 관련 */

         /** void 라서 return 이 없다. */
         UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
 /*이거 없앰*/ //articleRepository.save(dto.toEntity()); // toEntity 를 이용해서 매개변수로 받은 dto 정보로 부터 엔티티를 하나 만들어서 세이브 하는 코드임
 /* 새로 추가*/ articleRepository.save(dto.toEntity(userAccount));

     }

/*이거 없앰 - 아래 updateArticle() 메서드 추가 */
//     public void updateArticle(ArticleDto dto) {
//        try { // 수정 정보를 넣었을때 게시글이 없을때의 대비가 필요하다.
//             Article article = articleRepository.getReferenceById(dto.id());
//             if (dto.title() != null) { article.setTitle(dto.title()); } /** title 은 notNull 필드이기 때문에 만약(if) null 이 아닐때만 동작하게 해줄거다. 아래 content 도 동일 */
//
//             if (dto.content() != null) { article.setContent(dto.content()); }
//
//             article.setHashtag(dto.hashtag()); /** 해시태그는 null 가능 필드라서 그냥 입력받은 dto에 있는 값을 쓰면 된다.  */
//
//         } catch (EntityNotFoundException e) {
//            /** log 는 맨 위 클래스레벨에 @Slf4j 애너테이션 붙이면 됨*/
//            //log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: " + dto); // 이렇게 써도 되고
//            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto); // 이렇게 써도 된다.
//            // {} 를 쓰게 되면 warn 로그를 찍지 않아도 될때 dto 안에 있는 로직을 실행하거나 메모리를 잡지 않는다.
//         }
//
//     }
 /*새로 생성*/
     public void updateArticle(Long articleId, ArticleDto dto) {
         try {
             Article article = articleRepository.getReferenceById(articleId);
 /*추가*/     UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

/*추가 - 유저가 동일한지 검사하는거. article에서 영속성으로 게시글정보랑 같이 가져온 유저정보가
*       userAccount 에 담겨있는 유저정보와 같은지 동일한지 확인하는거*/
             if (article.getUserAccount().equals(userAccount)) {
                 if (dto.title() != null) {
                     article.setTitle(dto.title());
                 }/** title 은 notNull 필드이기 때문에 만약(if) null 이 아닐때만 동작하게 해줄거다. 아래 content 도 동일 */

                 if (dto.content() != null) {
                     article.setContent(dto.content());
                 }
                 article.setHashtag(dto.hashtag()); /** 해시태그는 null 가능 필드라서 그냥 입력받은 dto에 있는 값을 쓰면 된다.  */
             }
         } catch (EntityNotFoundException e) {
             /** log 는 맨 위 클래스레벨에 @Slf4j 애너테이션 붙이면 됨*/
             //log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: " + dto); // 이렇게 써도 되고
             log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto); // 이렇게 써도 된다.
             // {} 를 쓰게 되면 warn 로그를 찍지 않아도 될때 dto 안에 있는 로직을 실행하거나 메모리를 잡지 않는다.
         }
     }

     public void deleteArticle(long articleId, String userId) {
         /** 삭제는 save 때 처럼 딱히 할게 없다. id 로 그냥 지우면 된다. */
         /* 아무나 이 메소드(deleteArticle) 에 게시글 아이디만 날리면 다 지우게 할거임?
         *  그래서 삭제할때는 글번호랑, 유저아이디 같이 가져와서 DB에 둘 다 매칭 될때만 삭제하게 해야함.
         *  */

/* 이거 삭제 */ // articleRepository.deleteById(articleId);
/* 새로 생성 */ articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
/* 이거 바꾸면 ArticleRepository.java  랑  ArticleServiceTest.java 가서도 바꿔야함*/

     }

     /**게시글 개수 구하기.  마지막 글일때는 '다음' 버튼 안되게 해야 하기 때문에 count 쿼리를 이용해서 개수 뽑아옴 */
     public long getArticleCount() {
         return articleRepository.count();
         // count() - 레코드 개수 뽑을때 사용
     }

 }
