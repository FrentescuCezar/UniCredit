package com.pfm.category.service;

import com.pfm.category.exception.CategoryNotFoundException;
import com.pfm.category.service.dto.CategoryUpdateDTO;
import com.pfm.category.service.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionUpdateService {
    private final RestTemplate restTemplate;
    private final CategoryService categoryService;
    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    public TransactionDTO updateTransactionCategory(Long transactionId, Long categoryId) {
        if (!categoryService.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category with ID " + categoryId + " not found");
        }

        String url = transactionServiceUrl + "/api/transactions/" + transactionId + "/category";

        CategoryUpdateDTO categoryUpdateDTO = new CategoryUpdateDTO();
        categoryUpdateDTO.setCategoryId(categoryId);

        HttpEntity<CategoryUpdateDTO> requestEntity = new HttpEntity<>(categoryUpdateDTO);
        ResponseEntity<TransactionDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                TransactionDTO.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to update transaction category. Status code: " + response.getStatusCode());
        }

        return response.getBody();
    }

    public List<TransactionDTO> splitTransaction(Long parentId, List<TransactionDTO> splitTransactions) {
        splitTransactions.forEach(tr -> tr.setParentId(parentId));

        String url = transactionServiceUrl + "/api/transactions/bulk";
        HttpEntity<List<TransactionDTO>> requestEntity = new HttpEntity<>(splitTransactions);
        ResponseEntity<List<TransactionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<TransactionDTO>>() {
                }
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to split transaction. Status code: " + response.getStatusCode());
        }

        return response.getBody();
    }


}