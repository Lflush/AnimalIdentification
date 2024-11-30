package com.jinan.animalidentification.entity;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InferenceEngine {
    private List<Rule> rules; // 存储规则
    private Map<Rule, Boolean> ruleCache; // 规则缓存，用于存储已经计算过的推理结果

    private List<String> inferencePath;//解释推理路径

    public InferenceEngine(List<Rule> rules, List<String> inferencePath) {
        this.rules = rules;
        this.inferencePath = inferencePath;
        this.ruleCache = new HashMap<>();// 初始化缓存
    }

    public void addInitialRules() {
        // 创建初始规则
        List<Rule> rules = new ArrayList<>();

        rules.add(new Rule(Arrays.asList("有毛发"), "哺乳动物"));
        rules.add(new Rule(Arrays.asList("有奶"), "哺乳动物"));
        rules.add(new Rule(Arrays.asList("有羽毛"), "鸟"));
        rules.add(new Rule(Arrays.asList("会飞", "会生蛋"), "鸟"));
        rules.add(new Rule(Arrays.asList("吃肉"), "食肉动物"));
        rules.add(new Rule(Arrays.asList("有犀利牙齿", "有爪", "眼向前方"), "食肉动物"));
        rules.add(new Rule(Arrays.asList("哺乳动物", "有蹄"), "有蹄类动物"));
        rules.add(new Rule(Arrays.asList("哺乳动物", "反刍"), "有蹄类动物"));
        rules.add(new Rule(Arrays.asList("哺乳动物", "食肉动物", "有黄褐色", "有暗斑点"), "豹"));
        rules.add(new Rule(Arrays.asList("哺乳动物", "食肉动物", "有黄褐色", "有黑色条纹"), "虎"));
        rules.add(new Rule(Arrays.asList("有蹄类动物", "有长脖子", "有长腿", "有暗斑点"), "长颈鹿"));
        rules.add(new Rule(Arrays.asList("有蹄类动物", "有黑色条纹"), "斑马"));
        rules.add(new Rule(Arrays.asList("鸟", "不会飞", "有长脖子", "有长腿", "黑白二色"), "鸵鸟"));
        rules.add(new Rule(Arrays.asList("鸟", "不会飞", "会游泳", "黑白二色"), "企鹅"));
        rules.add(new Rule(Arrays.asList("鸟", "善飞"), "信天翁"));

        this.rules = rules;
    }

    // 正向推理：根据动物的属性推断动物的类型
    public InferenceResponse forwardInference(Animal animal) {
        Set<String> conclusions = new HashSet<>(); // 存储推理结果

        boolean hasNewConclusions = true;
        while (hasNewConclusions) {
            hasNewConclusions = false;
            // 按优先级排序规则
            rules.sort(Comparator.comparingInt(Rule::getPriority).reversed());
            for (Rule rule : rules) {
                if (ruleCache.containsKey(rule) && ruleCache.get(rule)) {
                    continue;
                }
                boolean satisfiesConditions;
                satisfiesConditions = true;
                for (String condition : rule.getConditions()) {
                    if (animal.getAttribute(condition) == null || !animal.getAttribute(condition)) {
                        satisfiesConditions = false;
                        break;
                    }
                }

                String conclusion = rule.getConclusion();
                if (satisfiesConditions && !conclusions.contains(conclusion)) {
                    hasNewConclusions = true;
                    // 记录推理路径
                    inferencePath.add("规则匹配: " + rule.getConditions() + " -> " + rule.getConclusion());
                    inferencePath.add("更新动物属性: " + conclusion);
                    inferencePath.add("------------------------------------------------");
                    // 更新推理结果
                    conclusions.add(conclusion);
                    // 更新动物属性
                    animal.setAttribute(conclusion, true);// 更新动物属性
                    ruleCache.put(rule, true); // 缓存推理结果
                } else {
                    ruleCache.put(rule, false); // 缓存推理结果
                }
            }
        }

        return new InferenceResponse(conclusions, inferencePath);
    }

    // 反向推理：根据结论找到支持该结论的条件
    public InferenceResponse backwardInference(List<String> conclusion) {
        Set<String> conditions = new HashSet<>(conclusion);

        boolean hasNewConditions = true;
        while (hasNewConditions) {
            hasNewConditions = false;
            // 用于存储新条件
            Set<String> newConditions = new HashSet<>();
            for (Rule rule : rules) {
                for (String condition : conditions) {
                    if (rule.getConclusion().equals(condition)) {
                        for (String c : rule.getConditions()) {
                            if (!conditions.contains(c)&&!newConditions.contains(c)) {
                                hasNewConditions = true;
                                inferencePath.add("规则匹配: " + rule.getConditions() + " -> " + rule.getConclusion());
                                inferencePath.add("添加条件: " + c);
                                inferencePath.add("------------------------------------------------");
                                newConditions.add(c);
                            }
                        }
                    }
                }
            }
            // 将新条件添加到主集合
            conditions.addAll(newConditions);
        }

        return new InferenceResponse(conditions, inferencePath);
    }

    // 清除已知结论，用于重新推理
    public void clearCache() {
        inferencePath.clear();
        ruleCache.clear();
    }

    // 添加规则
    public void addRule(Rule rule) {
        rules.add(rule);
        ruleCache.clear(); // 清除缓存
    }

    // 删除规则
    public void deleteRule(Rule rule) {
        rules.remove(rule);
        ruleCache.clear(); // 清除缓存
    }

    // 更新规则
    public void updateRule(Rule oldRule, Rule newRule) {
        int index = rules.indexOf(oldRule);
        if (index != -1) {
            rules.set(index, newRule);
            ruleCache.clear(); // 清除缓存
        }
    }

    // 获取所有规则
    public List<Rule> getAllRules() {
        return rules;
    }
}
