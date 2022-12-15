package com.bitstudy.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Ex999_날짜구하기 {
    public static void main(String[] args) {
        /*  참고: https://hianna.tistory.com/607
         *
         *   1) LocalDate - 날짜를 표현하는 클래스
         *   2) LocalTime - 시간을 표현하는 클래스
         *   3) LocalDateTime - 날짜와 시간을 표현하는 클래스
         * */

        // LocalDate
        // 현재 날짜 구하기
        LocalDate now1 = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // 포맷 적용
        String formatedNow = now1.format(formatter);
        // 결과 출력
        System.out.println(formatedNow);  // 2021/06/17

        System.out.println("----------------------------------------");


        // LocalTime
        // 현재 시간
        LocalTime now2 = LocalTime.now();
        // 현재시간 출력
        System.out.println(now2);  // 06:20:57.008731300
        // 포맷 정의하기
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        // 포맷 적용하기
        String formatedNow2 = now2.format(formatter2);
        // 포맷 적용된 현재 시간 출력
        System.out.println(formatedNow2);  // 06시 20분 57초

        // LocalDateTime
        // 현재 날짜/시간
        LocalDateTime now3 = LocalDateTime.now();
        // 현재 날짜/시간 출력
        System.out.println(now3); // 2021-06-17T06:43:21.419878100
        // 포맷팅
        String formatedNow3 = now3.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
        // 포맷팅 현재 날짜/시간 출력
        System.out.println(formatedNow3);  // 2021년 06월 17일 06시 43분 21초

        // 년, 월(문자열, 숫자), 일(월 기준, 년 기준), 요일(문자열, 숫자), 시, 분, 초 구하기
        int year = now3.getYear();  // 연도
        String month = now3.getMonth().toString();  // 월(문자열)
        int monthValue = now3.getMonthValue();  // 월(숫자)
        int dayOfMonth = now3.getDayOfMonth();  // 일(월 기준)
        int dayOfYear = now3.getDayOfYear();  // 일(년 기준)
        String dayOfWeek = now3.getDayOfWeek().toString();  // 요일(문자열)
        int dayOfWeekValue = now3.getDayOfWeek().getValue();  // 요일(숫자)
        int hour = now3.getHour();
        int minute = now3.getMinute();
        int second = now3.getSecond();

        // 년, 월(문자열, 숫자), 일(월 기준, 년 기준), 요일(문자열, 숫자), 시, 분, 초 출력
        System.out.println("년 : " + year);  // 년 : 2021
        System.out.println("월 : " + month + "(" + monthValue + ")");  // 월 : JUNE(6)
        System.out.println("일(월기준) : " + dayOfMonth);  // 일(월기준) : 17
        System.out.println("일(년기준) : " + dayOfYear);  // 일(년기준) : 168
        System.out.println("요일 : " + dayOfWeek + "(" + dayOfWeekValue + ")");  // 요일 : THURSDAY(4)
        System.out.println("시간 : " + hour);  // 시간 : 6
        System.out.println("분 : " + minute);  // 분 : 40
        System.out.println("초 : " + second);  // 초 : 35

    }
}
