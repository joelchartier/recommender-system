package com.jchartier.recommendersystem.secondassignment;

import com.jchartier.recommendersystem.secondassignment.dto.Attribute;
import com.jchartier.recommendersystem.secondassignment.dto.Document;
import com.jchartier.recommendersystem.secondassignment.dto.User;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataExtractor {

    private static final String COMMA_SEPARATOR = ",";
    private static final String DOCS = "docs";

    public List<Document> extractDocuments(Stream<String> stream) {

        List<String> rows = stream.collect(Collectors.toList());

        List<String> headers = rows.stream()
                .filter(line -> line.startsWith(DOCS))
                .map(value -> value.split(COMMA_SEPARATOR))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

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
                .collect(Collectors.toList());
    }

    public List<User> extractUsers(Stream<String> stream) {

        return null;
    }
}