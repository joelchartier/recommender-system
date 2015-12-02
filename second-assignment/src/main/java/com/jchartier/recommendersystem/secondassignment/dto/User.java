package com.jchartier.recommendersystem.secondassignment.dto;

import java.util.List;

public class User {

    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
