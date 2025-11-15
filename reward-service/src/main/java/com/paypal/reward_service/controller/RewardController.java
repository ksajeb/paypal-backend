package com.paypal.reward_service.controller;

import com.paypal.reward_service.entity.Reward;
import com.paypal.reward_service.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardRepository rewardRepository;

    @GetMapping
    public List<Reward> getAllRewards(){
        return rewardRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Reward> getRewardByUserId(@PathVariable Long userId){
        return rewardRepository.findByUserId(userId);
    }

}
