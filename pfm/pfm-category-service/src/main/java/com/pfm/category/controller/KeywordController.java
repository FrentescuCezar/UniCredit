package com.pfm.category.controller;

import com.pfm.category.service.KeywordService;
import com.pfm.category.utilty.TransactionDescriptionParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/parse-transaction")
    public ResponseEntity<TransactionDescriptionParser.TransactionParts> parseTransaction(@RequestBody String description) {
        TransactionDescriptionParser.TransactionParts transactionParts = parser.parse(description);
        if (transactionParts != null) {
            return ResponseEntity.ok(transactionParts);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
