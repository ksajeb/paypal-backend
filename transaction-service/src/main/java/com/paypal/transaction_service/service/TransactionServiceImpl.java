package com.paypal.transaction_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transaction_service.dto.TransferRequestDto;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.kafka.KafkaEventProducer;
import com.paypal.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository repository;

    private final ObjectMapper objectMapper;

    private final KafkaEventProducer kafkaEventProducer;

    private final RestTemplate restTemplate;

    @Override
    public Transaction createTransaction(TransferRequestDto requestDto) {

        Transaction transaction=new Transaction();
        transaction.setSenderId(requestDto.getSenderId());
        transaction.setReceiverId(requestDto.getReceiverId());
        transaction.setAmount(requestDto.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("Success");

        Transaction savedTransaction= repository.save(transaction);
        try{
//            String eventPayload=objectMapper.writeValueAsString(savedTransaction);
            String key=String.valueOf(savedTransaction.getId());
            kafkaEventProducer.sendTransactionEvent(key,savedTransaction);
            System.out.println("Kafka message sent.");
        }catch (Exception ex){
            System.err.println("Failed to send Kafka event: "+ ex.getMessage());
            ex.printStackTrace();
        }
        return savedTransaction;
    }

    @Override
    public List<Transaction> getAllTransaction() {
        return repository.findAll();
    }
}
