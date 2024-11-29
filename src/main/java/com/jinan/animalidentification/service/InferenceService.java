package com.jinan.animalidentification.service;

import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.InferenceEngine;
import com.jinan.animalidentification.entity.Rule;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InferenceService {


    private InferenceEngine engine;
    private void initializeRules() {

        List<Rule> rules = new ArrayList<>();
        List<String> inferencePath= new ArrayList<>();
        // 初始化一些初始规则
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

        engine = new InferenceEngine(rules,inferencePath);
        engine.clearCache();
    }


}
