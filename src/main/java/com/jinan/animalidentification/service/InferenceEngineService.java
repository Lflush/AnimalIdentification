package com.jinan.animalidentification.service;

import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.InferenceEngine;
import com.jinan.animalidentification.entity.InferenceResponse;
import com.jinan.animalidentification.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class InferenceEngineService {

    private final InferenceEngine inferenceEngine;

    @Autowired
    public InferenceEngineService(InferenceEngine inferenceEngine) {
        inferenceEngine.addInitialRules();
        this.inferenceEngine = inferenceEngine;
    }

    /**
     * 进行正向推理
     * @param animal 待推理的动物
     * @return 推理出的结论
     */
    public InferenceResponse performForwardInference(Animal animal) {
//        System.out.println("Received animal attributes: {}"+ animal.getAttributes());
        // 清空缓存
        inferenceEngine.clearCache();
        return inferenceEngine.forwardInference(animal);
    }

    /**
     * 进行反向推理
     * @param conclusions 需要反向推理的结论
     * @return 反向推理得到的条件
     */
    public InferenceResponse performBackwardInference(List<String> conclusions) {
        // 清空缓存
        inferenceEngine.clearCache();
        return inferenceEngine.backwardInference(conclusions);
    }

    /**
     * 清空缓存和推理路径
     */
    public void clearInferenceCache() {
        inferenceEngine.clearCache();
    }

    /**
     * 获取当前所有规则
     * @return 规则列表
     */
    public List<Rule> getAllRules() {
        return inferenceEngine.getAllRules();
    }

    /**
     * 添加规则
     * @param rule 需要添加的规则
     */
    public void addRule(Rule rule) {
        inferenceEngine.addRule(rule);
    }

    /**
     * 删除规则
     * @param rule 需要删除的规则
     */
    public void deleteRule(Rule rule) {
        inferenceEngine.deleteRule(rule);
    }

    /**
     * 更新规则
     * @param oldRule 旧规则
     * @param newRule 新规则
     */
    public void updateRule(Rule oldRule, Rule newRule) {
        inferenceEngine.updateRule(oldRule, newRule);
    }
}
