package com.jchartier.recommendersystem.secondassignment;

import com.jchartier.recommendersystem.secondassignment.dto.Attribute;
import com.jchartier.recommendersystem.secondassignment.dto.Document;
import com.jchartier.recommendersystem.secondassignment.dto.User;
import org.w3c.dom.Attr;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Runner implements Runnable {

    private DataExtractor dataExtractor = new DataExtractor();

    private String documentsFileName;
    private String usersFileName;
    private Map<String, Document> documents;

    public Runner(String documentsFileName,
                  String usersFileName) {

        this.documentsFileName = documentsFileName;
        this.usersFileName = usersFileName;
    }

    public Map<String, Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, Document> documents) {
        this.documents = documents;
    }

    public void run() {

        Optional<Stream<String>> optionalDocumentStream = Optional.empty();
        Optional<Stream<String>> optionalUserStream = Optional.empty();

        try {

            optionalDocumentStream = Optional.of(Files.lines(Paths.get(documentsFileName)));
            optionalDocumentStream.ifPresent(stream -> this.setDocuments(dataExtractor.extractDocuments(stream)));

            optionalUserStream = Optional.of(Files.lines(Paths.get(usersFileName)));
            optionalUserStream.ifPresent(stream -> {

                List<User> users = dataExtractor.extractUsers(stream);

                evaluateBestDocumentForUser(users.get(0), this.getDocuments());
            });

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            optionalDocumentStream.ifPresent(Stream::close);
            optionalUserStream.ifPresent(Stream::close);
        }
    }

    private void evaluateBestDocumentForUser(User user, Map<String, Document> documents) {

        Map<String, Integer> attributeLikeliness = buildAttributeLikeliness(user, documents);
    }

    private Map<String, Integer> buildAttributeLikeliness(User user, Map<String, Document> documents) {

        Map<String, Integer> attributeLikeliness = new HashMap<>();

        Consumer<Document> documentConsumer = document -> document
                .getAttributes().stream()
                .filter(attribute -> attribute.getValue().equals(1))
                .forEach(attribute -> {

                    // TODO: This has side-effect, should be refactored
                    if (!attributeLikeliness.containsKey(attribute.getName())) {

                        attributeLikeliness.put(attribute.getName(), document.getValue());
                    } else {

                        attributeLikeliness.put(attribute.getName(), attributeLikeliness.get(attribute.getName()) + document.getValue());
                    }
                });

        appendAttributesToUserDocument(user, documents);

        user.getDocuments().values().stream()
                .forEach(documentConsumer);

        return attributeLikeliness;
    }

    private void appendAttributesToUserDocument(User user, Map<String, Document> documents) {

        user.getDocuments().values()
                .stream()
                .forEach(userDocument -> userDocument.setAttributes(documents.get(userDocument.getName()).getAttributes()));
    }
}