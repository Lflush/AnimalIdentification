package com.jinan.animalidentification;

import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.InferenceEngine;
import com.jinan.animalidentification.entity.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootTest
class AnimalIdentificationApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test() {
        // 创建规则
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


        // 对规则使用优先级策略排序
        rules.sort((r1, r2) -> r2.getPriority() - r1.getPriority());

        List<String> inferencePath = new ArrayList<>();
        // 创建推理引擎
        InferenceEngine engine = new InferenceEngine(rules,inferencePath);

        // 创建动物对象
        Animal animal = new Animal();
        animal.setAttribute("有毛发", true);
//        animal.setAttribute("有奶", true);
//        animal.setAttribute("有羽毛", false);
//        animal.setAttribute("会飞", false);
        animal.setAttribute("吃肉", true);
        animal.setAttribute("有犀利牙齿", true);
        animal.setAttribute("有爪", true);
        animal.setAttribute("眼向前方", true);
        animal.setAttribute("有黄褐色", true);
        animal.setAttribute("有暗斑点", true);

        // 正向推理
        Set<String> conclusions = engine.forwardInference(animal);
        System.out.println("推理路径：");
        for (String step : inferencePath) {
            System.out.println(step);
        }
        System.out.println("推理出的结论：");
        for (String conclusion : conclusions) {
            System.out.println(conclusion);
        }

        // 清除缓存
        engine.clearCache();

        // 反向推理：根据结论推导条件
        List<String> conclusions2 = new ArrayList<>();
        conclusions2.add("豹");
        Set<String> conditions = engine.backwardInference(conclusions2);
        System.out.println("推理路径：");
        for (String step : inferencePath) {
            System.out.println(step);
        }
        System.out.println("满足"+conclusions2+"结论的条件：");
        for (String condition : conditions) {
            System.out.println(condition);
        }
    }
}
