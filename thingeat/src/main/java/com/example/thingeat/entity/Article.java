package com.example.thingeat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity //DB가 해당 객체를 인식 가능! (해당 클래스로 데이터를 만든다!)
@AllArgsConstructor
@NoArgsConstructor  //디폴트 생성자를 추가!
@ToString
@Getter
public class Article {

    @Id //대표값
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 id를 자동 생성 이노테이션!
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    public void patch(Article article) {
        if(article.title != null)
            this.title = article.title;
        if(article.content != null)
            this.content = article.content;
    }
}
