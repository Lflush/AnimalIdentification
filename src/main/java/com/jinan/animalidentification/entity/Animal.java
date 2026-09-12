package com.jinan.animalidentification.entity;

import java.util.HashMap;
import java.util.Map;

public class Animal {
    private Map<String, Boolean> attributes;

    public Animal() {
        attributes = new HashMap<>();
    }

    public void setAttribute(String attribute, Boolean value) {
        attributes.put(attribute, value);
    }

    public Boolean getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    public Map<String, Boolean> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Boolean> attributes) {
        this.attributes = attributes == null ? new HashMap<String, Boolean>() : attributes;
    }
}
