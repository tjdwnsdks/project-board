package com.bitstudy.app.config;


import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.dto.serucity.BoardPrincipal;
import com.bitstudy.app.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** - 할일 -
 지난시간에 댓글 저장, 삭제까지 구현했지만 현재는 아무나 내 댓글을 지울수 있다. 게시글도 마찬가지다. 그래서 인증기능을 추가해서 작성자만 삭제할 수 있도록 만들어야 한다.
 이전시간에 시큐리티로 로그인화면 만들때 무조건 로그인 페이지만 보여지게 되서 모든 요청을 허용한적이 있다. 그 부분을 이제 원래의 방식으로 돌려주자. */


//@EnableWebSecurity // 블로그나 책에 이 스프링 시큐리티 쓸땐 무조건 이거 넣으라고 하는데, 이제 안넣어도 된다. 스프링부트에서 시큐리티 쓸때는 autoConfigration에 들어가져 있다.
@Configuration // 설정파일로 등록
public class Ex38_1_SecurityConfig_인증 {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /** 각 파트 설명
         - HttpSecurity: 세부적인 보안 기능을 설정할 수 있는 API 제공함.
         ✔️ 리소스(URL) 접근 권한 설정
         ✔️ 커스텀 로그인 페이지 지원
         ✔️ 인증 후 성공/실패 핸들링
         ✔️ 사용자 로그아웃
         ✔️ CSRF 공격으로 부터 보호
         * */

        /* 이거 삭제 */
        /** return http
         .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // HttpSecurity 의 authorizeHttpRequests 에서 모든 요청이 인증을 거쳐야 함을 설정해준다. 풀어서 말하자면 모든 요청에 대한 인증을(auth.anyRequest()) 허용(통과)하겠다 (permitAll()) 라는 뜻임
         .formLogin().and() // formLogin() : 로그인 페이지를 만들어준다.
         // .and() 는 앞에꺼 하고 and() 다음꺼 해 라는 의미임
         .build(); // 빌드 해라*/
        /* 새로 추가 */
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(  /* mvcMatchers: 스프링에 패턴 매칭 기능이 들어간 메서드. 컨트롤러에서 맵핑할때 "/articles/** /form" 이런식으로 경로 설정할때도 있는데 그런 특정 경로를 지정해서 권한을 설정할 수도 있게 하는 메서드다. */
                                HttpMethod.GET, /* 특정 경로를 지정 하는 부분임. - GET 방식, 루트페이지, 게시판리스트 페이지 는  */
                                "/",
                                "/articles"
                        ).permitAll()  /* 권한을 허용하고 */
                        .anyRequest().authenticated() /* 그 외의 경로로 접근할때는 어떤 요청이든 authentication(인가) 과정을 거치도록 한다 */
                )
                .formLogin().and() // formLogin() : 로그인 페이지를 만들어준다.
                .logout()
                .logoutSuccessUrl("/") /* 로그아웃을 성공하면 "/" 이 경로로 이동해라 라는뜻*/
                .and()
                .build();
/////////////////////////////////////////////////////////////////////////////////////////////////////////
        /** formLogin() 이후 사용할 수 있는 메서드 설명
         http.formLogin()
         .loginPage("/login.html")				// 사용자 정의 로그인 페이지
         .defaultSuccessUrl("/home")				// 로그인 성공 후 이동 페이지
         .failureUrl("/login.html?error=true")		// 로그인 실패 후 이동 페이지
         .usernameParameter("username")			// 아이디 파라미터명 설정
         .passwordParameter("password")			// 패스워드 파라미터명 설정
         .loginProcessingUrl("/login")			// 로그인 Form Action Url
         .successHandler(loginSuccessHandler())		// 로그인 성공 후 핸들러
         .failureHandler(loginFailureHandler())		// 로그인 실패 후 핸들러
         */
    }

/* 새로 추가 - securityFilterChain() 메서드이 requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() 부분이 이 메서드를 대신함 */
//    @Configuration
//    public class SecurityConfiguration {
//        @Bean
//        public WebSecurityCustomizer webSecurityCustomizer() {
//            return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
///* 해설: 저 위에 securityFilterChain() 메서드는 스프링시큐리티를 이용해서 스프링 시큐리티 관리 하에 인증과권한 체크를 하는 부분인데,
//  ignoring() 메서드는 아예 스프링 시큐리티 검사에서 제외하겠다 라는 뜻임.
//  공식문서대로 하게되면 antMatchers(); 를 써서 () 안에 별도의 파일 경로를 다 잡아줬어야 하는데 유지보수할때 손이 많이 간다. 그래서 스프링에서 지원하는 PathRequest.toStaticResources().atCommonLocations()를 이용해서 기본적인 정적파일들을 다 잡아버리면 된다.
//  atCommonLocations 에서 ctrl+B 해서 들어가면 allOf(StaticResourceLocation.class) 있는데 그거 또 Ctrl + B 해보면 어떤게 걸려있는지 확인할 수 있다.
//    아무튼 이걸 사용해서 인증기능을 추가할 필요가 없는 정적 리소스들을 대상을 선택해서 설정하는 방식이다 */
//        }
//    }

    /* 사용자 정보 가져오는 부분 - 실제 인증 데이터를 가져오는 서비스 로직을 가져온다. */
    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) { /* DB에 있는 user 정보를 주입 받아야 하니까 매개변수로 userAccountRepository 를 받는다.  */
        return username -> userAccountRepository
                .findById(username)/* username 으로 유저 한명 찾고*/
                .map(UserAccountDto::from)/* 맵핑해서 username 으로 찾은 결과를 dto 로 바꿈 */
                .map(BoardPrincipal::from) /* BoardPrincipal: 사용자 정보를 가져올 수 있는 record. userDetail 을 받아옴 */
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username)); /* 혹시라도 인증된 사용자를 못찾을때에 대한 대안으로 UsernameNotFoundException 사용함.*/
    }

/*추가 - 지금 바로는 안씀. 일단 가지고 있기. 패스워드 인코더임.  */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

/* 다 하면 브라우저에서 실행해보기. 
 
 주의: 게시판 페이지 들어가는 주소는 괜찮음. 
    다만 거기서 글을 눌렀을때 로그인 화면 나와야 성공하는거임.

 다 하면 Ex38_3_JpaConfig 가기
    */
