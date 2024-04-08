package com.pfm.category.service;


import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordService {
    public List<String> filterKeywords(List<String> keywords) {
        Set<String> filterWords = new HashSet<>(Arrays.asList("srl", "sa", "romania", "sc", "impex"));
        return keywords.stream()
                .filter(keyword -> !filterWords.contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

}
