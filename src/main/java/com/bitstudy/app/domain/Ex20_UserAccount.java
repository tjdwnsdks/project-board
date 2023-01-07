package com.bitstudy.app.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;


/** 회원 관리에 대한 부분. Auditing 까지 연결해서 쓸거임.
 * 
 * 이거 하고 dto > UserAccountDto 도 만들기
 * */


@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
//@Entity
public class Ex20_UserAccount extends AuditingFields {
    /* 새로 생성 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/* 이거 없앰*/
/** @Id
    @Column(length = 50)
    private String userId;*/

/* 새로 생성 - userId 를 일반 필드로 바꾸고 저 위에 id 필드에 @Id 붙임 */
    @Setter @Column(nullable = false, length = 50) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;


    protected Ex20_UserAccount() {}

    private Ex20_UserAccount(String userId, String userPassword, String email, String nickname, String memo
/* 이거 없앰, String createdBy*/) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
/* 이거 없앰*/ /*this.createdBy = createdBy;
        this.modifiedBy = createdBy;*/
    }


    public static Ex20_UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
/* 새로 추가 - new 키워드*/
        return new Ex20_UserAccount(userId, userPassword, email, nickname, memo /* 이거 없앰, null*/);
    }
/* 이거 없앰 */
    /**public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
        return new UserAccount(userId, userPassword, email, nickname, memo, createdBy);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ex20_UserAccount that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }

}
