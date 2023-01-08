package com.bitstudy.app.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ex31_1_PaginationService {

    /* 페이지네이션 바의 길이 */
    private static final int BAR_LENGTH = 5;


    /* 페이지네이션 바를 어떻게 데이터로 내려줄거냐면, 리스트 형태의 숫자로 내려주면 된다.
    * 해설 - 이건 테스트에서 이 메서드(getPaginationBarNumbers) 에 값을 보내서 리턴으로 페이지 결과를 얻어내는건데 일단 null 로 주자.
        List<Integer> : 숫자리스트를 받아서 뷰에서 navbar 에 그려주면 된다.
        int currentPageNumber: 현재 몇페이지에 있는지 알아야 하니까, 현재 페이지 번호 보내주고
        int totalPages: 전체 페이지 수 구하기 */
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        return null;
    }


    /* 현재 이 서비스가 알고 있는 BAR_LENGTH 를 조회 할 수 있게 getter 처럼 만들어놔보자 */
    public int currentBarLength() {
        return BAR_LENGTH;
    }
    
    /* 여기까지 하고 Ex31_2_PaginationServiceTest 파일 만들러 가기*/

}
