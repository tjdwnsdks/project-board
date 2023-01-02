package com.bitstudy.app.service;


import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.repository.ArticleRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;


/** 서비스 비지니스 로직은 스프링 부트의 슬라이스 테스트 기능을 사용하지 않고 작성해볼거다.
 그렇게 함으로서 스프링부트 애플리케이션 컨텍스트가 뜨는데 걸리는 시간을 없애버리려고 한다.
 가능한한 가볍게 만들거고, 불필요한 스프링부트 어플리케이션을 띄우는 과정을 생략하는 대신에 디펜던시가 추가되어야 하는 부분이 있다면 Mocking 을 하는 방식으로 해볼거다.
 그래서 많이 사용하는 라이브러리가 mokito 라는게 있다. 스프링 테스트 패키지에 이미 포함되어 있어서 따로 설치할 필요는 없다.
 @ExtendWith(MockitoExtension.class) 쓰면 된다.

  * 이번에는 junit 안쓰고 assertJ 써볼거다.
 import 부분 수정하자
  * */


@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class Ex11_2_ArticleServiceTest {

    /* Mock을 주입하는 거에다가 @InjectMocks 를 달아줘야 한다. 그 외의 모든 Mock은 @Mock 을 달아준다. */
    @InjectMocks
    private ArticleService sut; // sut - system under test 라고 해서. 실무에서 테스트 짤대 사용하는 이름중 하나다. 이게 테스트 대상이다 라는뜻임.
    // Mock 을 주입하는 대상에는 @InjectMocks 에너테이션을 주고 , 그 외 나머지 모든 Mock 은 @Mock 애너테이션을 준다.

    @Mock
    private ArticleRepository articleRepository; // 여기 의존하는 걸 가져와야함. 테스트 중간에 mocking 할때 필요함.


    /** - 테스트 할 기능들 리스트(칸반보드에서 가져옴)
     1. 검색
     2. 각 게시글 페이지로 이동
     3. 페이지네이션
     4. 홈버튼 클릭시 -> 게시판 페이지로 리다이렉션   <= 이건 버튼 누르면 페이지 이동하는거라서 컨트롤러에서 해야함.
     * */


    /*  1. 검색  */
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void selectAll() {
        /** 제목, 본문, id, 닉네임, 해시태그 중에서 한번에 하나로만 검색되게 할건데 우선 '제목' 으로만 검색을 해볼거다.
         searchArticles 메서드를 만들건데 거기에 매개변수로 '제목' 이랑 '검색어' 를 보내서 DB에서 찾게 할거다.
         */
        // List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "검색어");

        /* 아래 selectOne() 하고 와도 됨.
         3. 페이지네이션 관련인데, 페이지는 별도로 불러올게 아니라 게시글 전체 불러올때 같이 불러와야 하니까 List 대신 Page 로 받아오면 됨
          이거 Page 로 바꾸면 */
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "검색어");

        assertThat(articles).isNotNull();

        /* 위에거 끝나면 아래 3개 작업 하기
            1) main > dto 패키지 만들고, 그 안에 ArticleDto.java 만들기
            2) main > domain 패키지 않에 type 패키지 만들고 그 안에 (class 말고) enum 으로 SearchType.java 만들기
            3) main > ArticleService.java 에 searchArticles 메서드 만들기 */

        /* 여기까지 하고 selectAll() 메서드 테스트 해보기*/
    }


    /*  2. 각 게시글 페이지로 이동  */
    @Disabled("구현중")
    @DisplayName("게시글을 조회하면, 게시글 하나를 반환한다.")
    @Test
    void selectOne() {
        /** 게시글 하나 불러올때는 해당글의 ID 로 검색할거다. */
        ArticleDto articles = sut.searchArticle(1L);

        assertThat(articles).isNotNull();
    }


    /* 3. 페이지네이션 - 이건 저 위에 selectAll() 메서드에서 하면 됨 */


    /* 4. 홈버튼 클릭시 -> 게시판 페이지로 리다이렉션
        이건 버튼 누르면 페이지 이동하는거라서 컨트롤러에서 해야함.

     *  main > controller > Ex11_5_MainController 만들고 맵핑이랑 뷰 파일 이동하게 페이지 지정해보자.

      Ex11_5_MainController ㄱㄱ*/

}