# Document Search Application

## Overview

This Spring Boot application provides an API to index documents from a directory and search for terms within those documents. It supports:

- Indexing documents from a given directory.
- Searching for a term or phrase within the indexed documents.
- Returning the top 10 matching filenames along with their rank scores.

1. **Index Documents**: `POST /api/documents/index?directoryPath={directoryPath}`
2. **Search Documents**: `GET /api/documents/search?query={query}`
3. **Health Check**: `GET /api/documents/health`

## Ranking

- **100%** if a file contains all the words in the query.
- **0%** if it contains none of the words.
- Between 0 and 100% if it contains only some of the words.


## Running the Application

1. **Build and Run**:

    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

### Post Request to index documents
POST /api/documents/index
Content-Type: application/json

{
"directoryPath": "/path/to/local/directory"
}

### GET Request to search documents
GET /api/documents/search?query=example