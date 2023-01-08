package com.bitstudy.app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이지네이션")
//@SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
/* @SpringBootTest 에는 여러가지 옵션을 줄 수 있는데, 대표적인게 webEnvironment 이다. 이건 기본값은 Mock 이다. Mocking 한 웹 환경을 넣어주는건데, None 을 넣어서 웹 환경을 넣지 않아서 스프링부트 테스트를 가볍게 할수 있고,
    classes = PaginationService.class 를 이용해서 설정 클래스를 지정해서 한번 더 가볍게 할 수 있다. 얘는 우리 메인 파일(ProjectBoardApplication) 에서 시작하는 모든 bean 스캔 대상들을 configration 에서 불러들이면서 통합테스트를 할 수 있게 하는건데 void.class 아무것도 안 읽게 만들어줄 수 있다. 그럼 엄청 빨라지는데 우린  sut 에서 PaginationService 를 가져다 써야 하니까 PaginationService.class 를 써서 PaginationService 클래스 정보를 얻어오긴 해야한다.

     만약 원래 쓰듯이 @SpringBootTest 만 쓰고 싶으면 private final PaginationService sut = new PaginationService(); 로 대신 쓰면 된다.
     (대신 컨스트럭터 인젝션 없애야함)
     */
class Ex31_2_PaginationServiceTest {
    
    // 일반 @SpringBootTest 쓸때 사용
//    private final PaginationService sut = new PaginationService();

    private final PaginationService sut;

    /*컨스트럭터 인젝션*/
    public Ex31_2_PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }


    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다.")
    /* 파라미터 테스트를 해볼거다.
      ParameterizedTest 란 여러 argument 를 이용해서 테스트를 여러번 돌릴 수 있는 테스트를 할 수 있는 기능을 가지고 있다. 그걸 제외하면 @Test 와 같다.
      ParameterizedTest 에 값을 연속적으로 주입해서 동일한 메서드 (getPaginationBarNumbers 요거) 를 여러번 테스트 하면서 입력 출력값을 볼 수 있다.
      ParameterizedTest 를 돌릴때 테스트를 위해 들어가는 값이나 객체들이 필요한데 그걸 Source 라고 한다.

       Source 종류로는 세가지가 있는데
        1. ValueSource - 같은 타입의 여러 가지 단순 값(literal value)들을 테스트할 때 사용되는 Source이다.
                        사용법: @ValueSource(ints = {0, 10001})

        2. CsvSource - comma( , )로 구분되는 값들을 인자로 사용할 수 있는 Source이다.
                        사용법: @CsvSource({"2, true", "1, false"})

        3. MethodSource - 메소드에서 리턴되는 값을 인자로 사용하여 테스트
                          *왠만해선 이거 쓰면 됨.
                          입력값이 별도로 있어야 한다.
        */
    @ParameterizedTest(name = "[{index}] 현재 페이지: {0}, 총 페이지: {1} => {2}") // 그냥 @ParameterizedTest 만 써도 되는데 너무 지저분하게 나온다. name 을 이용해서 출력 포멧을 넣어주자. {0} {1} {2} 는 givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers() 메서드의 매개변수를 의미한다. (currentPageNumber, totalPages, List<Integer> expected 요거)
    @MethodSource /* 원래 여기에 @MethodSource("연결할 메서드 명") 써주면 되는데 현재 메서드 명이랑 똑같이 Arguments 짤거면 생략해도 된다. */
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers(int currentPageNumber, int totalPages, List<Integer> expected) { /** 현재 페이지 번호, 전체 페이지 개수, 검증하고 싶은 값을 매개변수로 넣기 */

        // Given - 여기에 명시하지 않고

        // When
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber, totalPages);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers() {
        return Stream.of(
                // arguments 는 딥다이브로 import 하자 (alt + enter) 해서 했던거 (Arguments.arguments 이거 import 하면 됨)
                /** - 해설 -
                    arguments(1, 10, List.of(0, 1, 2, 3, 4))  이거는
                    요 위에 있는 메서드에 매개변수 보내는 용도임.
                    현재 페이지가 1페이지이고, 전체 페이지 수는 10 페이지다, 현재 나와야 하는 페이지네이션 번호는 1,2,3,4,5 다 라는 뜻임
                    전체 아티클 수가 100개니까 10개씩 하면 10 페이지, (한페이지당 몇개 게시글 보여줄지는 ArticleController.java 에 @PageableDefault(size = 10 한부분이 결정)
                    Page 인터페이스에서 제공해주는 페이지 번호는 0번부터 시작하기 때문에 List.of(0, 1, 2, 3, 4) 라고 쓴거임
                    view 쪽 에서는 +1 해서 표현하면 된다.
                 */
                arguments(0, 10, List.of(0, 1, 2, 3, 4)), /* Page 는 0 페이지부터임 */
                arguments(1, 10, List.of(0, 1, 2, 3, 4)),
                arguments(2, 10, List.of(0, 1, 2, 3, 4)),
                arguments(3, 10, List.of(1, 2, 3, 4, 5)), /* 4페이지 들어갔을때 해당 페이지가 가운데에 위치하도록 하려고 함. 그래서 4페이지에 들어와 있을때는 뷰쪽 에서는 2,3,4,5,6 로 나오게 함 */
                arguments(4, 10, List.of(2, 3, 4, 5, 6)),   
                arguments(5, 10, List.of(3, 4, 5, 6, 7)),
                arguments(6, 10, List.of(4, 5, 6, 7, 8)),
                arguments(7, 10, List.of(5 ,6, 7, 8, 9)),
                arguments(8, 10, List.of(6, 7, 8, 9)), /* 10 페이지까지밖에 없으니깐 9가 끝남*/
                arguments(9, 10, List.of(7, 8, 9))
        );

        /* 일단 테스트 돌려도 이건 에러남. 일단 속도 차이를 좀 보자. @SpringBootTest 이부분 바꿔서 엄청 빠를거다.  */
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void givenNothing_whenCalling_thenReturnsCurrentBarLength() {
        // Given

        // When
        int barLength = sut.currentBarLength(); /* PaginationService.java 에서 currentBarLength() 메서드 만든거 쓰는거임.
                                                    페이지네이션 바의 기본 길이가 몇인지 테스트 코드에서도 확인할 수 있게 하는 용도임.
                                                    우리가 개발하는거라서 우린 알지만 유지보수나 협업시 또는 특정상황에서 기본 나타내야 하는 페이지의 수를 구해야 할때 사용. */

        // Then
        assertThat(barLength).isEqualTo(5);
    }

    /* 여기까지 하고 Ex31_3_PaginationService 파일 만들러 가기*/
}