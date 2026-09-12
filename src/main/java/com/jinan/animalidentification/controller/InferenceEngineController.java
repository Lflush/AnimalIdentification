package com.jinan.animalidentification.controller;

import com.jinan.animalidentification.dto.RuleUpdateRequest;
import com.jinan.animalidentification.entity.Animal;
import com.jinan.animalidentification.entity.InferenceResponse;
import com.jinan.animalidentification.entity.Rule;
import com.jinan.animalidentification.service.InferenceEngineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/inference")
public class InferenceEngineController {

    private final InferenceEngineService inferenceEngineService;

    public InferenceEngineController(InferenceEngineService inferenceEngineService) {
        this.inferenceEngineService = inferenceEngineService;
    }

    @PostMapping("/forward")
    public InferenceResponse forwardInference(@RequestBody Animal animal) {
        return inferenceEngineService.performForwardInference(animal);
    }

    @PostMapping("/backward")
    public InferenceResponse backwardInference(@RequestBody List<String> conclusions) {
        return inferenceEngineService.performBackwardInference(conclusions);
    }

    @PostMapping("/clear")
    public String clearCache() {
        inferenceEngineService.clearInferenceCache();
        return "Inference cache cleared.";
    }

    @GetMapping({"/rules", "/rules/all"})
    public List<Rule> getAllRules() {
        return inferenceEngineService.getAllRules();
    }

    @PostMapping({"/rules", "/rules/add"})
    public ResponseEntity<String> addRule(@RequestBody Rule rule) {
        try {
            inferenceEngineService.addRule(rule);
            return ResponseEntity.ok("Rule added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/rules")
    public ResponseEntity<String> deleteRule(@RequestBody Rule rule) {
        return delete(rule);
    }

    @PostMapping("/rules/delete")
    public ResponseEntity<String> deleteRuleLegacy(@RequestBody Rule rule) {
        return delete(rule);
    }

    @PutMapping("/rules")
    public ResponseEntity<String> updateRule(@RequestBody RuleUpdateRequest request) {
        return update(request);
    }

    @PostMapping("/rules/update")
    public ResponseEntity<String> updateRuleLegacy(@RequestBody RuleUpdateRequest request) {
        return update(request);
    }

    private ResponseEntity<String> delete(Rule rule) {
        if (!inferenceEngineService.deleteRule(rule)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rule not found.");
        }
        return ResponseEntity.ok("Rule deleted successfully.");
    }

    private ResponseEntity<String> update(RuleUpdateRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("请求体不能为空");
        }
        try {
            if (!inferenceEngineService.updateRule(request.getOldRule(), request.getNewRule())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rule not found.");
            }
            return ResponseEntity.ok("Rule updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
