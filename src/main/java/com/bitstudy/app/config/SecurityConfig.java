package com.bitstudy.app.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** spring security 를 설치하고, 실행하면 어떤 url을 넣던 무조건 로그인 화면으로 넘어간다.
 *  아직 로그인 기능 없으니까 당장은 url에 있는 주소대로 화면이 나타나게 할거다.
 *  그렇다고 login 이 없어지는건 아니다. localhost:8080/login 넣으면 로그인 화면 동작한다.
 *
 *  스프링 시큐리티를 수업으로 설명할수는 없다. 이 파트는 일단 가져다 쓰기만 하고
 * */
//@EnableWebSecurity // 블로그나 책에 이 스프링 시큐리티 쓸땐 무조건 이거 넣으라고 하는데, 이제 안넣어도 된다. 스프링부트에서 시큐리티 쓸때는 autoConfigration에 들어가져 있다.
@Configuration // 설정파일로 등록
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* 각 파트 설명
        *   - HttpSecurity: 세부적인 보안 기능을 설정할 수 있는 API 제공함.
                            ✔️ 리소스(URL) 접근 권한 설정
                            ✔️ 커스텀 로그인 페이지 지원
                            ✔️ 인증 후 성공/실패 핸들링
                            ✔️ 사용자 로그아웃
                            ✔️ CSRF 공격으로 부터 보호
        * */


        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // HttpSecurity 의 authorizeHttpRequests 에서 모든 요청이 인증을 거쳐야 함을 설정해준다
                .formLogin().and() // formLogin() : 로그인 페이지를 만들어준다. 
                                   // .and() 는 앞에꺼 하고 and() 다음꺼 해 라는 의미임 
                .build(); // 빌드 해라


/* 다 쓰면 워드로 돌아가기.  (브라우저에 localhost:8080/articles 쳐보자.)

    확인 하고 TDD 폴더 > controller > 'Ex10_1_ArticleControllerTest_스프링시큐리티' 로 가기
* */



         
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


}
