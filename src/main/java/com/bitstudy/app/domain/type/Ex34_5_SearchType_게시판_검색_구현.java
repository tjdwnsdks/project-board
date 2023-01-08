package com.bitstudy.app.domain.type;

import lombok.Getter;

/* 할일: 서치타입이 한글로 나오게 부가정보 추가해보자
*       생성자랑 getter 만들어서 다른 파일에서 여기에 있는 이름들 가져다 쓸 수 있게 하자  */

public enum Ex34_5_SearchType_게시판_검색_구현 {
    /** 서비스 화면에서 검색 부분의 select option 부분 열어보면 아래 다섯개가 있음 */
//    TITLE, CONTENT, ID, NICKNAME, HASHTAG

    /* 그냥 이 위처럼 두면 영어로만 나오기때문에 한글로 나오게 부가정보 추가해보자 */
    TITLE("제목"),
    CONTENT("본문"),
    ID("유저 ID"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그"); /* 계속 컴마로 끝나다가 마지막꺼만 세미콜론*/


    /* 생성자랑 getter 만들고, index.th.xml 에 searchType. 에서 description 을 읽을수 있게 @Getter 달아줌 */
    @Getter
    private final String description;

    Ex34_5_SearchType_게시판_검색_구현(String description) {
        this.description = description;
    }
}
