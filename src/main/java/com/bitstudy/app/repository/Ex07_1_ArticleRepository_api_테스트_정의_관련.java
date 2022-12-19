package com.bitstudy.app.repository;

import com.bitstudy.app.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/* 워드 "API 테스트 정의" 부분 끝내고 시작하는 코드.*/

/** 할일 - 클래스 위에 @RepositoryRestResource 넣어서 해당 클래스를 spring rest data 를 사용할 준비 시키기
 *
 *  Ex07_2_ArticleCommentRepository_api_테스트_정의_관련 에 가서도 똑같이 하기
 *
 *  그 다음 service 탭에서 실행버튼 눌러서 실행하고(또는 Ctrl + Shift + F10)
 *  브라우저에서 http://localhost:8080/api 치면 HAL Explorer 켜진다 */
/* HAL 켰으면 잠깐 워드파일 'HAL 브라우저 확인' 부분 보고오기 */

@RepositoryRestResource
public interface Ex07_1_ArticleRepository_api_테스트_정의_관련 extends JpaRepository<Article, Long> {
}
