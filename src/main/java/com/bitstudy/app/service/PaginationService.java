package com.bitstudy.app.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;


/** 이번엔 페이지네이션 서비스를 바로 구현해보자 */

@Service
public class PaginationService {

    /** 페이지네이션 바의 길이 */
    private static final int BAR_LENGTH = 5;


    /** 페이지네이션 바를 어떻게 데이터로 내려줄거냐면, 리스트 형태의 숫자로 내려주면 된다.
    ** 해설 - 이건 테스트에서 이 메서드(getPaginationBarNumbers) 에 값을 보내서 리턴으로 페이지 결과를 얻어내는건데 일단 null 로 주자.
        List<Integer> : 숫자리스트를 받아서 뷰에서 navbar 에 그려주면 된다.
        int currentPageNumber: 현재 몇페이지에 있는지 알아야 하니까, 현재 페이지 번호 보내주고
        int totalPages: 전체 페이지 수 구하기 */
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {

/*새로생성 - 먼저 페이지네이션 시작하는 startNumber 와 끝나는 endNumber 번호를 구할거다.
*           이번엔 좀 특이하게 현재 보고 있는 페이지 번호가 페이지네이션의 가운데에 위치하게 해볼거다.
*           예를들어 7페이지를 보고 있고 한버에 5개의 페이지네이션을 할거라면 5,6,7,8,9 이렇게 나오게.
* */
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0); /* 1페이지나 2페이지일때 잘못하면 음수가 나올수도 있으므로 Math.max 를 이용해서 현재 계산하고 있는 값과 0중에 큰거가 찍히게 할거다. 음수면 0 나오게.   */

/* 현재 선택한 페이지가 가운데 와지는거 말고 일반적인거 하려면 이거 쓰면 됨 */
        //startNumber = Math.max(currentPageNumber / BAR_LENGTH * BAR_LENGTH , 0);

        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages); /* 이거도 계산한 값이 totalPages 보다 큰게 나오면 안되니까 Math.min 사용함 */


System.out.println("currentPageNumber: " + currentPageNumber +", totalPages:" +  totalPages);
System.out.println("startNumber: " + startNumber +", endNumber:" +  endNumber);
System.out.println("IntStream.range: " + IntStream.range(startNumber, endNumber).boxed().toList());

        // 이제 startNumber 와 endNumber 로 리스트를 만들어주면 된다.
        return IntStream.range(startNumber, endNumber).boxed().toList(); // boxed() 로 원시타입인 Integer 로 바꿔서 List에 담음
        /* IntStream 은 자바8 에서 나온 반복문을 간결하게 처리할 수 있는 인터페이스다.
        *
        * 예를들어 아래 for 문을
            for (int i = 1 ; i <= 10 ; i++) {
                System.out.println(i);
            }
          다음과 같이 바꿀 수 있다.
            IntStream.range(1, 11).forEach(System.out::println);
            * 1 부터 11 전의 수 까지 출력하라는 뜻
        *  */
        /*
            boxed() 메소드는 IntStream 같이 원시 타입에 대한 스트림 지원을 클래스 타입(예: IntStream -> Stream<Integer>)으로 전환하여 전용으로 실행 가능한 (예를 들어 본문과 같이 int 자체로는 Collection에 못 담기 때문에 Integer 클래스로 변환하여 List<Integer> 로 담기 위해 등) 기능을 수행

        */
    }


    /** 현재 이 서비스가 알고 있는 BAR_LENGTH 를 조회 할 수 있게 getter 처럼 만들어놔보자 */
    public int currentBarLength() {
        return BAR_LENGTH;
    }
    
    /** 여기까지 하고 test 파일 만들러 가기*/

}
