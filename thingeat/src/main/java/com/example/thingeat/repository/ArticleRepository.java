package com.example.thingeat.repository;

import com.example.thingeat.entity.Article;
import com.example.thingeat.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Override
    ArrayList<Article> findAll();
}