package com.bitstudy.app.repository;


/* 워드 "검색기능 추가하기" 에서 queryDSL 부분 끝내고 시작하는 코드.*/

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface Ex07_3_ArticleRepository_검색기능 extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>
        ,QuerydslBinderCustomizer<QArticle> // 이부분 처음에는 주석처리 하고 아래 빨간 '설명' 부분 설명하기
{
    /* 조심할것: QuerydslBinderCustomizer 는 QArticle 을 사용하는데 이건 build.grale 에서 quereyDsl 을 build 하고 와야 함. (한마디로 main 안에 generated 폴더 있어야 함) */

    /* 설명: QuerydslPredicateExecutor 는 기본적으로 Article 안에 있는 모든 필드에 대한 기본 검색 기능을 추가해준다. 사실 이거 하나만 넣어도 검색기능은 끝난다.
     - 간단히 테스트 해보기 위해서 
         1) QuerydslBinderCustomizer<QArticle> 이부분 주석처리 하고
         2) ArticleComment.java 가서도 똑같이 만들어준 다음에
         3) 서비스 올리고(projectBoardApplication 파일 실행하고)
         4) 빌드 하기 (Ctrl + F9)
         5) HAL 익스플로러 가서 아무 article 이나 열어서 title 부분 싹 다 복사한 다음
         6) Edit Headers 에 localhost:8080/api/articles?title=Donec vitae nisi. 같이 넣으면
            제대로 검색 되서 결과가 하나만 나온다. (생성한 DB에 같은 제목으로 있다면 걔네들 다 나올거임)
            * 부분검색은 안되고 정확한 검색만 되기 때문에 title 을 다 넣어줘야 한다.

        일단 이걸 구현해볼거다. 기본검색은 QuerydslPredicateExecutor 이거로 됐고
        추가적으로 우리 입맛에 맞는 검색기능을 만들기 위해서 QuerydslBinderCustomizer 를 추가하는거다.


    * */


    
    /** 아래꺼 Override 하는 방법
     * 1) Ctrl + o 눌러서 오버라이드 창 열고 
     * 2) 스크롤 내려보면 org.springframework.data.querydsl.binding.QueryBinding~  어쩌고 있음
     * 3) 그 안에 customize(bindings:QuerydslBindings, root T):void 있음 그거 선택하면 됨
     *  */
    @Override
    // 이거를 아래꺼로 바꿈
    //void customize(QuerydslBindings bindings, QArticle root);
    default void customize(QuerydslBindings bindings, QArticle root) {
    /**
        = customize 설명 =
            customize 안에 구현된 내용을 토대로 검색에 대한 세부적인 규칙이 다시 재구성 된다.
            근데 여긴 인터페이스라 원래는 구현을 할 수 없지만 java8 버전 이후로 가능해졌다.
            그래서 이걸 default 메소드로 바꿔줄거다

            왜냐면 우리는 리포지토리 레이어에서 직접 구현체를 만들지 않을거기 때문이다.
            스프링 데이터 JPA 를 이용해서 인터페이스만 가지고 기능을 사용하게끔 접근하고 있기 때문에 default 메소드를 사용해야 한다.
    */

        /* 1. 바인딩 하기
            현재 QuerydslPredicateExecutor 에 의해서 Article 에 있는 모든 필드에 대한 검색이 열려있는 상태이다. 그런데 우리가 원하는건 선택적인 필드들만 검색기능에서 사용하게 하고 싶다. (엑셀 api 명세서를 보면 '필터: 제목, 본문, id, 닉네임, 해시태그' 이렇게 되어 있다.)
             그래서 선택적으로 검색을 하게 하기 위해서 bindings.excludeUnlistedProperties 를 쓴다.
             이건 리스팅을 하지 않은 프로퍼티는 검색에서 어떻게 할래? 제외 할래 말래? 를 결정할 수 있는 메서드이다.
             true 하면 검색에서 제외, false 면 모든 프로퍼티를 열어주는거다. 그래서 우리는 true로 놓는다.
        * */
        bindings.excludeUnlistedProperties(true);

        /* 2. 이건 원하는 필드를 추가하는 부분
            including 을 이용해 title, content, hashtag, createdAt, createdBy 를 검색 가능하도록 할거다
            (엑셀 api 에 '필터: 제목, 본문, id, 닉네임, 해시태그' 이렇게 있으니깐. 그런데 id 는 아직 인증기능 달아서 유저 정보 알아올수 있을때 적용하자)
            사용법은 'root.필드명' 하면 됨
            *참고: content는 많은 데이터를 가지고 있을수 있기 때문에 속도 저하가 생길수 있다.
         */
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);

        /* 3. 아까 HAL 익스프로러에서 봤던것처럼 '정확한 검색'만 됐었는데 이걸 좀 바꿔볼거다.
                bind().first() 메서드를 써볼건데 한 필드당 하나씩 걸어줘야 한다.
        *  */
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // 이건 쿼리상 like '${문자열}' 로 들어감. // 검색어에다가 % 를 수동으로 따로 넣어줘야 한다. 이걸 가끔 쓰는 사람들이 있는데 그닥 사용하진 않고 containsIgnoreCase 를 왠만하면 쓰자
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // 이건 like '%${문자열}%'  로 들어감 // 검색어에다가 % 를 자동으로 넣어줌
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 이건 날짜니까 DatetimeExpression 으로 하고, eq 는 equals 의 의미. 날짜 필드 는 완벽히 같은것만 검색되도록 할거임. 근데 이렇게 하면 시분초 를 다 0 으로 인식하기 때문에 완벽하진 않다. 시분초 건드리는 부분은 별도로 할거다. 지금은 일단 맞는다는 가정하에 돌릴거다.
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }
    /* 여기까지 했으면 댓글쪽도 가서 완성하기 */
}
