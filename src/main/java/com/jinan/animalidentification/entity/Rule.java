package com.jinan.animalidentification.entity;

import java.util.List;
import java.util.Objects;

public class Rule {
    private List<String> conditions;
    private String conclusion;
    private int priority = 1;

    public Rule() {
    }

    public Rule(List<String> conditions, String conclusion) {
        this(conditions, conclusion, 1);
    }

    public Rule(List<String> conditions, String conclusion, int priority) {
        this.conditions = conditions;
        this.conclusion = conclusion;
        this.priority = priority;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(conditions, rule.conditions) && Objects.equals(conclusion, rule.conclusion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditions, conclusion);
    }
}
