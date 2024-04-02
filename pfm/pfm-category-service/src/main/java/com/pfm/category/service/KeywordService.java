package com.pfm.category.service;

import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.utilty.TransactionDescriptionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final TransactionDescriptionParser parser;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository, TransactionDescriptionParser parser) {
        this.keywordRepository = keywordRepository;
        this.parser = parser;
    }

    public Optional<String> parseTransactionDescription(String description) {
        return parser.parse(description)
                .map(TransactionDescriptionParser.TransactionParts::getKeywords);    }

    public List<String> filterKeywords(List<String> keywords) {
        Set<String> filterWords = new HashSet<>(Arrays.asList("srl", "sa", "romania", "sc", "impex"));
        return keywords.stream()
                .filter(keyword -> !filterWords.contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

}
