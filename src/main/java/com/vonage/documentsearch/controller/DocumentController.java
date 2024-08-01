package com.vonage.documentsearch.controller;

import com.vonage.documentsearch.dto.IndexRequest;
import com.vonage.documentsearch.dto.SearchResponse;
import com.vonage.documentsearch.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/index")
    public String indexDocumentsFromDirectory(@RequestBody IndexRequest indexRequest) {
        try {
            documentService.indexDocumentsFromDirectory(indexRequest.getDirectoryPath());
            return "Indexing completed";
        } catch (IOException e) {
            logger.error("Error indexing documents from directory: {}", indexRequest.getDirectoryPath(), e);
            return "Error indexing documents";
        }
    }

    @GetMapping("/search")
    public SearchResponse searchDocuments(@RequestParam String query) {
        Map<String, BigDecimal> results = documentService.searchDocuments(query);
        return new SearchResponse(results);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Service is up and running!";
    }
}
