package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/** Article.java 과 ArticleComment.java 의 중복필드들 하나로 합치기

 1) Article.java 에 있던 생성일시, 생성자, 수정일시, 수정자 필드를 가져온다.
        - 이거 하면서 @MappedSuperclass 써주기
 2) auditing 과 관련된거 가 가져오기
        - Article 에서 auditing 하는게 다 뽑혀왔으니까 auditing 과 관련된거 가 가져오자
        - Article.java 에서 가져오자 이거 => @EntityListeners(AuditingEntityListener.class)
 3) @ToString, @Getter 준비
 4) 파싱 준비하기
        - createdAt 과 modifiedAt 한테 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                      @Column(nullable = false, updatable = false)
 * */


@Getter // 각 필더에 접근해야 하니까 getter 를 넣는다.
        // @Setter 는 넣지 않는다. 왜냐면 여기에 있는 모든 속성들은 auditing(자동 셋팅) 이 되야지 누군가가 setting 을 하면 안되니까.
@ToString // toString 을 적용해서 편하게 출력
@EntityListeners(AuditingEntityListener.class) // Article.java 에서 가져옴
@MappedSuperclass
public class AuditingFields {

    // 실제로 웹화면에 보여줄때 그리고 웹화면에서 파라미터를 받아서 셋팅을 해야 할때 파싱을 해야 하는데, 그러려면 파싱에 대한 룰을 넣어줘야 하는데 스프링부트 포맷터 관련된 애너테이션 중에 DateTimeFormat 이 있음. 이걸 통해서 할 수 있는 방법이 있다.
    // *파싱이란 어떤 일정한 문법을 토대로 나열된 data들을 그 문법에 맞게 분석하여 새롭게 구성하는 작업을 뜻함.
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // 날짜 형식 맞춰주려고 하는거.
    @CreatedDate
    @Column(nullable = false, updatable = false) // updatable = false => 이 필드는 업데이트 불가 하다는 뜻. 이거 말고 insertable 도 있음
    private LocalDateTime createdAt; // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; // 생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // 날짜 형식 맞춰주려고 하는거.
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자



}
