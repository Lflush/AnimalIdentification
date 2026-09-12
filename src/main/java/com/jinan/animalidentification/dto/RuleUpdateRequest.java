package com.jinan.animalidentification.dto;

import com.jinan.animalidentification.entity.Rule;

public class RuleUpdateRequest {
    private Rule oldRule;
    private Rule newRule;

    public Rule getOldRule() {
        return oldRule;
    }

    public void setOldRule(Rule oldRule) {
        this.oldRule = oldRule;
    }

    public Rule getNewRule() {
        return newRule;
    }

    public void setNewRule(Rule newRule) {
        this.newRule = newRule;
    }
}
