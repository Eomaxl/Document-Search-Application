package com.vonage.documentsearch.dto;

import java.math.BigDecimal;
import java.util.Map;

public class SearchResponse {

    private Map<String, BigDecimal> results;

    public SearchResponse() {}

    public SearchResponse(Map<String, BigDecimal> results) {
        this.results = results;
    }

    public Map<String, BigDecimal> getResults() {
        return results;
    }

    public void setResults(Map<String, BigDecimal> results) {
        this.results = results;
    }
}
