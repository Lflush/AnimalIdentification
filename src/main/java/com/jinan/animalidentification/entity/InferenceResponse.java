package com.jinan.animalidentification.entity;

import java.util.List;
import java.util.Set;

public class InferenceResponse {
    private Set<String> conclusions;
    private List<String> inferencePath;

    public InferenceResponse(Set<String> conclusions, List<String> inferencePath) {
        this.conclusions = conclusions;
        this.inferencePath = inferencePath;
    }

    public Set<String> getConclusions() {
        return conclusions;
    }

    public List<String> getInferencePath() {
        return inferencePath;
    }
}
