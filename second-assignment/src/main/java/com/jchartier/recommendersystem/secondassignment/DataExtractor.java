package com.jchartier.recommendersystem.secondassignment;

import com.jchartier.recommendersystem.secondassignment.dto.Attribute;
import com.jchartier.recommendersystem.secondassignment.dto.Document;
import com.jchartier.recommendersystem.secondassignment.dto.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataExtractor {

    private static final String COMMA_SEPARATOR = ",";
    private static final String EMPTY_VALUE = "-";
    private static final String DOCS = "docs";

    public Map<String, Document> extractDocuments(Stream<String> stream) {

        List<String> rows = stream.collect(Collectors.toList());

        List<String> headers = extractHeader(rows);

        Function<String, Document> documentBuilder = documentRow -> {

            List<String> splitDoc = Arrays.asList(documentRow.split(COMMA_SEPARATOR));

            Document document = new Document();
            document.setName(splitDoc.get(0));

            Function<String, Attribute> attributeBuilder = nameOfAttribute -> {

                Attribute attribute = new Attribute();
                attribute.setName(nameOfAttribute);
                attribute.setValue(Integer.parseInt(splitDoc.get(headers.indexOf(nameOfAttribute))));

                return attribute;
            };

            List<Attribute> attributes = headers.stream()
                    .filter(header -> !header.equals(DOCS))
                    .map(attributeBuilder)
                    .collect(Collectors.toList());

            document.setAttributes(attributes);

            return document;
        };

        return rows.stream()
                .filter(line -> !line.startsWith(DOCS))
                .map(documentBuilder)
                .collect(Collectors.toMap(Document::getName, document -> document));
    }

    public List<User> extractUsers(Stream<String> stream) {

        List<String> rows = stream.collect(Collectors.toList());

        List<String> headers = extractHeader(rows);

        Function<String, User> userBuilder = extractedUser -> {

            User user = new User();
            user.setName(extractedUser);

            Function<String, Document> documentBuilder = documentRow -> {

                List<String> splitDoc = Arrays.asList(documentRow.split(COMMA_SEPARATOR));

                Document document = new Document();
                document.setName(splitDoc.get(0));
                document.setValue(Integer.parseInt(splitDoc.get(headers.indexOf(extractedUser))));

                return document;
            };

            Map<String, Document> documents = rows
                    .stream()
                    .filter(line -> !line.startsWith(DOCS))
                    .filter(line -> {

                        try {
                            List<String> splitDoc = Arrays.asList(line.split(COMMA_SEPARATOR));
                            String valueOfDocForUser = splitDoc.get(headers.indexOf(extractedUser));

                            return !EMPTY_VALUE.equals(valueOfDocForUser);
                        } catch (IndexOutOfBoundsException ignored) {}

                        return false;
                    })
                    .map(documentBuilder)
                    .collect(Collectors.toMap(Document::getName, document -> document));

            user.setDocuments(documents);

            return user;
        };

        return headers.stream()
                .filter(header -> !header.equals(DOCS))
                .map(userBuilder)
                .collect(Collectors.toList());
    }

    private List<String> extractHeader(List<String> rows) {

        return rows.stream()
                .filter(line -> line.startsWith(DOCS))
                .map(value -> value.split(COMMA_SEPARATOR))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }
}