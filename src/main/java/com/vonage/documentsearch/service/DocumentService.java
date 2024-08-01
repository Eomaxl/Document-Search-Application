package com.vonage.documentsearch.service;

import com.vonage.documentsearch.model.Document;
import com.vonage.documentsearch.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void indexDocumentsFromDirectory(String directoryPath) throws IOException {
        logger.info("Indexing documents from directory: {}", directoryPath);

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            List<Document> documents = paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            String content = new String(Files.readAllBytes(path));
                            return new Document(path.getFileName().toString(), content);
                        } catch (IOException e) {
                            logger.error("Error reading file: {}", path, e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            documentRepository.indexDocuments(documents);
            logger.info("Indexed {} documents", documents.size());
        } catch (IOException e) {
            logger.error("Error indexing documents from directory: {}", directoryPath, e);
            throw e;
        }
    }

    public Map<String, BigDecimal> searchDocuments(String query) {
        logger.info("Searching documents with query: {}", query);
        return documentRepository.searchDocuments(query);
    }
}
