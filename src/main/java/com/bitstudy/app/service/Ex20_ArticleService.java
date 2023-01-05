 package com.bitstudy.app.service;


 import com.bitstudy.app.domain.type.SearchType;
 import com.bitstudy.app.dto.ArticleDto;
 import com.bitstudy.app.dto.ArticleWithCommentsDto;
 import com.bitstudy.app.repository.ArticleRepository;
 import lombok.RequiredArgsConstructor;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;


 /** 기능 관련된 로직을 다 여기다가 짤건데 엄청 왔다갔다 할거라서 헷갈릴수 있다. 잘 보기 */

// @Slf4j
 @Service // 이렇게 하면 서비스 빈으로 등록되어서 사용할 수 있게 된다.
 @RequiredArgsConstructor  // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
 @Transactional // 이 클래스 동작할때 하나라도 잘못되면 다시 롤백 시켜라 라는말
 public class Ex20_ArticleService {

     private final ArticleRepository articleRepository; // 아티클 서비스는 당연히 ArticleRepository 쓸거다.

     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
     public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
         return Page.empty();
     }

     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
     public ArticleWithCommentsDto getArticle(Long articleId) {
         return null;
     }

     public void saveArticle(ArticleDto dto) {
     }

     public void updateArticle(ArticleDto dto) {
     }

     public void deleteArticle(long articleId) {
     }

     //////////////////////////////////////////////////////////////////////
     //     //////////////////////////////////////////////////////////////////////
     //     //////////////////////////////////////////////////////////////////////


//     private final ArticleRepository articleRepository;
//
//     @Transactional(readOnly = true)
//     public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
//         if (searchKeyword == null || searchKeyword.isBlank()) {
//             return articleRepository.findAll(pageable).map(ArticleDto::from);
//         }
//
//         return switch (searchType) {
//             case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
//         };
//     }
//
//     @Transactional(readOnly = true)
//     public ArticleWithCommentsDto getArticle(Long articleId) {
//         return articleRepository.findById(articleId)
//                 .map(ArticleWithCommentsDto::from)
//                 .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
//     }
//
//     public void saveArticle(ArticleDto dto) {
//         articleRepository.save(dto.toEntity());
//     }
//
//     public void updateArticle(ArticleDto dto) {
//         try {
//             Article article = articleRepository.getReferenceById(dto.id());
//             if (dto.title() != null) { article.setTitle(dto.title()); }
//             if (dto.content() != null) { article.setContent(dto.content()); }
//             article.setHashtag(dto.hashtag());
//         } catch (EntityNotFoundException e) {
//             log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
//         }
//     }
//
//     public void deleteArticle(long articleId) {
//         articleRepository.deleteById(articleId);
//     }


     ///////////////////////////////////////////////////////////////////////////////////////////////
     ///////////////////////////////////////////////////////////////////////////////////////////////
     ///////////////////////////////////////////////////////////////////////////////////////////////
     ///////////////////////////////////////////////////////////////////////////////////////////////


//
//     private final ArticleRepository articleRepository;
//
//
//     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
//     public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
//
//         /* 검색 시도했는데 검색어가 없거나(null) , 빈칸으로만 들어온 경우에는 검색 안하는거나 마찬가지니까 그냥 게시판 리스트 화면 뿌려주면 된다. */
//         if(searchKeyword == null || searchKeyword.isBlank()) {
//            return articleRepository.findAll(pageable).map(ArticleDto::from);
//            // Page 클래스 맨 아래에 map이 있는데, Page 안의 내용물을 형변환 해서 다시 페이지로 만들어준다.
//             // ArticleDto.java 의 from() 메서드 연결해라 라는 뜻.
//         }
//
////         return Page.empty();
//         return switch (searchType) {
//             case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
//             case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
//         };
//     }
//
////     @Transactional(readOnly = true) // 트랜잭션을 읽기 전용 모드로 설정하면 트랜잭션을 커밋하더라도 영속성 컨텍스트가 flush 되지 않아서 엔티티가 등록, 수정, 삭제 가 동작하지 않는다.
////     public ArticleWithCommentsDto getArticle(Long articleId) {
////         return null;
////     }
//    @Transactional(readOnly = true)
//    public ArticleWithCommentsDto getArticle(Long articleId) {
//        return articleRepository.findById(articleId)
//                .map(ArticleWithCommentsDto::from)
//                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
//    }
//
//     public void saveArticle(ArticleDto dto) {
//         articleRepository.save(dto.toEntity());
//     }
//
//     public void updateArticle(ArticleDto dto) {
//         try {
//             Article article = articleRepository.getReferenceById(dto.id());
//             if (dto.title() != null) { article.setTitle(dto.title()); }
//             if (dto.content() != null) { article.setContent(dto.content()); }
//             article.setHashtag(dto.hashtag());
//         } catch (EntityNotFoundException e) {
//             log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
//             /* log 는 맨 위 클래스레벨에 @Slf4j 애너테이션 붙이면 됨*/
//         }
//     }
//
//     public void deleteArticle(long articleId) {
//         articleRepository.deleteById(articleId);
//     }
//


 }
