package com.bitstudy.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing /*  JPA 에서 auditing 을 가능하게 하는 어노테이션
                        jpa auditing 이란:  jpa 에서 자동으로 셋팅하게 해줄때 사용하는 기능
                        이거 하려면 config 파일이 별도로 있어야 한다.
                        config 패키지 만들고 JpaConfig 클래스 만들자 */
@Configuration
/* @Configuration이라고 하면 설정파일을 만들기 위한 애노테이션 or Bean을 등록하기 위한 애노테이션이다.
이거 쓰면 JpaConfig 클래스는 Configuration bean 이 된다.
@Configuration 애노테이션을 사용하면 가시적으로 설정파일이야 ~ , Bean 등록할꺼야 ~ 라는 뜻임*/
public class JpaConfig {
    
/* Ex01_Article_ 꺼임 */
    // 사람 이름 정보를 넣어주기 위한 config 를 잡아줄거임
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("bitstudy");
        // 이렇게 하면 앞으로 JPA Auditing 할때마다 사람이름은 이걸로 넣게 된다.
        // TODO: 나중에 스프링 시큐리티로 인증 기능 붙일때 수정할것
    }



}
