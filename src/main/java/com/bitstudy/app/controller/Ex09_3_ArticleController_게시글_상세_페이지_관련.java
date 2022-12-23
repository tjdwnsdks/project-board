package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/* templates 안에 articles 안에 Ex09_1_index.html  하고 오기
    게시글 상세 페이지 관련 메서드를 만들거다. /articles/{article-id} 방식으로 넘어오는걸 대비해서 만들자.
 * */

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
public class Ex09_3_ArticleController_게시글_상세_페이지_관련 {

    // 게시판 리스트 관련
    @GetMapping("/")
    public  String articles(ModelMap map) {
        /** ModelMap 쓰는 이유: 테스트파일에서 ".andExpect(model().attributeExists("articles"))" 를 이용해서 모델에 articles 라는 키값으로 데이터를 넣어주기로 했으니까 필요함. 원래 쓰던 Model 이랑 같은거임. 차이점으로는 Model은 인터페이스, ModelMap은 클래스(구현체)임. 걍 Model 써도 똑같은데, 이런것도 있다 정도로 알고만 있기 */
        map.addAttribute("articles", List.of()); // 키: articles, 값은 그냥 비어있는 list. (비어있는 immutable 리스트 - 추가, 삭제, 변경이 불가능한 List임. 컬렉션이 생성된 후 변경되는 것을 방지할 때 사용할 수 있다.)
        return "articles/index";
    }
    
    /* 게시글 상세 페이지 관련*/
    @GetMapping("/{articleId}") // 아티클 번호가 넘어올거니까 이름은 articleId 로 받자
    public String article(@PathVariable Long articleId, ModelMap map) {

        map.addAttribute("article", null); // 이 경로는 test 파일의 .andExpect(model().attributeExists("article")) 이부분 참고한거임.
        // null 대신 원래 Article 이 들어와야 하는데, 도메인코드인 Article 을 노출시키지 않을거다. 나중에 dto 같은거 만들어서 넣을거임. 그래서 지금은 일단 null 넣어놓을겅미
        map.addAttribute("articleComments",  List.of());
        return "articles/detail"; // 이 경로는 test 파일 가서 보면 "andExpect(view().name("articles/detail"))" 이부분의 경로 참고
    }
    /* 여기까지 했으면 templates > articles > detail.html (뷰파일) 만들러 가자
    * 
    *  Ex09_4_detail.html 로 ㄱㄱ
    * */

}
