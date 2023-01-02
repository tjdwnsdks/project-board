package com.bitstudy.app.service;


/* 서비스 비지니스 로직은 스프링 부트의 슬라이스 테스트 기능을 사용하지 않고 작성해볼거다.
 * 그렇게 함으로서 스프링부트 애플리케이션 컨텍스트가 뜨는데 걸리는 시간을 없애버리려고 한다.
 * 가능한한 가볍게 만들거고, 불필요한 스프링 스프링부트 띄우는 과정을 생략하는 대신에 디펜던시가 추가되어야 하는 부분이 있다면
 * Mocking 을 하는 방식으로 해볼거다.
 *
 * 그래서 많이 사용하는 라이브러리가 mokito 라는게 있다. 스프링 테스트 패키지에 이미 포함되어 있어서 따로 설치할 필요는 없다.
 * @ExtendWith(MockitoExtension.class) 쓰면 된다.
 *
 * 이번에는 junit 안쓰고 assertJ 써볼거다.
 * import 부분 수정하자
 * */

import com.bitstudy.app.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** !! import 부분 수정하자 - assertJ 사용할거임 */
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    /* Mock을 주입하는 거에다가 @InjectMocks 를 달아줘야 한다. 그 외의 모든 Mock은 @Mock 을 달아준다. */
    @InjectMocks private ArticleService sut; // sut - system under test 라고 해서. 실무에서 테스트 짤대 사용하는 이름중 하나다. 이게 테스트 대상이다 라는뜻임.

    @Mock
    private ArticleRepository articleRepository; // 여기 의존하는 걸 가져와야함. 테스트 중간에 mocking 할때 필요함.


/** - 테스트 할 기능들 리스트(칸반보드에서 가져옴)
 검색
 각 게시글 페이지로 이동
 페이지네이션
 홈버튼 클릭시 -> 게시판 페이지로 리다이렉션
 * */
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void test() {

        sut.searchArticles(); // 제목, 본문, ID, 닉네임, 헤시태그

    }

}