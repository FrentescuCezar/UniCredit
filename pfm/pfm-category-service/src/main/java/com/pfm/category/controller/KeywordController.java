package com.pfm.category.controller;

import com.pfm.category.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController {
    private final KeywordService keywordService;

    @GetMapping("/filter/{keyword}")
    public ArrayList<String> filterKeywords(@PathVariable String keyword) {
        return keywordService.filterKeywords(keyword);
    }
}
