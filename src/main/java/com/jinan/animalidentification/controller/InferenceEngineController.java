package com.jinan.animalidentification.controller;

import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.Rule;
import com.jinan.animalidentification.service.InferenceEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/inference")
public class InferenceEngineController {

    private final InferenceEngineService inferenceEngineService;

    @Autowired
    public InferenceEngineController(InferenceEngineService inferenceEngineService) {
        this.inferenceEngineService = inferenceEngineService;
    }

    /**
     * 执行正向推理
     * @param animal 待推理的动物
     * @return 推理结果
     */
    @PostMapping("/forward")
    public Set<String> forwardInference(@RequestBody Animal animal) {
        return inferenceEngineService.performForwardInference(animal);
    }

    /**
     * 执行反向推理
     * @param conclusions 推理的结论
     * @return 反向推理得到的条件
     */
    @PostMapping("/backward")
    public Set<String> backwardInference(@RequestBody List<String> conclusions) {
        return inferenceEngineService.performBackwardInference(conclusions);
    }

    /**
     * 清空推理缓存和路径
     * @return 响应消息
     */
    @DeleteMapping("/clear")
    public String clearCache() {
        inferenceEngineService.clearInferenceCache();
        return "Inference cache cleared.";
    }

    /**
     * 获取所有规则
     * @return 所有规则的列表
     */
    @GetMapping("/rules")
    public List<Rule> getAllRules() {
        return inferenceEngineService.getAllRules();
    }

    /**
     * 添加规则
     * @param rule 新的规则
     * @return 响应消息
     */
    @PostMapping("/rules")
    public String addRule(@RequestBody Rule rule) {
        inferenceEngineService.addRule(rule);
        return "Rule added successfully.";
    }

    /**
     * 删除规则
     * @param rule 需要删除的规则
     * @return 响应消息
     */
    @DeleteMapping("/rules")
    public String deleteRule(@RequestBody Rule rule) {
        inferenceEngineService.deleteRule(rule);
        return "Rule deleted successfully.";
    }

    /**
     * 更新规则
     * @param oldRule 旧规则
     * @param newRule 新规则
     * @return 响应消息
     */
    @PutMapping("/rules")
    public String updateRule(@RequestBody Rule oldRule, @RequestBody Rule newRule) {
        inferenceEngineService.updateRule(oldRule, newRule);
        return "Rule updated successfully.";
    }
}
