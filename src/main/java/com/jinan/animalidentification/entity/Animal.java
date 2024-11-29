package com.jinan.animalidentification.entity;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Animal {
    // 属性
    private Map<String, Boolean> attributes;

    public Animal() {
        attributes = new HashMap<>();
    }

    // 设置属性
    public void setAttribute(String attribute, Boolean value) {
        attributes.put(attribute, value);
    }

    // 获取属性值
    public Boolean getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    // 获取所有属性
    public Map<String, Boolean> getAttributes() {
        return attributes;
    }
}
