package com.pfm.category.utilty;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TransactionDescriptionParser {

    private static final String CARD_NUMBER_PATTERN = "\\b(\\w{4}-\\w{4}-\\w{4}-\\w{4})\\b";
    private static final String DATE_PATTERN = "\\b(\\d{4}\\.\\d{2}\\.\\d{2})\\b";
    private static final String TRANSACTION_TYPE_PATTERN = "(CUMPARARE PRIN POS|POS purchase|RETRAGERI DE NUMERAR DE LA ATM-UL BANCII|All transactions)";
    private static final String AUTH_CODE_PATTERN = "Auth code (\\d+)";
    private static final String AMOUNT_AND_OPTIONAL_EXCHANGE_PATTERN = "([\\d,.]+\\s+\\w{3})(?:\\s+([\\d,.]+\\s+\\w{3})\\s+@([\\d,.]+)\\s+\\w{3}-\\w{3})?";
    private static final String KEYWORDS_PATTERN = "(.*?)"; // Non-greedy match

    private static final Pattern TRANSACTION_PATTERN = Pattern.compile(
            CARD_NUMBER_PATTERN + "\\s+" +
                    DATE_PATTERN + "\\s+" +
                    KEYWORDS_PATTERN + "\\s+" +
                    TRANSACTION_TYPE_PATTERN + "\\s+" +
                    AUTH_CODE_PATTERN + "\\s+" +
                    AMOUNT_AND_OPTIONAL_EXCHANGE_PATTERN,
            Pattern.CASE_INSENSITIVE);

    public TransactionParts parse(String description) {
        Matcher matcher = TRANSACTION_PATTERN.matcher(description);
        if (matcher.find()) {
            TransactionParts parts = new TransactionParts(
                    matcher.group(1), // Card Number
                    matcher.group(2), // Date
                    matcher.group(3).trim(), // Keywords
                    matcher.group(4), // Transaction Type
                    matcher.group(5), // Auth Code
                    matcher.group(6) // Initial Amount and Currency
            );

            if (matcher.group(7) != null && matcher.group(8) != null) {
                String secondaryAmount = matcher.group(7);
                String exchangeRate = matcher.group(8);

                parts.setSecondaryAmount(secondaryAmount);
                parts.setExchangeRate(exchangeRate);
            }

            return parts;
        } else {
            System.out.println("No match found.");
            return null;
        }
    }




    @Data
    public static class TransactionParts {
        private String cardNumber;
        private String date;
        private String keywords;
        private String transactionType;
        private String authCode;
        private String amount;
        private String secondaryAmount;
        private String exchangeRate;

        public TransactionParts(String cardNumber, String date, String keywords, String transactionType, String authCode, String amount) {
            this.cardNumber = cardNumber;
            this.date = date;
            this.keywords = keywords;
            this.transactionType = transactionType;
            this.authCode = authCode;
            this.amount = amount;
            this.secondaryAmount = null;
            this.exchangeRate = null;
        }
    }
}
