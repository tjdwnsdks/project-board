package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** 뷰 엔드포인트 관련 컨트롤러
 * 엑셀 api 에 보면 우리가 정의해놓은 URL 부분들 있다. 그거 보면서 하면 됨(아래 있는거)
     /articles	                GET	게시판 페이지
     /articles/{article-id}	    GET	게시글(상세)페이지
     /articles/search	        GET	게시판 검색 전용 페이지
     /articles/search-hashtag	GET	게시판 해시태그 검색 전용 페이지

 * 모든 경로들은 /articles 들어가니까 클래스 레벨에 1차로 @RequestMapping("/articles") 걸어놓자
 * */


@Controller
@RequestMapping("/articles") // 모든 경로들은 /articles 들어가니까 클래스 레벨에 1차로 @RequestMapping("/articles") 걸어놓자
                            /* 이제 서비스 돌리고, URL 에 http://localhost:8080/articles 이거 넣으면 동작한다.*/
public class Ex08_1_ArticleController_게시판_뷰_테스트 {

}
/* 이렇게 까지만 하고 test 에 Ex08_2 만들어보기 */
