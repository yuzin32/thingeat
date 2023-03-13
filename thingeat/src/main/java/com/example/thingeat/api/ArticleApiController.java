package com.example.thingeat.api;

import com.example.thingeat.entity.Article;
import com.example.thingeat.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ArticleApiController {
    @Autowired  // DI : 외부에서 가져온다는 뜻 (생성 객체를 가져와 연결!)
    private ArticleService articleService;

    // GET
    @GetMapping("/api/recipe")
    public List<Article> index(){
        return articleService.index();
    }
    @GetMapping("/api/recipe/{id}")
    public Article show(@PathVariable Long id){
        return articleService.show(id);
    }

    // POST
    @PostMapping("/api/recipe")
    public ResponseEntity<Article> create(@RequestBody com.example.thingeat.dto.ArticleForm dto){
        Article created = articleService.create(dto);
        return (created != null) ?
                ResponseEntity.status(HttpStatus.OK).body(created):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // PATCH
    @PatchMapping("/api/recipe/{id}")
    public ResponseEntity<Article> update (@PathVariable Long id,
                                           @RequestBody com.example.thingeat.dto.ArticleForm dto){
        Article updated = articleService.update(id, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // DELETE
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id){
        Article deleted = articleService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 트랜잭션 -> 실패 -> 롤백 !
    @PostMapping("/api/transaction-test")
    public ResponseEntity<List<Article>> trasactionTest(@RequestBody List<com.example.thingeat.dto.ArticleForm> dtos){
        List<Article> createdList= articleService.createArticles(dtos);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

