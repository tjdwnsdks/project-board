package com.bitstudy.app.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

/* 그냥 복붙하면
* @Bean 영역의 thymeleaf3Properties 와
* @ConstructorBinding 영역의 @ConfigurationProperties 부분에 빨간 밑줄이 생긴다.
* 이유는 ConfigurationProperties 를 유져가 직접 만드는 경우에는 반드시 스캔을 해줘야 한다.
* 그래서 메인클래스(ProjectBoardApplication)에 가서 @ConfigurationPropertiesScan 를 달아줘야 한다.
* 그럼 빨간줄 사라짐
* */
@Configuration
public class ThymeleafConfig {


    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties

            /** thymeleafTemplateResolver 라는 빈을 등록하는데 리턴타입은 SpringResourceTemplateResolver 다.
             * 그런데 이게 타임리프 auto-configration 을 불렀을때. 즉, 타임리프를 스프링부트 프로젝트에 넣었을때 auto-configration 가 자동으로 잡힐거다.
             * */
    ) {

        /** 그런데 DeCoupledLogic 을 세팅하는 건 (setUseDecoupledLogic) 이미 만들어져 있다. 그런데 외부 프로퍼티이기 때문에 인식을 못할 뿐이다.
         * application.yaml 파일에 가서 맨 아래 thymeleat 를 등록하자.  */
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }


    @RequiredArgsConstructor // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션
    @Getter
    @ConstructorBinding
    @ConfigurationProperties("spring.thymeleaf3") /* prefix 로 이름을 thymeleaf3 로 설정함.(나중에 application.yaml 가서 사용할거임) */
    public static class Thymeleaf3Properties {
        /**
         * Use Thymeleaf 3 Decoupled Logic
         */
        private final boolean decoupledLogic;



        /* 이건 생성자 인데 위에 @RequiredArgsConstructor 로 대체 할 수 있다. (Lombok 임)  */
//        public Thymeleaf3Properties(boolean decoupledLogic) {
//            this.decoupledLogic = decoupledLogic;
//        }
        
        /* 이건 getter 의 역할을 하는거 - @Getter 로 대체 가능 */
//        public boolean isDecoupledLogic() {
//            return this.decoupledLogic;
//        }
    }

    /* 다 하면 application.yaml 맨 아래쪽으로 가기 */
    /* 다 하면 application.yaml 맨 아래쪽으로 가기 */
}