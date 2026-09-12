package com.jinan.animalidentification.engine;

import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.InferenceResponse;
import com.jinan.animalidentification.entity.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InferenceEngineTest {

    private InferenceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new InferenceEngine();
    }

    @Test
    void forwardInferenceIdentifiesLeopard() {
        Animal animal = leopardFacts();

        Set<String> conclusions = engine.forwardInference(animal).getConclusions();

        assertTrue(conclusions.contains("哺乳动物"));
        assertTrue(conclusions.contains("食肉动物"));
        assertTrue(conclusions.contains("豹"));
        assertFalse(conclusions.contains("虎"));
        assertFalse(animal.getAttributes().containsKey("豹"));
    }

    @Test
    void backwardInferenceReturnsSupportConditionsWithoutGoal() {
        Set<String> conditions = engine.backwardInference(Collections.singletonList("豹")).getConclusions();

        assertFalse(conditions.contains("豹"));
        assertTrue(conditions.contains("哺乳动物"));
        assertTrue(conditions.contains("食肉动物"));
        assertTrue(conditions.contains("有毛发"));
        assertTrue(conditions.contains("吃肉"));
        assertTrue(conditions.contains("有黄褐色"));
        assertTrue(conditions.contains("有暗斑点"));
    }

    @Test
    void addRuleEnablesNewConclusion() {
        engine.addRule(new Rule(Arrays.asList("豹", "会爬树"), "花豹", 3));
        Animal animal = leopardFacts();
        animal.setAttribute("会爬树", true);

        Set<String> conclusions = engine.forwardInference(animal).getConclusions();

        assertTrue(conclusions.contains("花豹"));
    }

    @Test
    void deleteRuleStopsMatching() {
        Rule leopardRule = new Rule(Arrays.asList("哺乳动物", "食肉动物", "有黄褐色", "有暗斑点"), "豹");

        assertTrue(engine.deleteRule(leopardRule));
        Set<String> conclusions = engine.forwardInference(leopardFacts()).getConclusions();

        assertTrue(conclusions.contains("哺乳动物"));
        assertTrue(conclusions.contains("食肉动物"));
        assertFalse(conclusions.contains("豹"));
    }

    @Test
    void updateRuleReplacesConclusion() {
        Rule oldRule = new Rule(Arrays.asList("哺乳动物", "食肉动物", "有黄褐色", "有暗斑点"), "豹");
        Rule newRule = new Rule(Arrays.asList("哺乳动物", "食肉动物", "有黄褐色", "有暗斑点"), "花豹", 3);

        assertTrue(engine.updateRule(oldRule, newRule));
        Set<String> conclusions = engine.forwardInference(leopardFacts()).getConclusions();

        assertTrue(conclusions.contains("花豹"));
        assertFalse(conclusions.contains("豹"));
    }

    @Test
    void cyclicRulesTerminate() {
        engine.addRule(new Rule(Collections.singletonList("甲"), "乙"));
        engine.addRule(new Rule(Collections.singletonList("乙"), "甲"));

        InferenceResponse response = engine.backwardInference(Collections.singletonList("甲"));

        assertTrue(response.getConclusions().contains("乙"));
        assertFalse(response.getConclusions().contains("甲"));
    }

    @Test
    void addInitialRulesIsIdempotent() {
        int size = engine.getAllRules().size();
        engine.addInitialRules();
        assertEquals(size, engine.getAllRules().size());
    }

    private Animal leopardFacts() {
        Animal animal = new Animal();
        animal.setAttribute("有毛发", true);
        animal.setAttribute("吃肉", true);
        animal.setAttribute("有黄褐色", true);
        animal.setAttribute("有暗斑点", true);
        return animal;
    }
}
