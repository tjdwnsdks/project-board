package com.bitstudy.app.service;

import com.bitstudy.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/* Ex11_ArticleServiceTest 하고 오기 */
@RequiredArgsConstructor // 필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 애너테이션

@Service // 이렇게 하면 서비스 빈으로 등록되어서 사용할 수 있게 된다.
public class ArticleService {
    /* 이거까지만 하고 TDD 만들자.
        1) 우클릭 > Go To > Test
         또는
        2) Ctrl + Shift + T
    */



    /* Ex11_ArticleServiceTest 하고 오기 */
    private final ArticleRepository articleRepository; // 아티클 서비스는 당연히 ArticleRepository 쓸거다.
    // 댓글도 여기 넣으면 좋겠지만, 댓글은 게시글 자체에서 다루게 하는게 더 좋을것 같아서 여기서는 취급하지 않는다.
    /* 다시 TDD (Ex11_ArticleServiceTest) 로 돌아가기 */
}
