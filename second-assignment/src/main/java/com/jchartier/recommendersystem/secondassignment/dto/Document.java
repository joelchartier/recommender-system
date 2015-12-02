package com.jchartier.recommendersystem.secondassignment.dto;

import java.util.ArrayList;
import java.util.List;

public class Document extends ValuedElement {

    private String name;
    private List<Attribute> attributes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
