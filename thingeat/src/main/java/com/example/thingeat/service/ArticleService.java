package com.example.thingeat.service;

import com.example.thingeat.dto.ArticleForm;
import com.example.thingeat.entity.Article;
import com.example.thingeat.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

// 서비스 선언! (서비스 객체를 스플이부트에 생성)
@Slf4j
@Service    // 해당 클래스를 서비스로 인식하여 스프링 부트에 객체를 생성(등록)
public class ArticleService {
    @Autowired  // DI
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if (article.getId() != null) {
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        // 1. 수정용 엔티티를 생성
        Article article = dto.toEntity();
        log.info("id:{}, article:{}", id, article.toString());

        // 2. 대상 엔티티 찾기
        Article target = articleRepository.findById(id).orElse(null);

        // 3. 잘못된 요청 처리(대상이 없거나, id가 다른 경우)
        if (target == null || id != article.getId()){
            log.info("잘못된 요청! id: {}, article: {}",id,article.toString());
            return null;
        }

        // 4. 업데이트 및 정상 응답 (200)
        target.patch(article);  // 추가된 코드
        Article updated = articleRepository.save(target); // 변경된 코드 (article -> target)
        return updated;
    }

    public Article delete(Long id) {
        // 1. 대상 엔티티 찾기
        Article target = articleRepository.findById(id).orElse(null);
        // 2. 잘못된 요청 처리(대상이 없는 경우)
        if(target == null){
            return null;
        }
        // 3. 대상 삭제 후 응답 반환
        articleRepository.delete(target);
        return target;
    }

    @Transactional // 해당 메소드를 트랜잭션으로 묶는다!
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // dto 묶음을 entity 묶음으로 변환
        List<Article> articleList = dtos.stream()
                .map(dto->dto.toEntity())
                .collect(Collectors.toList());

        // entity 묶음을 DB로 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        // 강제 예외 발생 (id 값은 -가 될수 x)
        articleRepository.findById(-1L).orElseThrow(
                ()-> new IllegalArgumentException("결제 실패")
        );

        // 결과값 반환
        return articleList;
    }
}
