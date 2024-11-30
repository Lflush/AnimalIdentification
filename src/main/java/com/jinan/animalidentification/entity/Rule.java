package com.jinan.animalidentification.entity;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class Rule {
    private List<String> conditions; // 规则的条件部分
    private String conclusion; // 规则的结论部分
    private int priority=1; // 规则的优先级
    private double confidence=0.5;// 置信度

    public Rule(){};
    public Rule(List<String> conditions, String conclusion) {
        this.conditions = conditions;
        this.conclusion = conclusion;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public String getConclusion() {
        return conclusion;
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
