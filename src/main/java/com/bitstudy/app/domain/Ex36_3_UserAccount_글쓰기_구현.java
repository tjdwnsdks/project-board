package com.bitstudy.app.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;


@Getter
@ToString(callSuper = true)
@Table(indexes = {
        /*이거 삭제*///@Index(columnList = "userId", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Ex36_3_UserAccount_글쓰기_구현 extends AuditingFields {
    /*이거 삭제*///
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    /* 새로 생성 */
    @Id
    @Column(length = 50)
    private String userId;
    /*이거 삭제*///@Setter @Column(nullable = false, length = 50) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;


    protected Ex36_3_UserAccount_글쓰기_구현() {}

    private Ex36_3_UserAccount_글쓰기_구현(String userId, String userPassword, String email, String nickname, String memo
            /* 이거 없앰, String createdBy*/) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        /* 이거 없앰*/ /*this.createdBy = createdBy;
        this.modifiedBy = createdBy;*/
    }


    public static Ex36_3_UserAccount_글쓰기_구현 of(String userId, String userPassword, String email, String nickname, String memo) {
        /* 새로 추가 - new 키워드*/
        return new Ex36_3_UserAccount_글쓰기_구현(userId, userPassword, email, nickname, memo /* 이거 없앰, null*/);
    }
    /* 이거 없앰 */
    /**public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
     return new UserAccount(userId, userPassword, email, nickname, memo, createdBy);
     }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ex36_3_UserAccount_글쓰기_구현 that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }

}
