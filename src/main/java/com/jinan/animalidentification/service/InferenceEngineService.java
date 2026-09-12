package com.jinan.animalidentification.service;

import com.jinan.animalidentification.engine.InferenceEngine;
import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.InferenceResponse;
import com.jinan.animalidentification.entity.Rule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InferenceEngineService {

    private final InferenceEngine inferenceEngine;

    public InferenceEngineService(InferenceEngine inferenceEngine) {
        this.inferenceEngine = inferenceEngine;
    }

    public InferenceResponse performForwardInference(Animal animal) {
        if (animal == null) {
            animal = new Animal();
        }
        return inferenceEngine.forwardInference(animal);
    }

    public InferenceResponse performBackwardInference(List<String> conclusions) {
        return inferenceEngine.backwardInference(conclusions);
    }

    public void clearInferenceCache() {
        inferenceEngine.clearCache();
    }

    public List<Rule> getAllRules() {
        return inferenceEngine.getAllRules();
    }

    public void addRule(Rule rule) {
        validateRule(rule);
        inferenceEngine.addRule(rule);
    }

    public boolean deleteRule(Rule rule) {
        if (rule == null) {
            return false;
        }
        return inferenceEngine.deleteRule(rule);
    }

    public boolean updateRule(Rule oldRule, Rule newRule) {
        if (oldRule == null) {
            return false;
        }
        validateRule(newRule);
        return inferenceEngine.updateRule(oldRule, newRule);
    }

    private void validateRule(Rule rule) {
        if (rule == null || rule.getConditions() == null || rule.getConditions().isEmpty()
                || rule.getConclusion() == null || rule.getConclusion().trim().isEmpty()) {
            throw new IllegalArgumentException("规则必须包含条件和结论");
        }
    }
}
