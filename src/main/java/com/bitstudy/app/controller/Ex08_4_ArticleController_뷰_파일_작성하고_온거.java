package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/* template > articles > Ex08_3 뷰 파일 만들고 와서 보기 */

/** index 테스트코드랑 뷰파일(index.html) 만들었으니까 게시글 다 불러오는 메서드 하나 하나 만들어보자 */

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
public class Ex08_4_ArticleController_뷰_파일_작성하고_온거 {

    @GetMapping
    public  String articles(ModelMap map) {
        /* ModelMap 쓰는 이유: 테스트파일에서 ".andExpect(model().attributeExists("articles"))" 를 이용해서 모델에 articles 라는 키값으로 데이터를 넣어주기로 했으니까 필요함.
            원래 쓰던 Model 이랑 같은거임.
            차이점으로는 Model은 인터페이스, ModelMap은 클래스(구현체)
                                                걍 Model 써도 똑같은데, 이런것도 있다 정도로 알고만 있기 */
        map.addAttribute("articles", List.of()); // 키: articles, 값은 그냥 list
        return "articles/index";
    }

    /* 다 했으면 워드로 돌아가기 - 커밋 할거임*/
}
