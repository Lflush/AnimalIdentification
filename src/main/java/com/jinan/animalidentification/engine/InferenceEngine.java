package com.jinan.animalidentification.engine;

import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.InferenceResponse;
import com.jinan.animalidentification.entity.Rule;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InferenceEngine {
    private volatile List<Rule> rules = new CopyOnWriteArrayList<>();

    public InferenceEngine() {
        addInitialRules();
    }

    public synchronized void addInitialRules() {
        if (!rules.isEmpty()) {
            return;
        }
        List<Rule> seed = new ArrayList<>();
        seed.add(new Rule(Arrays.asList("有毛发"), "哺乳动物", 1));
        seed.add(new Rule(Arrays.asList("有奶"), "哺乳动物", 1));
        seed.add(new Rule(Arrays.asList("有羽毛"), "鸟", 1));
        seed.add(new Rule(Arrays.asList("会飞", "会生蛋"), "鸟", 1));
        seed.add(new Rule(Arrays.asList("吃肉"), "食肉动物", 2));
        seed.add(new Rule(Arrays.asList("有犀利牙齿", "有爪", "眼向前方"), "食肉动物", 2));
        seed.add(new Rule(Arrays.asList("哺乳动物", "有蹄"), "有蹄类动物", 2));
        seed.add(new Rule(Arrays.asList("哺乳动物", "反刍"), "有蹄类动物", 2));
        seed.add(new Rule(Arrays.asList("哺乳动物", "食肉动物", "有黄褐色", "有暗斑点"), "豹", 3));
        seed.add(new Rule(Arrays.asList("哺乳动物", "食肉动物", "有黄褐色", "有黑色条纹"), "虎", 3));
        seed.add(new Rule(Arrays.asList("有蹄类动物", "有长脖子", "有长腿", "有暗斑点"), "长颈鹿", 3));
        seed.add(new Rule(Arrays.asList("有蹄类动物", "有黑色条纹"), "斑马", 3));
        seed.add(new Rule(Arrays.asList("鸟", "不会飞", "有长脖子", "有长腿", "黑白二色"), "鸵鸟", 3));
        seed.add(new Rule(Arrays.asList("鸟", "不会飞", "会游泳", "黑白二色"), "企鹅", 3));
        seed.add(new Rule(Arrays.asList("鸟", "善飞"), "信天翁", 3));
        replaceRules(seed);
    }

    public InferenceResponse forwardInference(Animal animal) {
        Animal working = copyAnimal(animal);
        Set<String> conclusions = new HashSet<>();
        List<String> inferencePath = new ArrayList<>();
        Set<Rule> fired = new HashSet<>();
        List<Rule> snapshot = new ArrayList<>(rules);

        boolean hasNewConclusions = true;
        while (hasNewConclusions) {
            hasNewConclusions = false;
            for (Rule rule : snapshot) {
                if (fired.contains(rule) || !satisfies(working, rule)) {
                    continue;
                }
                String conclusion = rule.getConclusion();
                if (conclusions.contains(conclusion)) {
                    fired.add(rule);
                    continue;
                }
                hasNewConclusions = true;
                inferencePath.add("规则匹配: " + rule.getConditions() + " -> " + rule.getConclusion());
                inferencePath.add("更新动物属性: " + conclusion);
                inferencePath.add("------------------------------------------------");
                conclusions.add(conclusion);
                working.setAttribute(conclusion, true);
                fired.add(rule);
            }
        }

        return new InferenceResponse(conclusions, inferencePath);
    }

    public InferenceResponse backwardInference(List<String> goals) {
        if (goals == null || goals.isEmpty()) {
            return new InferenceResponse(Collections.<String>emptySet(), Collections.<String>emptyList());
        }

        Set<String> originals = new HashSet<>(goals);
        Set<String> derived = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>(goals);
        List<String> inferencePath = new ArrayList<>();
        List<Rule> snapshot = new ArrayList<>(rules);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (!visited.add(current)) {
                continue;
            }
            for (Rule rule : snapshot) {
                if (rule.getConclusion() == null || !rule.getConclusion().equals(current)) {
                    continue;
                }
                List<String> conditions = rule.getConditions();
                if (conditions == null) {
                    continue;
                }
                for (String condition : conditions) {
                    if (condition == null || visited.contains(condition)) {
                        continue;
                    }
                    inferencePath.add("规则匹配: " + rule.getConditions() + " -> " + rule.getConclusion());
                    inferencePath.add("添加条件: " + condition);
                    inferencePath.add("------------------------------------------------");
                    if (derived.add(condition)) {
                        stack.push(condition);
                    }
                }
            }
        }

        derived.removeAll(originals);
        return new InferenceResponse(derived, inferencePath);
    }

    public void clearCache() {
    }

    public synchronized void addRule(Rule rule) {
        List<Rule> next = new ArrayList<>(rules);
        next.add(rule);
        replaceRules(next);
    }

    public synchronized boolean deleteRule(Rule rule) {
        List<Rule> next = new ArrayList<>(rules);
        if (!next.remove(rule)) {
            return false;
        }
        this.rules = new CopyOnWriteArrayList<>(next);
        return true;
    }

    public synchronized boolean updateRule(Rule oldRule, Rule newRule) {
        List<Rule> next = new ArrayList<>(rules);
        int index = next.indexOf(oldRule);
        if (index == -1) {
            return false;
        }
        next.set(index, newRule);
        replaceRules(next);
        return true;
    }

    public List<Rule> getAllRules() {
        return new ArrayList<>(rules);
    }

    private void replaceRules(List<Rule> next) {
        next.sort(Comparator.comparingInt(Rule::getPriority).reversed());
        this.rules = new CopyOnWriteArrayList<>(next);
    }

    private boolean satisfies(Animal animal, Rule rule) {
        List<String> conditions = rule.getConditions();
        if (conditions == null || conditions.isEmpty() || rule.getConclusion() == null) {
            return false;
        }
        for (String condition : conditions) {
            Boolean value = animal.getAttribute(condition);
            if (value == null || !value) {
                return false;
            }
        }
        return true;
    }

    private Animal copyAnimal(Animal animal) {
        Animal working = new Animal();
        if (animal != null && animal.getAttributes() != null) {
            working.setAttributes(new HashMap<>(animal.getAttributes()));
        }
        return working;
    }
}
