package com.paypal.reward_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paypal.reward_service.entity.Reward;
import com.paypal.reward_service.entity.Transaction;
import com.paypal.reward_service.repository.RewardRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RewardConsumer {

    private final RewardRepository rewardRepository;
    private final ObjectMapper objectMapper;


    public RewardConsumer(RewardRepository repository, ObjectMapper objectMapper) {
        this.rewardRepository = repository;

        this.objectMapper=new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @KafkaListener(topics = "txn-initiated",groupId = "reward-group")
    public void consumeTransaction(Transaction transaction){
        try{
            if (rewardRepository.existsByTransactionId(transaction.getId())){
                System.out.println("Reward already exists for transaction: "+transaction.getId());
                return;
            }
            Reward reward=new Reward();
            reward.setUserId(transaction.getSenderId());
            reward.setPoints(transaction.getAmount()*100);
            reward.setSentAt(LocalDateTime.now());
            reward.setTransactionId(transaction.getId());

            rewardRepository.save(reward);
            System.out.println("Reward saved: "+reward);
        }catch (Exception ex){
            System.err.println("Failed to process transaction "+transaction.getId()+": "+ex.getMessage());
            throw ex;
        }
    }
}
