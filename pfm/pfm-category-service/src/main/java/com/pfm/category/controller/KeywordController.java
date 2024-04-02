package com.pfm.category.controller;

import com.pfm.category.service.KeywordService;
import com.pfm.category.utilty.TransactionDescriptionParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/keywords")
public class KeywordController {
    private final KeywordService keywordService;
    private final TransactionDescriptionParser parser;


    public KeywordController(KeywordService keywordService, TransactionDescriptionParser parser) {
        this.keywordService = keywordService;
        this.parser = parser;
    }


}
