package com.jchartier.recommendersystem.secondassignment;

import com.jchartier.recommendersystem.secondassignment.dto.Document;
import com.jchartier.recommendersystem.secondassignment.dto.User;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Runner implements Runnable {

    private DataExtractor dataExtractor = new DataExtractor();

    private String documentsFileName;
    private String usersFileName;

    public Runner(String documentsFileName,
                  String usersFileName) {

        this.documentsFileName = documentsFileName;
        this.usersFileName = usersFileName;
    }

    public void run() {

        Optional<Stream<String>> optionalDocumentStream = Optional.empty();
        Optional<Stream<String>> optionalUserStream = Optional.empty();

        try {

            optionalDocumentStream = Optional.of(Files.lines(Paths.get(documentsFileName)));
            optionalDocumentStream.ifPresent(stream -> {

                List<Document> documents = dataExtractor.extractDocuments(stream);
            });

            optionalUserStream = Optional.of(Files.lines(Paths.get(usersFileName)));
            optionalUserStream.ifPresent(stream -> {

                List<User> users = dataExtractor.extractUsers(stream);
            });

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            optionalDocumentStream.ifPresent(Stream::close);
            optionalUserStream.ifPresent(Stream::close);
        }
    }
}