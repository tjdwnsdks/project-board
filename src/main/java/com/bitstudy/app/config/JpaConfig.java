package com.bitstudy.app.config;

import com.bitstudy.app.dto.serucity.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


/* 할일: 이전에 만들때 맨 아래에 TODO 를 걸어놨었다.
    그떄 만들때는 당장 인증기능이 없었어서 사람이름 써야할때 정보가 없으면 auditing 으로 일단 무조건 'bitstudy' 라는게 나가게 했었다.
    그런데 이제 인증기능을 만들었으니까 인증된 실제 사용자로부터 사용자 정보를 받을수 있게 됐다.
    그래서 그 부분을 구현할거다.
* */

@EnableJpaAuditing /**  JPA 에서 auditing 을 가능하게 하는 어노테이션
 jpa auditing 이란:
 Audit은 감시하다, 감사하다라는 뜻으로 Spring Data JPA에서 자동으로 값을 넣어주는 기능.
 간단히 말해서, jpa 에서 자동으로 셋팅하게 해줄때 사용하는 기능인데, 해당 데이터를 보고 있다가 생성 또는 수정이 발생하면 자동으로 값을 넣어주는 편리한 기능.
 이거 하려면 config 파일이 별도로 있어야 한다.
 config 패키지 만들고 JpaConfig 클래스 만들자 */
@Configuration
/** @Configuration이라고 하면 설정파일을 만들기 위한 애노테이션 or Bean을 등록하기 위한 애노테이션이다.
이거 쓰면 JpaConfig 클래스는 Configuration bean 이 된다.
 @Configuration 애노테이션을 사용하면 가시적으로 설정파일이야 ~ , Bean 등록할꺼야 ~ 라는 뜻임*/
public class JpaConfig {


    // 사람 이름 정보를 넣어주기 위한 config 를 잡아줄거임
    @Bean
    public AuditorAware<String> auditorAware() {
        /* 이거 삭제 */ // return () -> Optional.of("bitstudy");
        // 이렇게 하면 앞으로 JPA Auditing 할때마다 사람이름은 이걸로 넣게 된다.
        // TODO: 나중에 스프링 시큐리티로 인증 기능 붙일때 수정할것


        /* 새로 생성*/
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                /* 해설:  ofNullable: 일반 객체뿐만 아니라 null값까지 입력으로 받을 수 있다는 뜻. 인증이 안됐을수도 있으니까
                 *       SecurityContextHolder: 모든 인증정보를 가지고 있음
                 *       getContext(): SecurityContextHolder 안에서 시큐리티 컨텍스트를 가져온다.(컨텍스트란 애플리케이션 환경에 대한 인터페이스 이자, 추상 클래스 이다. 즉, 애플리케이션의 현재 상태 를 뜻한다) */
                .map(SecurityContext::getAuthentication)
                /* SecurityContext 는 org.springframework.security.core.context 사용
                 * getAuthentication: 인증 정보를 불러온다. Authentication(인증), Authorization(권한) 이라는 뜻 */
                .filter(Authentication::isAuthenticated) /* isAuthenticated: filter를 이용해서 인증이 됐는지 확인 하는 뜻. 로그인 한 상태인지 보라는거임.  */
                .map(Authentication::getPrincipal) /* Authentication 안에 Principal 이라는 인증정보 가져오라는 뜻 */
                .map(BoardPrincipal.class::cast) /* 아까 만든 BoardPrincipal class 를 불러온다.  */
                .map(BoardPrincipal::getUsername); /* BoardPrincipal 에서 인증받은 유저 이름을 가져온다. */
    }

/* 다 했으면

    test >  config 폴더 생성  > TestSecurityConfig.java 만들기  
    
    
    JpaConfig.java  ㄱㄱ


인증정보를 이용해서 만들어놓은 기능들을 사용할 수 있게 바꿔줘야 한다.
* 
* JpaReposityroTest ㄱㄱ
*  */


}


