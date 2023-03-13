package com.example.thingeat.controller;

import com.example.thingeat.entity.Article;
import com.example.thingeat.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ArticleController {
    @Autowired //스프링 부트가 미리 생성해놓은 객체를 가져다가 연결
    private ArticleRepository articleRepository;

    @GetMapping("/help")
    public String helpArticle (Model model){
        return "articles/help";
    }
    @GetMapping("/join")
    public String joinArticle (Model model){
        return "articles/join";
    }
    @GetMapping("/login")
    public String loginArticle (Model model){
        return "articles/login";
    }
    @GetMapping("/id_find")
    public String id_findArticle (Model model){
        return "articles/id_find";
    }
    @GetMapping("/pw_find")
    public String pw_findArticle (Model model){
        return "articles/pw_find";
    }

    @GetMapping("/home")
    public String homeArticle (Model model){
        return "articles/home";
    }
    @GetMapping("/advice")
    public String adviceArticle (Model model){
        return "articles/advice";
    }
    @GetMapping("/disease")
    public String diseaseArticle (Model model){
        return "articles/disease";
    }

    @GetMapping("/recipe")
    public String index(Model model){
        //1: 모든 Article을 가져온다
        Iterable<Article> articleEntityList = articleRepository.findAll();

        //2: 가져온 Article 묶음을 뷰로 전달!
        model.addAttribute("articleList", articleEntityList);

        //3: 뷰 페이지를 설정!
        return "articles/recipe"; //articles/recipe.mustache
    }

    @GetMapping("/new")
    public String newArticleForm() {
        return "articles/new";
    }

    //@PostMapping("url") 이거 405 오류 뜨면 밑에꺼 써보기
    @RequestMapping(value = "/recipe/create")
    public String createArticle(com.example.thingeat.dto.ArticleForm form) {
        log.info(form.toString());
        //System.out.println(form.toString()); -> 로깅기능으로 대체!

        //1. DTO를 변환! Entity!
        Article article = form.toEntity();
        log.info(article.toString());
        //System.out.println(article.toString());

        //2. Repository에게 Entity를 DB안에 저장하게 함!
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
        //System.out.println(saved.toString());

        return "redirect:/recipe/"+saved.getId();
    }

    //url 입력 받기
    @GetMapping("/recipe/{id}")
    public String show(@PathVariable Long id, Model model) {
        log.info("id=" + id);

        //1: id로 데이터를 가져옴!
        Article articeEntity = articleRepository.findById(id).orElse(null);

        //2: 가져온 데이터를 모델에 등록!
        model.addAttribute("article", articeEntity);

        //3. 보여줄 페이지를 설정!
        return "articles/show";
    }

    @GetMapping("/recipe/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터를 가져오기!
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록!
        model.addAttribute("article", articleEntity);

        // 뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/recipe/update")
    public String update(com.example.thingeat.dto.ArticleForm form){
        log.info(form.toString());

        // 1. DTO를 엔티티로 변환한다!
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        // 2. 엔티티를 DB로 저장한다!
        // 2-1 : DB에서 기존 데이터를 가져온다!
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        // 2-2 : 기존 데이터에 값을 수정, 갱신한다!
        if (target != null){
            articleRepository.save(articleEntity);  //엔티티가 DB로 갱신
        }

        // 3. 수정 결과 페이지로 리다이렉트 한다!
        return "redirect:/recipe/"+articleEntity.getId();
    }

    @GetMapping("recipe/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어옴");

        // 1. 삭제 대상을 가져옴
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 2. 대상을 삭제 한다
        if (target != null){
            articleRepository.delete(target);
            //삭제가 된 경우
            rttr.addFlashAttribute("msg", "삭제가 왼료 되었습니다!");
        }

        // 3. 결과 페이지로 리다이렉트 한다
        return "redirect:/recipe";
    }
}