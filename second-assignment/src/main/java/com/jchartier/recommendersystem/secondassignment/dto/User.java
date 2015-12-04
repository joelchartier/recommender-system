package com.jchartier.recommendersystem.secondassignment.dto;

import java.util.Map;

public class User {

    private String name;
    private Map<String, Document> documents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, Document> documents) {
        this.documents = documents;
    }
}
